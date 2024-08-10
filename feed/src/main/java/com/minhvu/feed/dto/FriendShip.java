package com.minhvu.feed.dto;

import lombok.AllArgsConstructor;

import java.util.List;
@AllArgsConstructor
public class FriendShip {

    public Long userId;
    public List<Long> friendsIds;
}
