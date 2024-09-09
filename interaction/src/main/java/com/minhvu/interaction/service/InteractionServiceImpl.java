package com.minhvu.interaction.service;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.InteractionDto;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.SharedDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Transactional
public class InteractionServiceImpl implements InteractionService {

    private final CommentService commentService;
    private final SharedService sharedService;
    private final ReactionService reactionService;

}
