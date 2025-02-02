package com.minhvu.monolithic.service;


import com.minhvu.monolithic.dto.model.LikeDto;
import com.minhvu.monolithic.dto.model.NotificationDto;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Comment;
import com.minhvu.monolithic.entity.Like;
import com.minhvu.monolithic.entity.Post;
import com.minhvu.monolithic.enums.LikeType;
import com.minhvu.monolithic.repository.CommentRepository;
import com.minhvu.monolithic.repository.LikeRepository;
import com.minhvu.monolithic.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final NotificationService notificationService;

    public ResponseEntity<Boolean> checkIfUserLikedPost(UUID postId, AppUser userDetails) {
        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        }

        Optional<Like> existingLike = likeRepository.findByPostAndUser(existingPost.get(), userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(existingLike.isPresent());
    }

    public ResponseEntity<String> addPostLike(UUID postId, AppUser userDetails) {
        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        Optional<Like> existingLike = likeRepository.findByPostAndUser(existingPost.get(), userDetails);

        try {
            if (existingLike.isEmpty()) {
                // Add like
                Like like = new Like();
                like.setLikeType(LikeType.POST);
                like.setUser(userDetails);
                like.setPost(existingPost.get());
                likeRepository.saveAndFlush(like);
                NotificationDto notificationDto = notificationService.generateNotification(existingPost.get().getUser().getId(),
                        "New post like",
                        "Some one just like your post",
                        "like",
                        existingPost.get().getId(),
                        userDetails.getId());
                notificationService.saveNotification(notificationDto);
                return ResponseEntity.status(HttpStatus.OK).body("Post liked successfully");
            } else {
                // Remove like
                likeRepository.delete(existingLike.get());
                return ResponseEntity.status(HttpStatus.OK).body("Post unliked successfully");
            }
        } catch (Exception e) {
            // Log the error for debugging (optional)
            // logger.error("Error toggling like for postId {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong, please try again later");
        }
    }



    
    public ResponseEntity<String> likeComment(UUID commentId, AppUser userDetails) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        if(existingComment.isEmpty()){
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comment not found");
        }

        Optional<Like> existingLike = likeRepository.findByCommentAndUser(existingComment.get(), userDetails);

        try {
            if (existingLike.isEmpty()) {
                // Add like
                Like like = new Like();
                like.setLikeType(LikeType.COMMENT);
                like.setUser(userDetails);
                like.setComment(existingComment.get());
                likeRepository.save(like);
                return ResponseEntity.status(HttpStatus.OK).body("comment liked successfully");
            } else {
                // Remove like
                likeRepository.delete(existingLike.get());
                return ResponseEntity.status(HttpStatus.OK).body("comment unliked successfully");
            }
        } catch (Exception e) {
            // Log the error for debugging (optional)
            // logger.error("Error toggling like for postId {}: {}", postId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Something went wrong, please try again later");
        }
    }


    public ResponseEntity<?> postLikeDetails(UUID postId) {
        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        List<Like> allLikes = likeRepository.findByPost(existingPost.get());

        if (allLikes.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(Collections.emptyList());
        }

        //mapping to like dto
        List<LikeDto> allLikesDetails = allLikes.stream().map( like -> {
            LikeDto likeRecord = new LikeDto();
            likeRecord.setId(like.getId());
            likeRecord.setUserName(like.getUser().getUsername());
            likeRecord.setUserId(like.getUser().getId());
            likeRecord.setUserProfilePicture(like.getUser().getProfilePicture());
            return likeRecord;
        }).toList();

        return ResponseEntity.status(HttpStatus.OK).body(allLikesDetails);
    }

    public ResponseEntity<?> postLikeCount(UUID postId) {
        Optional<Post> existingPost = postRepository.findById(postId);

        if (existingPost.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }

        List<Like> allLikes = likeRepository.findByPost(existingPost.get());

        if (allLikes.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(allLikes.size());
    }

    public ResponseEntity<?> commentLikeCount(UUID commentId) {
        Optional<Comment> existingComment = commentRepository.findById(commentId);

        if (existingComment.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("comment not found");
        }

        List<Like> allLikes = likeRepository.findByComment(existingComment.get());

        if (allLikes.isEmpty()){
            return ResponseEntity.status(HttpStatus.OK).body(0);
        }
        return ResponseEntity.status(HttpStatus.OK).body(allLikes.size());
    }
}
