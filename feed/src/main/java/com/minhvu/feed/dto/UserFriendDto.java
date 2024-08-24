package com.minhvu.feed.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class UserFriendDto {

    public Long userId;
    public List<Long> friendId;
}
