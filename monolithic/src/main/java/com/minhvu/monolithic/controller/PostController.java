package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.PostRequestDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/api/v1")
@RestController
public class PostController extends BaseController {
    @Autowired
    PostService postService;

    //api to create post
    @PostMapping(value = "/post")
    private ResponseEntity<String> addPost(@RequestBody PostRequestDto postDetails) {
        AppUser userPrinciple = getCurrentUser();
        return postService.addPost(postDetails, userPrinciple);
    }

    //api to update post caption
    @PutMapping("/post/{postId}/{caption}")
    private ResponseEntity<String> updateCaption(@PathVariable String caption,
                                                 @PathVariable UUID postId) {
        AppUser userPrinciple = getCurrentUser();
        return postService.updateCaption(caption, userPrinciple, postId);
    }


    //api to delete post
    @DeleteMapping("/post/{postId}")
    private ResponseEntity<String> deletePost(@PathVariable UUID postId) {
        AppUser userPrinciple = getCurrentUser();
        return postService.deletePost(postId, userPrinciple);
    }


    //api to get single post
    @GetMapping("/post/{postId}")
    private ResponseEntity<?> getSinglePost(@PathVariable UUID postId) {
        return postService.getSinglePost(postId);
    }


    //api to get all post of single user
    @GetMapping("/posts/{userId}")
    private ResponseEntity<?> getAllPost(@PathVariable UUID userId) {
        return postService.getAllPost(userId);
    }
}
