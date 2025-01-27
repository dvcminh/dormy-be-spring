package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.CreatePostRequestDto;
import com.minhvu.monolithic.dto.PostResponseDto;
import com.minhvu.monolithic.dto.UpdatePostRequestDto;
import com.minhvu.monolithic.dto.mapper.AppUserMapper;
import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.repository.PostRepository;
import com.minhvu.monolithic.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AppUserMapper appUserMapper;
//    @Autowired
//    CloudinaryService cloudinaryService;

    public ResponseEntity<String> addPost(@Valid CreatePostRequestDto postDetails, AppUser userPrinciple) {
        try {
            // Create Post entity
            Post post = new Post();
            post.setCaption(postDetails.getCaption());
            post.setPostType(postDetails.getPostType());
            post.setCreatedAt(LocalDateTime.now());
            post.setPostContent(postDetails.getPostContentFileUrl());
            post.setUser(userPrinciple);


            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.CREATED).body("Post added successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong while saving the post. Please try again later.");
        }
    }

    public ResponseEntity<String> updateCaption(UpdatePostRequestDto updatePostRequestDto, AppUser userPrinciple) {
        UUID currentUserId = userPrinciple.getId();

        Optional<Post> currentPost = postRepository.findById(updatePostRequestDto.getPostId());

        if (updatePostRequestDto.getCaption().length() > 200) {
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

        post.setCaption(updatePostRequestDto.getCaption());
        post.setPostType(updatePostRequestDto.getPostType());
        post.setPostContent(updatePostRequestDto.getPostContentFileUrl());

        try {
            postRepository.save(post);
            return ResponseEntity.status(HttpStatus.OK).body("Post updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("something went wrong, please try agan latter");
        }

    }

    public ResponseEntity<String> deletePost(UUID postId, AppUser userPrinciple) {
        Optional<Post> currentPost = postRepository.findById(postId);

        if (currentPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Post post = currentPost.get();

        UUID loginUserId = userPrinciple.getId();
        if (!loginUserId.equals(post.getUser().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this post");
        }

        try {
            postRepository.delete(post);
            return ResponseEntity.status(HttpStatus.OK).body("Post deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).
                    body("something went wrong , please try again latter");
        }
    }

    public ResponseEntity<?> getSinglePost(UUID postId) {
        Optional<Post> currentPost = postRepository.findById(postId);

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
        AppUserDto userDto = new AppUserDto();
        userDto.setId(post.getUser().getId());
        userDto.setUsername(post.getUser().getUsername());
        userDto.setDisplayName(post.getUser().getDisplayName());
        userDto.setProfilePicture(post.getUser().getProfilePicture());
        userDto.setBio(post.getUser().getBio());
        userDto.setGender(post.getUser().getGender());

        postResponseDto.setUser(userDto);


        //handling tagged user
        Set<AppUserDto> taggedUsersDto = post.getTaggedUSer()
                .stream()
                .map(taggedUser -> {
                    AppUserDto taggedUserDto = new AppUserDto();
                    taggedUserDto.setId(taggedUser.getId());
                    taggedUserDto.setUsername(taggedUser.getUsername());
                    taggedUserDto.setProfilePicture(taggedUser.getProfilePicture());
                    taggedUserDto.setDisplayName(taggedUser.getDisplayName());
                    taggedUserDto.setBio(taggedUser.getBio());
                    taggedUserDto.setGender(taggedUser.getGender());
                    return taggedUserDto;
                })
                .collect(Collectors.toSet());
        postResponseDto.setTaggedUser(taggedUsersDto);

        return ResponseEntity.status(HttpStatus.OK).body(postResponseDto);
    }

    public ResponseEntity<?> getAllPost(UUID userId) {

        Optional<AppUser> userDetails = userRepository.findById(userId);

        if (userDetails.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        AppUser user = userDetails.get();

        try {
            // Finding all posts related to a user
            List<Post> allPost = postRepository.findAllByUser(user);

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
                AppUserDto userDto = new AppUserDto();
                userDto.setId(post.getUser().getId());
                userDto.setUsername(post.getUser().getUsername());
                userDto.setProfilePicture(post.getUser().getProfilePicture());
                userDto.setDisplayName(post.getUser().getDisplayName());
                userDto.setBio(post.getUser().getBio());
                userDto.setGender(post.getUser().getGender());
                postResponseDto.setUser(userDto);

                // Handling Tagged Users
                Set<AppUserDto> taggedUsersDto = post.getTaggedUSer()
                        .stream()
                        .map(appUserMapper::toDto)
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
