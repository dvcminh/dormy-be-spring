package com.minhvu.feed.service;

import com.minhvu.feed.dto.InteractionDto;
import com.minhvu.feed.dto.MediaDto;
import com.minhvu.feed.dto.PostResponse;
import com.minhvu.feed.dto.PostWithInteractionResponse;
import com.minhvu.feed.dto.mapper.PostMapper;
import com.minhvu.feed.model.PostEntity;
import com.minhvu.feed.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final InteractionService interactionService;
    private final MediaService mediaService;
    private final PostMapper postMapper;
    @Override
    public List<PostWithInteractionResponse> getPostsByUserId(UUID userId) {
        List<PostWithInteractionResponse> postWithInteractionResponses = new ArrayList<>();
        List<PostEntity> postEntities = postRepository.findPostEntitiesByUserId(userId);
        postEntities.forEach(postEntity -> {
            List<MediaDto> mediaDTOS = mediaService.getMediaByPostId(postEntity.getId());
            PostWithInteractionResponse postWithInteractionResponse = new PostWithInteractionResponse();
            postWithInteractionResponse.setPostResponse(PostResponse.builder()
                    .post(postMapper.toDto(postEntity))
                    .medias(mediaDTOS)
                    .build());
            InteractionDto intercationResponse = interactionService.getInteractionsOfPost(postEntity.getId());
            postWithInteractionResponse.setInteractionDto(intercationResponse);
            postWithInteractionResponses.add(postWithInteractionResponse);

        });
        return postWithInteractionResponses;
    }
}
