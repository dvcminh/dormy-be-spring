package com.minhvu.review.service;

import com.minhvu.review.client.FriendClient;
import com.minhvu.review.client.InteractionClient;
import com.minhvu.review.client.MediaClient;
import com.minhvu.review.dto.*;
import com.minhvu.review.dto.AppUserDto;
import com.minhvu.review.dto.FriendDto;
import com.minhvu.review.dto.InteractionDto;
import com.minhvu.review.dto.mapper.PostMapper;
import com.minhvu.review.dto.request.Notification;
import com.minhvu.review.dto.request.NotificationComponent;
import com.minhvu.review.dto.request.PostRequest;
import com.minhvu.review.dto.request.PostUpdateRequest;
import com.minhvu.review.exception.BadRequestException;
import com.minhvu.review.kafka.NotificationProducer;
import com.minhvu.review.kafka.PostProducer;
import com.minhvu.review.model.AppUser;
import com.minhvu.review.model.PostEntity;
import com.minhvu.review.repository.AppUserRepository;
import com.minhvu.review.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostMapper postMapper;
    @Qualifier("com.minhvu.review.client.MediaClient")
    private final MediaClient mediaClient;
    @Qualifier("com.minhvu.review.client.InteractionClient")
    private final InteractionClient interactionClient;
    private final PostProducer postProducer;
    private final FriendClient friendClient;
    private final AppUserRepository appUserRepository;
    private final NotificationProducer notificationProducer;

    @Transactional
    public PostEntityDto createPost(UUID userId, PostRequest postRequest) {
        PostEntity postEntity = PostEntity.builder()
                .body(postRequest.getBody())
                .userId(userId)
                .urlsMedia(postRequest.getUrlsMedia())
                .isDeleted(false)
                .build();
        postEntity.setCreatedBy(userId);
        postEntity.setUpdatedBy(userId);
        postEntity = postRepository.saveAndFlush(postEntity);
        PostEntityDto postResponse = postMapper.toDto(postEntity);
        postProducer.send(postResponse);
        return postResponse;
    }

    public List<PostWithInteractionResponse> getAllPost(UUID userId) {
        List<PostWithInteractionResponse> postWithInteractionResponses = new ArrayList<>();
        List<PostEntity> postEntities = postRepository.findPostEntitiesByUserId(userId);
        postEntities.forEach(postEntity -> {
            List<MediaDTO> mediaDTOS = mediaClient.getMediaByPostId(postEntity.getId());
            PostWithInteractionResponse postWithInteractionResponse = new PostWithInteractionResponse();
            postWithInteractionResponse.setPostResponse(PostResponse.builder()
//                    .post(postMapper.toDto(postEntity))
//                    .medias(mediaDTOS)
                    .build());
            InteractionDto intercationResponse = interactionClient.getInteractionsOfPost(postEntity.getId()).getBody();
            postWithInteractionResponse.setInteractionDto(intercationResponse);
            postWithInteractionResponses.add(postWithInteractionResponse);

        });
        return postWithInteractionResponses;
    }

    public List<PostWithInteractionResponse> getAllPostByUserId(UUID userId) {
        List<PostWithInteractionResponse> postWithInteractionResponses = new ArrayList<>();
        //find friend of user
        FriendDto friends =  friendClient.getFriendsOfUser(userId).getBody();
        List<UUID> friendIds = friends.getFriendId();
        friendIds.forEach(friendId -> {
            List<PostEntity> postEntities = postRepository.findPostEntitiesByUserId(friendId);
            postEntities.forEach(postEntity -> {
                List<MediaDTO> mediaDTOS = mediaClient.getMediaByPostId(postEntity.getId());
                PostWithInteractionResponse postWithInteractionResponse = new PostWithInteractionResponse();
                postWithInteractionResponse.setPostResponse(PostResponse.builder()
//                        .post(postMapper.toDto(postEntity))
//                        .medias(mediaDTOS)
                        .build());
                InteractionDto intercationResponse = interactionClient.getInteractionsOfPost(postEntity.getId()).getBody();
                postWithInteractionResponse.setInteractionDto(intercationResponse);
                postWithInteractionResponses.add(postWithInteractionResponse);
            });
        });
        return postWithInteractionResponses;
    }

    public void delete(UUID postId, AppUserDto currentUser) {
        PostEntity postEntity = postRepository.findByIdAndUserId(postId, currentUser.getId())
                .orElseThrow(() -> new BadRequestException(
                        String.format("Post with id [%s] not found", postId)
                ));
        if (postEntity.getIsDeleted())
            throw new BadRequestException(
                    String.format("Post with id [%s] has already deleted", postId)
            );
        postEntity.setIsDeleted(true);
        postEntity.setUpdatedBy(currentUser.getId());
        postRepository.saveAndFlush(postEntity);
        postProducer.send(postMapper.toDto(postEntity));
    }

    public Notification generateNotification(PostEntity postEntity) {
        Collection<UUID> toUsersId = new ArrayList<>();
        toUsersId.addAll(appUserRepository.findById(
                postEntity.getCreatedBy()
        ).stream().map(AppUser::getId).collect(Collectors.toList()));

        Date date = Date.from(Instant.from(ZonedDateTime.now().toLocalDate()));


        return Notification.builder()
                .component(new NotificationComponent(
                        "Post",
                        "Post",
                        postEntity.getId()
                ))
                .message("New post entity")
                .description(String.format("Post created by %s", postEntity.getCreatedBy()))
                .createdAt(date)
                .toUserIds(toUsersId)
                .createdBy(postEntity.getCreatedBy())
                .build();
    }

    public void restore(UUID postId, AppUserDto currentUser) {
        PostEntity postEntity = postRepository.findByIdAndUserId(postId, currentUser.getId())
                .orElseThrow(() -> new BadRequestException(
                        String.format("Post with id [%s] not found", postId)
                ));
        if (!postEntity.getIsDeleted())
            throw new BadRequestException(
                    String.format("Post with id [%s] has already displayed", postId)
            );
        postEntity.setIsDeleted(false);
        postEntity.setUpdatedBy(currentUser.getId());
        postRepository.save(postEntity);
        postProducer.send(postMapper.toDto(postEntity));
    }

    public void update(UUID postId, PostUpdateRequest postUpdateRequest, AppUserDto currentUser) {
        PostEntity postEntity = postRepository.findByIdAndUserId(postId, currentUser.getId())
                .orElseThrow(() -> new BadRequestException(
                        String.format("Post with id [%s] not found", postId)
                ));
        postEntity.setBody(postUpdateRequest.getBody());
        postEntity.setUrlsMedia(postUpdateRequest.getUrlsMedia());
        postEntity.setUpdatedBy(currentUser.getId());
        postRepository.save(postEntity);
        postProducer.send(postMapper.toDto(postEntity));
    }
}
