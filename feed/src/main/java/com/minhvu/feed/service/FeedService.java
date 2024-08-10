package com.minhvu.feed.service;

import com.minhvu.feed.client.FriendshipServiceClient;
import com.minhvu.feed.client.InteractionServiceClient;
import com.minhvu.feed.client.PostServiceClient;
import com.minhvu.feed.dto.CompletReaction;
import com.minhvu.feed.dto.FriendShip;
import com.minhvu.feed.dto.PostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final FriendshipServiceClient friendshipServiceClient;
    private final InteractionServiceClient interactionServiceClient;
    private final PostServiceClient postServiceClient;
    private final RedisTemplate<String, Object> redisTemplate;

    public HashMap<PostDto, List<CompletReaction>> getFeed(Long userId) {
        if (redisTemplate.hasKey(Long.toString(userId))) {
            HashMap<PostDto, List<CompletReaction>> map = getHashMapFromRedis(Long.toString(userId));
            return map;
        }
        HashMap<PostDto, List<CompletReaction>> listHashMap = new HashMap<>();
//        List<FriendShip> friendShips = friendshipServiceClient.getFriends(userId);
        // Check if the user has friends
        List<FriendShip> friendShips = friendshipServiceClient.getFriends(userId);
        if (friendShips == null || friendShips.isEmpty()) {
            return null;
        }
        friendShips.forEach(friendShip -> {
            List<PostDto> postDto = postServiceClient.getPostByUser(friendShip.userId);
            postDto.stream().forEach(postDto1 -> {
                List<CompletReaction> completReaction = interactionServiceClient.getReactionsByPostId(postDto1.id);
                listHashMap.put(postDto1, completReaction);
            })
            ;
        });
        saveHashMapToRedis(Long.toString(userId), listHashMap);
        return listHashMap;
    }

    public void saveHashMapToRedis(String key, HashMap<PostDto, List<CompletReaction>> map) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        ops.set(key, map);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    public HashMap<PostDto, List<CompletReaction>> getHashMapFromRedis(String key) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        return (HashMap<PostDto, List<CompletReaction>>) ops.get(key);
    }

}
