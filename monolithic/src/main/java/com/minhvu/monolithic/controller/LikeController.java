package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class LikeController extends BaseController {
    @Autowired
    LikeService likeService;
    //api to like a post
    @PostMapping("/post/like/{postId}")
    private ResponseEntity<String> likePost(@PathVariable UUID postId){
        AppUser userDetails = getCurrentUser();
        return  likeService.addPostLike(postId,userDetails);
    }

    //api to like a comment
    @PostMapping("/comment/like/{commentId}")
    private ResponseEntity<String> likeComment(@PathVariable UUID commentId){
        AppUser userDetails = getCurrentUser();
        return likeService.likeComment(commentId,userDetails);
    }


    //api to get Details of all like of post
    @GetMapping("/like/details/{postId}")
    private ResponseEntity<?> postLikeDetails (@PathVariable UUID postId){
        return likeService.postLikeDetails(postId);
    }

    // api to get all likes of a post
    @GetMapping("/like/count/{postId}")
    private ResponseEntity<?> postLikeCount (@PathVariable UUID postId){
        return likeService.postLikeCount(postId);
    }

    //api to get count of all like of a comment
    @GetMapping("/like/comment/count/{commentId}")
    private ResponseEntity<?> commentLikeCount (@PathVariable UUID commentId){
        return likeService.commentLikeCount(commentId);
    }

}








