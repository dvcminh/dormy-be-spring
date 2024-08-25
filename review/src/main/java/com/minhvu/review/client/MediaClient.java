package com.minhvu.review.client;

import com.minhvu.review.dto.MediaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@FeignClient(name = "MEDIA-SERVICE/api/v1/medias")
public interface MediaClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<List<MediaDTO>> add(@RequestPart("files") List<MultipartFile> files,
                                       @RequestParam("postId") UUID postId, @RequestParam("userId") UUID userId);

    @GetMapping("/post/{postId}")
    List<MediaDTO> getMediaByPostId(@PathVariable("postId") UUID postId);

    @DeleteMapping("/{mediaUuid}")
    ResponseEntity<Void> delete(@PathVariable String mediaUuid,
                                @RequestParam("userId") UUID userId,@RequestParam("postId") UUID postId) ;

    @DeleteMapping("/post/{postId}")
    void deleteMediaByPostId(@PathVariable("postId") UUID postId);
}
