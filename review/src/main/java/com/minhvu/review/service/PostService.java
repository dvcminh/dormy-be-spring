package com.minhvu.review.service;

import com.minhvu.review.client.FriendClient;
import com.minhvu.review.client.InteractionClient;
import com.minhvu.review.client.MediaClient;
import com.minhvu.review.dto.*;
import com.minhvu.review.dto.inter.FriendDto;
import com.minhvu.review.dto.inter.InteractionDto;
import com.minhvu.review.dto.mapper.PostMapper;
import com.minhvu.review.exception.PostException;
import com.minhvu.review.exception.PostNotFoundException;
import com.minhvu.review.model.PostEntity;
import com.minhvu.review.producer.PostProducer;
import com.minhvu.review.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    private final PostProducer producer;
    private final FriendClient friendClient;

    @Transactional
    public PostResponse createPost(UUID userId, PostRequest postRequest) {
        PostEntityDto postEntityDto = PostEntityDto.builder()
                .body(postRequest.getBody())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .userId(userId)
                .build();
        PostEntity postEntity = postMapper.toModel(postEntityDto);
        postEntity = postRepository.save(postEntity);
        PostResponse postResponse = new PostResponse();
        postResponse.setPost(postEntityDto);
        postResponse.getPost().setId(postEntity.getId());
        if (postRequest.getMultipartFiles() != null && !postRequest.getMultipartFiles().isEmpty()) {
            postResponse.setMedias(mediaClient.add(postRequest.getMultipartFiles(), postEntity.getId(), userId).getBody());

        }
        producer.sendMessage(new PostProducerDto(postResponse.getPost().getId(), postResponse.getPost().getBody(), postResponse.getPost().getUserId(), null));
        return postResponse;
    }


    public PostResponse updatePost(UUID userId, UUID id, PostUpdateRequest postUpdateRequest) {

        //check if the post exist
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        //check if the user is the owner of the post
        if (!postEntity.getUserId().equals(userId)) throw new PostException("You are not the owner of the post");
        //check if there is media to delete
        if (postUpdateRequest.getMediaUuidsToDelete() != null && !postUpdateRequest.getMediaUuidsToDelete().isEmpty()) {
            postUpdateRequest.getMediaUuidsToDelete().forEach(mediaUuid -> {
                // call media client to delete media
                //send post id and media id and user id
                mediaClient.delete(mediaUuid,  userId, id);
            });
        }
        // get the list of media for this post
        List<MediaDTO> mediaDTOS = mediaClient.getMediaByPostId(id);
        log.info("mediaDTOS {} ", mediaDTOS);
        //check if there is media to add
        if (postUpdateRequest.getMultipartFiles() != null && !postUpdateRequest.getMultipartFiles().isEmpty()) {

            List<MediaDTO> mediaDTOS1 = mediaClient.add(postUpdateRequest.getMultipartFiles(), id, userId).getBody();
            mediaDTOS.addAll(mediaDTOS1);
        }

        postEntity.setBody(postUpdateRequest.getBody());
        postEntity = postRepository.save(postEntity);
        PostResponse postResponse = new PostResponse();
        postResponse.setPost(postMapper.toDto(postEntity));
        postResponse.setMedias(mediaDTOS);
        return postResponse;
    }

  public void deletePost(UUID userId, UUID id) {
        PostEntity postEntity = postRepository.findById(id).orElseThrow(() -> new PostNotFoundException("Post not found"));
        if (!postEntity.getUserId().equals(userId)) throw new PostException("You are not the owner of the post");
        mediaClient.deleteMediaByPostId(id);
        postRepository.delete(postEntity);

    }

    public List<PostEntityDto> getPostsByUserId(UUID id) {
        log.info("id: {}", id);
        return postRepository.findPostEntitiesByUserId(id).stream()
                .map(postMapper::toDto)
                .toList();
    }

    public List<PostWithInteractionResponse> getAllPost(UUID userId) {
        List<PostWithInteractionResponse> postWithInteractionResponses = new ArrayList<>();
        List<PostEntity> postEntities = postRepository.findPostEntitiesByUserId(userId);
        postEntities.forEach(postEntity -> {
            List<MediaDTO> mediaDTOS = mediaClient.getMediaByPostId(postEntity.getId());
            PostWithInteractionResponse postWithInteractionResponse = new PostWithInteractionResponse();
            postWithInteractionResponse.setPostResponse(PostResponse.builder()
                    .post(postMapper.toDto(postEntity))
                    .medias(mediaDTOS)
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
                        .post(postMapper.toDto(postEntity))
                        .medias(mediaDTOS)
                        .build());
                InteractionDto intercationResponse = interactionClient.getInteractionsOfPost(postEntity.getId()).getBody();
                postWithInteractionResponse.setInteractionDto(intercationResponse);
                postWithInteractionResponses.add(postWithInteractionResponse);
            });
        });
        return postWithInteractionResponses;
    }
}
