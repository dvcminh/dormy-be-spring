package com.minhvu.review.controller;



import com.minhvu.review.dto.*;
import com.minhvu.review.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Slf4j
@CrossOrigin("*")
public class PostController {

    private final PostService postService;
    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestHeader("id") String userId ,@ModelAttribute PostRequest postRequest) {

        return ResponseEntity.ok(postService.createPost(getUserId(userId),postRequest));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(@RequestHeader("id") String userId, @PathVariable("postId") Long postId, @ModelAttribute PostUpdateRequest postUpdateRequest) {
        log.info("update post {} ", postUpdateRequest);

        return ResponseEntity.ok(postService.updatePost(getUserId(userId), postId, postUpdateRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@RequestHeader("id") String userId, @PathVariable("postId") Long postId) {
        postService.deletePost(getUserId(userId), postId);
        return ResponseEntity.noContent().build();
    }


    private Long getUserId(String userId) {
        return Long.parseLong(userId);
    }
    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PostWithInteractionResponse>> getPost(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(postService.getAllPost(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostWithInteractionResponse>> getPostByUserId(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(postService.getAllPostByUserId(userId));
    }





}
