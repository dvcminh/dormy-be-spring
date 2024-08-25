package com.minhvu.review.client;

import com.minhvu.review.dto.MediaDTO;
import com.minhvu.review.exception.MediaException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public class MediaFallback implements MediaClient{
    @Override
    public ResponseEntity<List<MediaDTO>> add(List<MultipartFile> files, UUID postId, UUID userId) {
        throw new MediaException("Error while adding media.");
    }

    @Override
    public List<MediaDTO> getMediaByPostId(UUID postId) {
        throw new MediaException("Error while getting media by post id.");
    }

    @Override
    public ResponseEntity<Void> delete(String mediaUuid, UUID userId, UUID postId) {
        throw new MediaException("Error while deleting media.");
    }

    @Override
    public void deleteMediaByPostId(UUID postId) {
        throw new MediaException("Error while deleting media by post id.");
    }
}
