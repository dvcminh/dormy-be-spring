package com.minhvu.feed.client;

import com.minhvu.feed.dto.PostDto;
import com.minhvu.feed.exception.PostException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PostClientFallback implements PostServiceClient {
    @Override
    public PostDto getPost(Long postId) {
        throw new PostException("there was an error geting the post with that id "+postId);
    }

    @Override
    public List<PostDto> getPostByUser(Long userId) {
        throw new PostException("there was an error geting the post with that userid "+userId);
    }
}
