package com.minhvu.feed.client;

import com.minhvu.feed.dto.CompletReaction;
import com.minhvu.feed.exception.InteractionFallbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class InteractionFallback implements InteractionServiceClient{
    @Override
    public List<CompletReaction> getReactionsByPostId(Long postId) {
        log.error("La recuperation des interaction a echoue", postId);
        throw new InteractionFallbackException("La recuperation des interaction a echoue" + postId);
    }
}
