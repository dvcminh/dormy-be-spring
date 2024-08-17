package com.minhvu.feed.dto;

import lombok.Data;

import java.util.List;
@Data
public class CompletReaction {

    public List<CommentDto> comment;
    public List<SharedDto> shared;
    public List<ReactionDto> reaction;

}
