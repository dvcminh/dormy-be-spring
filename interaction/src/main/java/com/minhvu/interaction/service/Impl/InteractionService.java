package com.minhvu.interaction.service.Impl;

import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.InteractionDto;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.service.IcommentService;
import com.minhvu.interaction.service.IinteractionService;
import com.minhvu.interaction.service.IreactionService;
import com.minhvu.interaction.service.IsharedService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class InteractionService implements IinteractionService {

    private final IcommentService icommentService;
    private final IsharedService isharedService;
    private final IreactionService ireactionService;

    @Override
    public InteractionDto getInteractionsOfPost(Long postId)
    {
        InteractionDto interactionDto = new InteractionDto();
        List<CommentDto> comments = icommentService.getAllCommentsByPostId(postId);
        List<SharedDto> shareds = isharedService.getAllSharedByPostId(postId);
        List<ReactionDto> reactions = ireactionService.getAllReactionsByPostId(postId);
        int countComments = icommentService.getCountOfCommentsByPost(postId);
        int countShareds = isharedService.getCountSharedsOfPost(postId);
        int countReactions = ireactionService.getCountReactionsOfPost(postId);
        int countLikes = ireactionService.getLikeCountOfPost(postId);
        int countLoves = ireactionService.getLoveCountOfPost(postId);
        int countWows = ireactionService.getWowCountOfPost(postId);
        int countAngry = ireactionService.getAngryCountOfPost(postId);
        int countSad = ireactionService.getSadCountOfPost(postId);
        int countHahah = ireactionService.getHahahCountOfPost(postId);
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
