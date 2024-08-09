package com.minhvu.interaction.controller;

import com.minhvu.interaction.dto.InteractionDto;
import com.minhvu.interaction.service.IinteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/interactions")
@CrossOrigin("*")
public class InteractionController {

    private final IinteractionService iinteractionService;

    @GetMapping("/post/{postId}")
    public ResponseEntity<InteractionDto> getInteractionsOfPost(@PathVariable Long postId)
    {
        return new ResponseEntity<>(iinteractionService.getInteractionsOfPost(postId), HttpStatus.OK);
    }
}
