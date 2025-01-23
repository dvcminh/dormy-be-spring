package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.CommentRequestDto;
import com.minhvu.monolithic.dto.ReplyDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.repository.CommentRepository;
import com.minhvu.monolithic.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;


    public ResponseEntity<String> addComment(CommentRequestDto commentDetails, AppUser userDetails) {
        if (userDetails.getId() == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You can not add comment");
        }

        UUID postId = commentDetails.getPostId();
        //finding post

        Optional<Post> existingPost = postRepository.findById(postId);
        if(existingPost.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found in which you want to add this comment");
        }

        //checking validations
        String commentBody = commentDetails.getText();
        if (commentBody.isEmpty()|| commentBody.length() > 400){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please add comment between 1 to 500 character");
        }

        // creating a comment object
        Comment comment = new Comment();
        comment.setText(commentBody);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(existingPost.get());
        comment.setUser(userDetails);
        comment.setParentComment(null);

        try{
            commentRepository.save(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong please try again latter");
        }

       return ResponseEntity.status(HttpStatus.CREATED).body("comment added");
    }

    public ResponseEntity<String> addReply(UUID commentId, CommentRequestDto commentDetails, AppUser userDetails) {
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to reply");
        }

        UUID existingPostId = commentDetails.getPostId();
        Optional<Post> post = postRepository.findById(existingPostId);
        if(post.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("post not found in which you want to reply the comment");
        }

        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if(existingComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found which you want to reply");
        }

        String commentBody = commentDetails.getText();
        if (commentBody.isEmpty()|| commentBody.length() > 400){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please add comment between 1 to 500 character");
        }


        // creating a comment object
        Comment comment = new Comment();
        comment.setText(commentBody);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setPost(post.get());
        comment.setUser(userDetails);
        comment.setParentComment(existingComment.get());


        try {
            commentRepository.save(comment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong please try again latter");
        }


        return  ResponseEntity.status(HttpStatus.CREATED).body("Your reply added successfully");



    }

    public ResponseEntity<?> getAllReplyOfPost(UUID commentId, AppUser userDetails) {
        if(userDetails == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized ");
        }

        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if(existingComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found ");
        }

        List<Comment> allReplyComments = commentRepository.findAllByParentComment(existingComment.get());


        if (allReplyComments.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body("No replies found");
        }
        List<ReplyDto> allReplies  = allReplyComments.stream().map(comment -> {
            ReplyDto replies = new ReplyDto();
             replies.setId(comment.getId());
             replies.setText(comment.getText());
             replies.setCreatedAt(comment.getCreatedAt());
             replies.setUserId(comment.getUser().getId());
             replies.setUserName(comment.getUser().getUsername());
             replies.setUserprofilePicture(comment.getUser().getProfilePicture());
             replies.setParentCommentId(comment.getParentComment().getId());
             return  replies;
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allReplies);
    }

    public ResponseEntity<?> getNoOfReplies(UUID commentId) {
        Optional<Comment> parentComment = commentRepository.findById(commentId);
        if (parentComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }

        List<Comment> allReplies = commentRepository.findAllByParentComment(parentComment.get());
        Integer numberOfReplies = allReplies.size();

        return ResponseEntity.status(HttpStatus.OK).body(numberOfReplies);
    }

    public ResponseEntity<?> getAllComments(UUID postId) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        List<Comment> allParentComment = commentRepository.findAllByParentCommentAndPost(null, existingPost.get());


        List<ReplyDto> allReplies  = allParentComment.stream().map(comment -> {
            ReplyDto replies = new ReplyDto();
            replies.setId(comment.getId());
            replies.setText(comment.getText());
            replies.setCreatedAt(comment.getCreatedAt());
            replies.setUserId(comment.getUser().getId());
            replies.setUserName(comment.getUser().getDisplayName());
            replies.setUserprofilePicture(comment.getUser().getProfilePicture());
            replies.setParentCommentId(null);
            return  replies;
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allReplies);
    }

    public ResponseEntity<Integer> getTotalNumberOfCommets(UUID postId) {
        Optional<Post> existingPost = postRepository.findById(postId);
        if (existingPost.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(0);
        }
        Integer totalComments = commentRepository.findAllByParentCommentAndPost(null, existingPost.get()).size();
      return  ResponseEntity.status(HttpStatus.OK).body(totalComments);

    }

    public ResponseEntity<String> editComment(UUID commentId, AppUser userDetails, String text) {
        if (text == null || text.trim().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Text cannot be null or empty");
        }

        if (text.isEmpty()|| text.length() > 400){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("please add comment between 1 to 400 character");
        }

        Optional<Comment> existingComment = commentRepository.findById(commentId);
        if (existingComment.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");
        }

        //verifying user  means the user can only edit comment who made this comment
        if (!existingComment.get().getUser().getId().equals(userDetails.getId())){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to edit this comment");
        }

        existingComment.get().setText(text);

        try{
            commentRepository.save(existingComment.get());
        }catch (Exception e){
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong, please try again latter");
        }

        return  ResponseEntity.status(HttpStatus.OK).body("Comment update successfully");

    }

    public ResponseEntity<String> deleteComment(UUID commentId, AppUser userDetails) {
        Optional<Comment> comment = commentRepository.findById(commentId);

        if (comment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }

        // Check if the user is authorized to delete the comment
        if (!comment.get().getUser().getId().equals(userDetails.getId())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("You are not authorized to delete this comment");
        }

        try {
            commentRepository.delete(comment.get());
            return ResponseEntity.status(HttpStatus.OK).body("Comment deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete the comment");
        }
    }
}

//note corrrect the get all reply
