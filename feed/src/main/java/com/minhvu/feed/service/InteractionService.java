package com.minhvu.feed.service;

import com.minhvu.feed.dto.InteractionDto;
import io.lettuce.core.StreamMessage;

import java.util.UUID;

public interface InteractionService {
    InteractionDto getInteractionsOfPost(UUID id);
}
