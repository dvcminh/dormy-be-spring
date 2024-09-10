package com.minhvu.interaction.controller;

import com.minhvu.interaction.dto.InteractionDto;
import com.minhvu.interaction.service.InteractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/interactions")
@CrossOrigin("*")
public class InteractionController {

    private final InteractionService interactionService;
}
