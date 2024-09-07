package com.minhvu.media.controller;


import com.minhvu.media.dto.MediaDto;
import com.minhvu.media.dto.mapper.MediaMapper;
import com.minhvu.media.kafka.MediaProducer;
import com.minhvu.media.repository.MediaRepository;
import com.minhvu.media.service.ImageService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/medias")
@RequiredArgsConstructor
@CrossOrigin("*")
public class MediaController {
    private final ImageService mediaService;
    private final MediaRepository mediaRepository;
    private final MediaProducer mediaProducer;
    private final MediaMapper mediaMapper;

    @PostMapping
    public ResponseEntity<List<MediaDto>> add(@RequestParam("files") List<MultipartFile> files,
                                              @RequestParam("postId") UUID postId, @RequestParam("userId") UUID userId) throws IOException {
        List<MediaDto> mediaList = new ArrayList<>();
        for (MultipartFile file : files) {
            MediaDto media = mediaService.upload(file, userId,postId);
            mediaList.add(media);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(mediaList);
    }

    @DeleteMapping("/{mediaUuid}")
    public ResponseEntity<Void> delete(@PathVariable UUID mediaUuid,@RequestParam("userId") UUID userId,@RequestParam("postId") UUID postId) {
        mediaService.delete(mediaUuid);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/post/{postId}")
    public List<MediaDto> getMediaByPostId(@PathVariable("postId") UUID postId) {
        return mediaService.getMediaByPostId(postId);
    }

    @DeleteMapping("/post/{postId}")
    public void deleteMediaByPostId(@PathVariable("postId") UUID postId) {
        mediaService.deleteMediaByPostId(postId);
    }

    @GetMapping("/sync")
    public ResponseEntity<String> sync() {
        mediaRepository.findAll().forEach(media -> {
            mediaProducer.sendFeedEvent(mediaMapper.toDto(media));
        });
        return ResponseEntity.ok("Synced");
    }
}