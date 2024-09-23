package com.minhvu.review.controller;


import com.minhvu.review.dto.*;
import com.minhvu.review.dto.inter.AppUserDto;
import com.minhvu.review.dto.mapper.PostMapper;
import com.minhvu.review.kafka.PostProducer;
import com.minhvu.review.repository.PostRepository;
import com.minhvu.review.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PostResponse> createPost(
            @RequestParam("content") String content,
            @RequestParam("mediaFiles") List<MultipartFile> mediaFiles,
            HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        PostRequest postRequest = PostRequest.builder()
                .body(content)
                .multipartFiles(mediaFiles)
                .build();
        return ResponseEntity.ok(postService.createPost(user.getId(), postRequest));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(HttpServletRequest request, @PathVariable("postId") UUID postId, @ModelAttribute PostUpdateRequest postUpdateRequest) {
        log.info("update post {} ", postUpdateRequest);
        AppUserDto user = getCurrentUser(request);
        return ResponseEntity.ok(postService.updatePost(user.getId(), postId, postUpdateRequest));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(HttpServletRequest request, @PathVariable("postId") UUID postId) {
        UUID userId = getCurrentUser(request).getId();
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all/{userId}")
    public ResponseEntity<List<PostWithInteractionResponse>> getPost(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(postService.getAllPost(userId));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<PostWithInteractionResponse>> getPostByUserId(@PathVariable("userId") UUID userId) {
        return ResponseEntity.ok(postService.getAllPostByUserId(userId));
    }

    @GetMapping("/sync")
    public ResponseEntity<String> syncPost(HttpServletRequest request) {
        postRepository.findAll().forEach(post -> {
            producer.send(postMapper.toDto(post));
        });
        return ResponseEntity.ok("Sync post successfully");
    }


}
