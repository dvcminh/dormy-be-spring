package com.minhvu.review.client;

import com.minhvu.review.dto.inter.InteractionDto;
import com.minhvu.review.exception.InteractionFallbackException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class InteractionFallback implements InteractionClient{
    @Override
    public ResponseEntity<InteractionDto> getInteractionsOfPost(Long postId) {
        log.error("La recuperation des interaction a echoue", postId);
        throw new InteractionFallbackException("La recuperation des interaction a echoue" + postId);

    }
}
