package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.request.CreatePostRequestDto;
import com.minhvu.monolithic.dto.response.UpdatePostRequestDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RequestMapping("/api/v1/post")
@RestController
public class PostController extends BaseController {
    @Autowired
    PostService postService;

    //api to create post
    @PostMapping
    private ResponseEntity<String> addPost(@RequestBody CreatePostRequestDto postDetails) {
        AppUser userPrinciple = getCurrentUser();
        return postService.addPost(postDetails, userPrinciple);
    }

    //api to update post caption
    @PutMapping
    private ResponseEntity<String> updateCaption(@RequestBody UpdatePostRequestDto updatePostRequestDto) {
        AppUser userPrinciple = getCurrentUser();
        return postService.updateCaption(updatePostRequestDto, userPrinciple);
    }


    //api to delete post
    @DeleteMapping("/{postId}")
    private ResponseEntity<String> deletePost(@PathVariable UUID postId) {
        AppUser userPrinciple = getCurrentUser();
        return postService.deletePost(postId, userPrinciple);
    }


    //api to get single post
    @GetMapping("/{postId}")
    private ResponseEntity<?> getSinglePost(@PathVariable UUID postId) {
        return postService.getSinglePost(postId);
    }


    //api to get all post of single user
    @GetMapping("/posts/{userId}")
    private ResponseEntity<?> getAllPost(@PathVariable UUID userId) {
        return postService.getAllPost(userId);
    }

    //api to get all post of me
    @GetMapping
    private ResponseEntity<?> getAllPost() {
        AppUser userPrinciple = getCurrentUser();
        return postService.getAllPost(userPrinciple.getId());
    }

    //api to see number of user's post
    @GetMapping("/count/{userId}")
    private ResponseEntity<Integer> countPost(@PathVariable UUID userId) {
        return postService.countPost(userId);
    }
}
