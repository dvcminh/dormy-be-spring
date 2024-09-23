package com.minhvu.interaction.controller;

import com.minhvu.interaction.dto.AppUserDto;
import com.minhvu.interaction.dto.CommentDto;
import com.minhvu.interaction.dto.CreateCommentRequest;
import com.minhvu.interaction.dto.UpdateCommentRequest;
import com.minhvu.interaction.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@Slf4j
@CrossOrigin("*")
public class CommentController extends BaseController {

    private final CommentService commentService;

    // create comment
    @PostMapping("/post")
    public ResponseEntity<CommentDto> save(@RequestBody CreateCommentRequest createCommentRequest,
                                           HttpServletRequest request) {
        AppUserDto user = getCurrentUser(request);
        log.info("post id {}", user.getId());
        return new ResponseEntity<>(commentService.save(user.getId(), createCommentRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Error> delete(@PathVariable UUID id, HttpServletRequest request) {
        Error error = new Error("Comment deleted successfully");
        AppUserDto user = getCurrentUser(request);
        commentService.delete(user.getId(), id);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> all() {
        return new ResponseEntity<>(commentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> byId(@PathVariable UUID id) {
        return new ResponseEntity<>(commentService.getById(id), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> update(HttpServletRequest request, @RequestBody UpdateCommentRequest updateCommentRequest)
    {
        AppUserDto user = getCurrentUser(request);
        return new ResponseEntity<>(commentService.update(user.getId(), updateCommentRequest), HttpStatus.OK);
    }
}
