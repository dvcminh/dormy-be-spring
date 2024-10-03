package com.minhvu.review.controller;


import com.minhvu.review.dto.PostEntityDto;
import com.minhvu.review.dto.request.PostRequest;
import com.minhvu.review.dto.PostResponse;
import com.minhvu.review.dto.request.PostUpdateRequest;
import com.minhvu.review.dto.AppUserDto;
import com.minhvu.review.dto.mapper.PostMapper;
import com.minhvu.review.dto.response.Response;
import com.minhvu.review.kafka.PostProducer;
import com.minhvu.review.repository.PostRepository;
import com.minhvu.review.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
@Slf4j
@CrossOrigin("*")
public class PostController extends BaseController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final PostProducer producer;
    private final PostMapper postMapper;

    @PostMapping()
    public ResponseEntity<PostEntityDto> createPost(@RequestBody PostRequest postRequest, HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        return ResponseEntity.ok(postService.createPost(user.getId(), postRequest));
    }

    @DeleteMapping("{postId}")
    @Transactional
    public ResponseEntity<Response> deleteById(
            @PathVariable("postId") UUID postId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        postService.delete(postId, currentUser);
        return ResponseEntity.ok(
                new Response(String.format("Bill with id [%s] move to trash bin", postId))
        );
    }

    @PutMapping("{postId}/restore")
    @Transactional
    public ResponseEntity<Response> restorePostById(
            @PathVariable UUID postId,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        postService.restore(postId, currentUser);
        return ResponseEntity.ok(
                new Response(String.format("Bill with id [%s] restore from trash bin", postId))
        );
    }

    @PutMapping("{postId}")
    @Transactional
    public ResponseEntity<Response> updatePostById(
            @PathVariable UUID postId,
            @RequestBody PostUpdateRequest postUpdateRequest,
            HttpServletRequest request
    ) {
        AppUserDto currentUser = getCurrentUser(request);
        postService.update(postId, postUpdateRequest, currentUser);
        return ResponseEntity.ok(
                new Response(String.format("Bill with id [%s] updated", postId))
        );
    }


}
