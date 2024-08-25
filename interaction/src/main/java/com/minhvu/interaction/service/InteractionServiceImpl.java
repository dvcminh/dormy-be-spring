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

    @Override
    public InteractionDto getInteractionsOfPost(UUID postId)
    {
        InteractionDto interactionDto = new InteractionDto();
        List<CommentDto> comments = commentService.getAllCommentsByPostId(postId);
        List<SharedDto> shareds = sharedService.getAllSharedByPostId(postId);
        List<ReactionDto> reactions = reactionService.getAllReactionsByPostId(postId);
        int countComments = commentService.getCountOfCommentsByPost(postId);
        int countShareds = sharedService.getCountSharedsOfPost(postId);
        int countReactions = reactionService.getCountReactionsOfPost(postId);
        int countLikes = reactionService.getLikeCountOfPost(postId);
        int countLoves = reactionService.getLoveCountOfPost(postId);
        int countWows = reactionService.getWowCountOfPost(postId);
        int countAngry = reactionService.getAngryCountOfPost(postId);
        int countSad = reactionService.getSadCountOfPost(postId);
        int countHahah = reactionService.getHahahCountOfPost(postId);
        interactionDto.setComments(comments);
        interactionDto.setShareds(shareds);
        interactionDto.setReactions(reactions);
        interactionDto.setCountComments(countComments);
        interactionDto.setCountShareds(countShareds);
        interactionDto.setCountLikes(countLikes);
        interactionDto.setCountAngry(countAngry);
        interactionDto.setCountReactions(countReactions);
        interactionDto.setCountHahah(countHahah);
        interactionDto.setCountLoves(countLoves);
        interactionDto.setCountSad(countSad);
        interactionDto.setCountWow(countWows);
        return interactionDto;
    }
}
