package com.minhvu.review.client;

import com.minhvu.review.dto.inter.InteractionDto;
import com.minhvu.review.exception.InteractionFallbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class InteractionFallback implements InteractionClient{
    @Override
    public ResponseEntity<InteractionDto> getInteractionsOfPost(UUID postId) {
        log.error("Error", postId);
        throw new InteractionFallbackException("Error" + postId);

    }
}
