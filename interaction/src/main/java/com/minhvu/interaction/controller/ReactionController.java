package com.minhvu.interaction.controller;


import com.minhvu.interaction.dto.CreateReactionRequest;
import com.minhvu.interaction.dto.ReactionDto;
import com.minhvu.interaction.dto.UpdateReactionRequest;
import com.minhvu.interaction.dto.mapper.ReactionMapper;
import com.minhvu.interaction.kafka.ReactionProducer;
import com.minhvu.interaction.repository.ReactionRepository;
import com.minhvu.interaction.service.ReactionService;
import jakarta.servlet.http.HttpServletRequest;
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
public class ReactionController extends BaseController{

    private final ReactionService reactionService;
    private final ReactionRepository reactionRepository;
    private final ReactionProducer reactionProducer;
    private final ReactionMapper reactionMapper;

    // create reaction to post
    @PostMapping("/post")
    public ResponseEntity<ReactionDto> save(HttpServletRequest request, @RequestBody CreateReactionRequest createReactionRequest)
    {
        return new ResponseEntity<>(reactionService.save(getCurrentUser(request).getId(), createReactionRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Error> delete(HttpServletRequest request, @PathVariable UUID id)
    {
        reactionService.delete(getCurrentUser(request).getId(), id);
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

    @PutMapping("/{id}")
    public ResponseEntity<ReactionDto> update(@PathVariable UUID id, @RequestBody UpdateReactionRequest updateReactionRequest)
    {
        return new ResponseEntity<>(reactionService.update(id, updateReactionRequest), HttpStatus.OK);
    }

    @GetMapping("/sync")
    public ResponseEntity<String> sync()
    {
        reactionRepository.findAll().forEach(reation -> {
            reactionProducer.send(reactionMapper.toDto(reation));
        });
        return ResponseEntity.ok("Synced");
    }
}
