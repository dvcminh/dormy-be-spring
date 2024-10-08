package com.minhvu.interaction.controller;

import com.minhvu.interaction.dto.CreateSharedRequest;
import com.minhvu.interaction.dto.SharedDto;
import com.minhvu.interaction.dto.UpdateSharedRequest;
import com.minhvu.interaction.service.SharedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/shareds")
@CrossOrigin("*")
public class SharedController {

    private final SharedService sharedService;

    @GetMapping
    public ResponseEntity<List<SharedDto>> all()
    {
        return new ResponseEntity<>(sharedService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/post")
    public ResponseEntity<SharedDto> save(@PathVariable UUID postId, @RequestBody CreateSharedRequest createSharedRequest)
    {
        return new ResponseEntity<>(sharedService.save(postId, createSharedRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Error> delete(@PathVariable UUID id)
    {
        Error error = new Error("Shared deleted successfully");
        sharedService.delete(id);
        return new ResponseEntity<>(error, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SharedDto> update(@PathVariable UUID id, @RequestBody UpdateSharedRequest updateSharedRequest)
    {
        return new ResponseEntity<>(sharedService.update(id, updateSharedRequest), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SharedDto> byId(@PathVariable UUID id)
    {
        return new ResponseEntity<>(sharedService.getById(id), HttpStatus.OK);
    }
}
