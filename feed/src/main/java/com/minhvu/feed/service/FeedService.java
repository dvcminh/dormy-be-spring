package com.minhvu.feed.service;

import com.minhvu.feed.client.FriendshipServiceClient;
import com.minhvu.feed.client.InteractionServiceClient;
import com.minhvu.feed.client.PostServiceClient;
import com.minhvu.feed.dto.CompletReaction;
import com.minhvu.feed.dto.FriendDto;
import com.minhvu.feed.dto.PostDto;
import com.minhvu.feed.dto.PostWithInteractionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

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

    public HashMap<String, List<PostWithInteractionResponse>> getFeed(Long userId) {
        if (redisTemplate.hasKey(String.valueOf(userId))) {
            HashMap<String, List<PostWithInteractionResponse>> map = getHashMapFromRedis(String.valueOf(userId));
            return map;
        }
        HashMap<String, List<PostWithInteractionResponse>> listHashMap = new HashMap<>();
//        List<FriendShip> friendShips = friendshipServiceClient.getFriends(userId);
        // Check if the user has friends
        FriendDto friendShips = friendshipServiceClient.getFriends(userId).getBody();
        if (friendShips == null) {
            return null;
        }
        friendShips.getFriendId().forEach(friendId -> {
            List<PostWithInteractionResponse> postDto = postServiceClient.getPostByUser(friendId).getBody();
            listHashMap.put(String.valueOf(friendId), postDto);
        });

        saveHashMapToRedis(String.valueOf(userId), listHashMap);
        return listHashMap;
    }

    public void saveHashMapToRedis(String key, HashMap<String, List<PostWithInteractionResponse>> map) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        ops.set(key, map);
        redisTemplate.expire(key, 10, TimeUnit.MINUTES);
    }

    public HashMap<String, List<PostWithInteractionResponse>> getHashMapFromRedis(String key) {
        ValueOperations<String, Object> ops = this.redisTemplate.opsForValue();
        return (HashMap<String, List<PostWithInteractionResponse>>) ops.get(key);
    }

}
