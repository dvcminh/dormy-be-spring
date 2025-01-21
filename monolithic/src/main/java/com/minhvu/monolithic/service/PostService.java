package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.PostRequestDto;
import com.minhvu.monolithic.dto.PostResponseDto;
import com.minhvu.monolithic.dto.UserDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.repository.IPost;
import com.minhvu.monolithic.repository.IUser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    IPost iPost;

    @Autowired
    IUser iUser;

//    @Autowired
//    CloudinaryService cloudinaryService;

    public ResponseEntity<String> addPost(@Valid PostRequestDto postDetails, AppUser userPrinciple) {
        try {
            // Create Post entity
            Post post = new Post();
            post.setCaption(postDetails.getCaption());
            post.setPostType(postDetails.getPostType());
            post.setCreatedAt(LocalDateTime.now());
            post.setPostContent(postDetails.getPostContentFileUrl());
            post.setUser(userPrinciple);


            iPost.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while saving the post. Please try again later.");
        }
    }

    public ResponseEntity<String> updateCaption(String caption, AppUser userPrinciple, UUID postId) {
        UUID currentUserId = userPrinciple.getId();


        Optional<Post> currentPost = iPost.findById(postId);

        if (caption != null && caption.length() > 200) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).
                    body("Caption should be less than 200 characters.");
        }


        if (currentPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = currentPost.get();

        if (!post.getUser().getId().equals(currentUserId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to update post");
        }

        post.setCaption(caption);

        try {
            iPost.save(post);
            return ResponseEntity.status(HttpStatus.OK).body("Post updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("something went wrong, please try agan latter");

        }

    }

    public ResponseEntity<String> deletePost(UUID postId, AppUser userPrinciple) {
        Optional<Post> currentPost = iPost.findById(postId);

        if (currentPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = currentPost.get();

        UUID loginUserId = userPrinciple.getId();
        if (!loginUserId.equals(post.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post");
        }

        try {
            iPost.delete(post);
            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("something went wrong , please try again latter");
        }
    }

    public ResponseEntity<?> getSinglePost(UUID postId) {
        Optional<Post> currentPost = iPost.findById(postId);

        if (currentPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = currentPost.get();

        //creating post response sto
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(post.getId());
        postResponseDto.setCaption(post.getCaption());
        postResponseDto.setPostContentUrl(post.getPostContent());
        postResponseDto.setCreatedAt(post.getCreatedAt());
        postResponseDto.setUpdatedAt(post.getUpdatedAt());
        postResponseDto.setPostType(post.getPostType());

        //setting user dto
        UserDto userDto = new UserDto();
        userDto.setId(post.getUser().getId());
        userDto.setUserName(post.getUser().getUsername());
        userDto.setFullName(post.getUser().getDisplayName());
        userDto.setBio(post.getUser().getBio());
        userDto.setGender(post.getUser().getGender());

        postResponseDto.setUser(userDto);


        //handling tagged user
        Set<UserDto> taggedUsersDto = post.getTaggedUSer()
                .stream()
                .map(taggedUser -> {
                    UserDto taggedUserDto = new UserDto();
                    taggedUserDto.setId(taggedUser.getId());
                    taggedUserDto.setUserName(taggedUser.getUsername());
                    taggedUserDto.setFullName(taggedUser.getDisplayName());
                    taggedUserDto.setBio(taggedUser.getBio());
                    taggedUserDto.setGender(taggedUser.getGender());
                    return taggedUserDto;
                })
                .collect(Collectors.toSet());
        postResponseDto.setTaggedUser(taggedUsersDto);

        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    public ResponseEntity<?> getAllPost(UUID userId) {

        Optional<AppUser> userDetails = iUser.findById(userId);

        if (userDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        AppUser user = userDetails.get();

        try {
            // Finding all posts related to a user
            List<Post> allPost = iPost.findAllByUser(user);

            if (allPost == null || allPost.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
            }

            // Converting List<Post> to List<PostResponseDto>
            List<PostResponseDto> allPostResponse = allPost.stream().map(post -> {
                PostResponseDto postResponseDto = new PostResponseDto();
                postResponseDto.setId(post.getId());
                postResponseDto.setCaption(post.getCaption());
                postResponseDto.setPostContentUrl(post.getPostContent());
                postResponseDto.setCreatedAt(post.getCreatedAt());
                postResponseDto.setUpdatedAt(post.getUpdatedAt());

                // Setting UserDto for the post
                UserDto userDto = new UserDto();
                userDto.setId(post.getUser().getId());
                userDto.setUserName(post.getUser().getUsername());
                userDto.setFullName(post.getUser().getDisplayName());
                userDto.setBio(post.getUser().getBio());
                userDto.setGender(post.getUser().getGender());
                postResponseDto.setUser(userDto);

                // Handling Tagged Users
                Set<UserDto> taggedUsersDto = post.getTaggedUSer()
                        .stream()
                        .map(taggedUser -> {
                            UserDto taggedUserDto = new UserDto();
                            taggedUserDto.setId(taggedUser.getId());
                            taggedUserDto.setUserName(taggedUser.getUsername());
                            taggedUserDto.setFullName(taggedUser.getDisplayName());
                            taggedUserDto.setBio(taggedUser.getBio());
                            taggedUserDto.setGender(taggedUser.getGender());
                            return taggedUserDto;
                        })
                        .collect(Collectors.toSet());
                postResponseDto.setTaggedUser(taggedUsersDto);

                return postResponseDto;
            }).collect(Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(allPostResponse);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong. Please try again later.");
        }
    }

}
