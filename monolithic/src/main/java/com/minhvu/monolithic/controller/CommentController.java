package com.minhvu.monolithic.controller;


import com.minhvu.monolithic.dto.CommentRequestDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("/api/v1/post")
@RestController
public class CommentController extends BaseController{
    @Autowired
    private CommentService commentService;


    //api to add a comment (parent comment)
    @PostMapping("/comment")
    private ResponseEntity<String> addComment(@RequestBody CommentRequestDto comment) {
        AppUser userDetails = getCurrentUser();
        return commentService.addComment(comment, userDetails);
    }


    //api to reply a comment
    @PostMapping("/{commentId}/reply")
    private ResponseEntity<String> addReply(@PathVariable UUID commentId,
                                            @RequestBody CommentRequestDto comment) {
        AppUser userDetails = getCurrentUser();
        return commentService.addReply(commentId, comment, userDetails);
    }

    //api to get all replies of comment
    @GetMapping("/{commentId}/reply")
    private ResponseEntity<?> commentReply(
            @PathVariable UUID commentId
    ) {
        AppUser userDetails = getCurrentUser();
        return commentService.getAllReplyOfPost(commentId, userDetails);
    }

    // api to get count of all replies of a comment
    @GetMapping("/comment/{commentId}")
    private ResponseEntity<?> getNoOfReplies(@PathVariable UUID commentId) {
        return commentService.getNoOfReplies(commentId);
    }

    //api to get all comment of a post
    @GetMapping("/all-comments/{postId}")
    private ResponseEntity<?> getAllComments(@PathVariable UUID postId) {
        return commentService.getAllComments(postId);
    }

    //api to get count of all comment of a post
    @GetMapping("/all-comment/{postId}")
    private ResponseEntity<Integer> getAllComment(@PathVariable UUID postId) {
        return commentService.getTotalNumberOfCommets(postId);
    }


    //api to delete a comment
    //api to delete a reply
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable UUID commentId) {
        AppUser userDetails = getCurrentUser();
        return  commentService.deleteComment(commentId,userDetails);
    }

    //api to edit a comment
    @PutMapping("/comment/{commentId}")
    private ResponseEntity<String> editComment(@PathVariable UUID commentId,
                                               @RequestBody String text) {
        AppUser userDetails = getCurrentUser();
        return  commentService.editComment(commentId,userDetails,text);
    }



}
