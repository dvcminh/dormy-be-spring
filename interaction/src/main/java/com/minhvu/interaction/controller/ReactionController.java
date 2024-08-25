package com.minhvu.interaction.controller;


import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.exception.Error;
import com.minhvu.interaction.service.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reactions")
@CrossOrigin("*")
public class ReactionController {

    private final ReactionService reactionService;

    @PostMapping("/post/{postId}")
    public ResponseEntity<ReactionDto> save(@PathVariable UUID postId, @RequestBody ReactionDto reactionDto)
    {
        return new ResponseEntity<>(reactionService.save(postId, reactionDto), HttpStatus.CREATED);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<ReactionDto>> getByPostId(@PathVariable UUID postId)
    {
        return new ResponseEntity<>(reactionService.getAllReactionsByPostId(postId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Error> delete(@PathVariable UUID id)
    {
        reactionService.delete(id);
        Error error = new Error("Reaction deleted successfully");
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<ReactionDto>> all()
    {
        return new ResponseEntity<>(reactionService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReactionDto> byId(@PathVariable UUID id)
    {
        return new ResponseEntity<>(reactionService.getById(id), HttpStatus.OK);
    }
}
