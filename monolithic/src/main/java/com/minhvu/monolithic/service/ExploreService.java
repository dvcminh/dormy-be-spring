package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.dto.response.PostResponseDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Follow;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.enums.AccountType;
import com.minhvu.monolithic.enums.FollowStatus;
import com.minhvu.monolithic.enums.PostType;
import com.minhvu.monolithic.repository.FollowRepository;
import com.minhvu.monolithic.repository.PostRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExploreService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    private final AppUserMapper appUserMapper;
    public ResponseEntity<?> searchUser(String query, AppUser userDetails) {

        // Validate the query length
        if (query.length() < 3) { // Check for query length
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter at least 3 characters");
        }

        // Fetch all users matching the query (by username or full name)
        List<AppUser> allResults = userRepository.findByUsernameIgnoreCaseContainingOrDisplayNameIgnoreCaseContaining(query, query);

        // 1. Find exact matches (users with a username exactly matching the query)
        List<AppUser> exactMatch = allResults.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(query))
                .toList();

        // 2. Find users that the current user is following
        List<Follow> followingList = followRepository.findByFollower(userDetails);

        Set<AppUser> followingUsers = followingList.stream()
                .map(follow -> follow.getFollowing())
                .collect(Collectors.toSet());

        List<AppUser> followingMatches = allResults.stream()
                .filter(user -> followingUsers.contains(user) && !exactMatch.contains(user))
                .toList();

        // 3. Find users who are following the current user
        List<Follow> followerList = followRepository.findByFollowing(userDetails);
        Set<AppUser> followerUsers = followerList.stream()
                .map(follow -> follow.getFollower())
                .collect(Collectors.toSet());

        List<AppUser> followerMatches = allResults.stream()
                .filter(user -> followerUsers.contains(user) && !exactMatch.contains(user) && !followingMatches.contains(user))
                .toList();

        // 4. Filter remaining matches (users not in exact, following, or follower matches)
        List<AppUser> otherMatches = allResults.stream()
                .filter(user -> !exactMatch.contains(user) && !followingMatches.contains(user) && !followerMatches.contains(user))
                .toList();

        // Combine results in priority order:
        // 1. Exact matches
        // 2. Following matches
        // 3. Follower matches
        // 4. Other matches
        List<AppUser> rankedResults = new ArrayList<>();
        rankedResults.addAll(exactMatch);
        rankedResults.addAll(followingMatches);
        rankedResults.addAll(followerMatches);
        rankedResults.addAll(otherMatches);

        // Return the ranked results in the response
        return ResponseEntity.status(HttpStatus.OK).body(rankedResults);
    }

    public ResponseEntity<?> searchPost(String query, int page, int size) {
        if (query.length() < 3) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter at least 3 characters");
        }

        //Create a pageable Object
        Pageable pageable = PageRequest.of(page, size);

        // Fetch paginated results
        Page<Post> postPage = postRepository.findByCaptionIgnoreCaseContaining(query, pageable);

        if (postPage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
        }



        //creating response of post
        List<PostResponseDto> allPost = postPage.stream().map( post -> {
            PostResponseDto response = new PostResponseDto();
            AppUserDto userResponse = new AppUserDto();

            response.setId(post.getId());
            response.setCaption(post.getCaption());
            response.setPostType(post.getPostType());
            response.setPostContentUrl(post.getPostContent());
            response.setCreatedAt(post.getCreatedAt());
            response.setUpdatedAt(post.getUpdatedAt());

            //we have to convert user to user dto
            userResponse.setId(post.getUser().getId());
            userResponse.setUsername(post.getUser().getUsername());
            userResponse.setBio(post.getUser().getBio());
            userResponse.setGender(post.getUser().getGender());
            userResponse.setProfilePicture(post.getUser().getProfilePicture());

            response.setUser(userResponse);

            return response;
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allPost);

    }

    public ResponseEntity<?> createTimeline(int page, int size, AppUser userDetails) {

        //step 1 find all the user which is followed by current user.
        List<Follow> follows = followRepository.findByFollowerAndStatus(userDetails, FollowStatus.ACCEPTED);
        // list of follows have all information like following and followers, but we only interested in following user because we know follower is current user
        List<AppUser> followedUsers = follows.stream()
                .map(follow -> follow.getFollowing())
                .toList();


        //step 2 fetch the posts from followed user
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Page<Post> followedPosts = postRepository.findByUserIn(followedUsers, pageable);



        // Step 3: Convert followed posts to PostResponseDto
        List<PostResponseDto> allPost = followedPosts.stream().map(post -> {
            PostResponseDto postInfo = new PostResponseDto();

            // Set the basic fields of the post
            postInfo.setId(post.getId());
            postInfo.setCaption(post.getCaption());
            postInfo.setPostType(post.getPostType());
            postInfo.setCreatedAt(post.getCreatedAt());
            postInfo.setPostContentUrl(post.getPostContent());
            postInfo.setUpdatedAt(post.getUpdatedAt());

            // Map the user details to UserDto
            AppUserDto userInfo = new AppUserDto();
            userInfo.setId(post.getUser().getId());
            userInfo.setUsername(post.getUser().getUsername());
            userInfo.setBio(post.getUser().getBio());
            userInfo.setDisplayName(post.getUser().getDisplayName());
            userInfo.setRole(post.getUser().getRole());
            userInfo.setGender(post.getUser().getGender());
            userInfo.setProfilePicture(post.getUser().getProfilePicture());
            postInfo.setUser(userInfo);

            // Map the tagged users
           if (postInfo.getTaggedUser() != null){
               Set<AppUserDto> taggedUsers = postInfo.getTaggedUser().stream().map(taggedUser -> {
                   AppUserDto taggedUserDto = new AppUserDto();
                   taggedUserDto.setId(taggedUser.getId());
                   taggedUserDto.setUsername(taggedUser.getUsername());
                   taggedUserDto.setBio(taggedUser.getBio());
                   taggedUserDto.setGender(taggedUser.getGender());
                   taggedUserDto.setProfilePicture(taggedUser.getProfilePicture());
                   return taggedUserDto;
               }).collect(Collectors.toSet());
               postInfo.setTaggedUser(taggedUsers);
           }else{
               postInfo.setTaggedUser(Collections.emptySet());
           }
            return postInfo;
        }).toList();

        if (allPost.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("please follow more user ");
        }
        return ResponseEntity.status(HttpStatus.OK).body(allPost);
    }

    public ResponseEntity<?> createReelTimeLine(int page, int size, AppUser userDetails) {

        if (page < 0 || size <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Page and size must be positive values.");
        }

        // Step 1: Fetch public reels sorted by creation date
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Post> fetchedReels = postRepository.findByPostTypeAndUser_AccountType(PostType.REEL, AccountType.PUBLIC, pageable);

        // Step 2: Convert reels to PostResponseDto
        List<PostResponseDto> allReels = fetchedReels.stream().map(post -> {
            PostResponseDto reelInfo = new PostResponseDto();

            // Set the basic fields of the post (reel)
            reelInfo.setId(post.getId());
            reelInfo.setCaption(post.getCaption());
            reelInfo.setPostType(post.getPostType());
            reelInfo.setCreatedAt(post.getCreatedAt());
            reelInfo.setPostContentUrl(post.getPostContent());
            reelInfo.setUpdatedAt(post.getUpdatedAt());

            // Map the user details to UserDto
            AppUserDto userInfo = new AppUserDto();
            userInfo.setId(post.getUser().getId());
            userInfo.setUsername(post.getUser().getUsername());
            userInfo.setBio(post.getUser().getBio());
            userInfo.setDisplayName(post.getUser().getDisplayName());
            userInfo.setRole(post.getUser().getRole());
            userInfo.setGender(post.getUser().getGender());
            userInfo.setProfilePicture(post.getUser().getProfilePicture());
            reelInfo.setUser(userInfo);

            // Map the tagged users
            if (post.getTaggedUSer() != null && !post.getTaggedUSer().isEmpty()) {
                Set<AppUserDto> taggedUsers = post.getTaggedUSer().stream().map(taggedUser -> {
                    AppUserDto taggedUserDto = new AppUserDto();
                    taggedUserDto.setId(taggedUser.getId());
                    taggedUserDto.setUsername(taggedUser.getUsername());
                    taggedUserDto.setBio(taggedUser.getBio());
                    taggedUserDto.setGender(taggedUser.getGender());
                    taggedUserDto.setProfilePicture(taggedUser.getProfilePicture());
                    return taggedUserDto;
                }).collect(Collectors.toSet());
                reelInfo.setTaggedUser(taggedUsers);
            } else {
                reelInfo.setTaggedUser(Collections.emptySet());
            }
            return reelInfo;
        }).toList();

        // Step 3: Return response
        if (allReels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body("No reels available at the moment.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(allReels);
    }

}
























































































































//import React, { useState, useEffect } from "react";
//        import axios from "axios";
//
//        const InfiniteScrollPosts = ({ query }) => {
//        const [posts, setPosts] = useState([]);
//  const [page, setPage] = useState(0);
//  const [hasMore, setHasMore] = useState(true);
//  const [loading, setLoading] = useState(false);
//
//  const fetchPosts = async () => {
//        if (loading || !hasMore) return;
//
//setLoading(true);
//
//    try {
//            const response = await axios.get("/api/search/posts", {
//    params: { query, page, size: 10 },
//});
//
//        const newPosts = response.data.content;
//
//setPosts((prevPosts) => [...prevPosts, ...newPosts]);
//setHasMore(!response.data.last); // Update hasMore based on 'last' in response
//    } catch (error) {
//        console.error("Error fetching posts:", error);
//    } finally {
//setLoading(false);
//    }
//            };
//
//            const handleScroll = () => {
//        if (
//window.innerHeight + document.documentElement.scrollTop >=
//document.documentElement.offsetHeight - 100
//        ) {
//setPage((prevPage) => prevPage + 1);
//        }
//        };
//
//useEffect(() => {
//fetchPosts();
//// eslint-disable-next-line react-hooks/exhaustive-deps
//  }, [page]);
//
//useEffect(() => {
//        window.addEventListener("scroll", handleScroll);
//    return () => window.removeEventListener("scroll", handleScroll);
//  }, []);
//
//          return (
//<div>
//{posts.map((post) => (
//        <div key={post.id} className="post">
//          <h3>{post.caption}</h3>
//          <p>Created at: {new Date(post.createdAt).toLocaleString()}</p>
//        </div>
//      ))}
//
//      {loading && <p>Loading...</p>}
//        {!hasMore && <p>No more posts to load.</p>}
//    </div>
//        );
//        };
//
//export default InfiniteScrollPosts;
//


