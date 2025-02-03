package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.SavedPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/saved-posts")
@RequiredArgsConstructor
public class SavedPostController extends BaseController {

    private final SavedPostService savedPostService;

    @PostMapping("/{postId}")
    public ResponseEntity<String> savePost(@PathVariable UUID postId) {
        AppUser user = getCurrentUser();
        savedPostService.savePost(user, postId);
        return ResponseEntity.ok("Post saved successfully");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> unsavePost(@PathVariable UUID postId) {
        AppUser user = getCurrentUser();
        savedPostService.unsavePost(user, postId);
        return ResponseEntity.ok("Post unsaved successfully");
    }

    @GetMapping
    public ResponseEntity<List<?>> getSavedPosts() {
        AppUser user = getCurrentUser();
        return ResponseEntity.ok(savedPostService.getSavedPosts(user));
    }
}
