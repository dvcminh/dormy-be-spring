package com.minhvu.media.service;

import com.minhvu.media.dto.MediaDto;
import com.minhvu.media.exception.MediaException;
import com.minhvu.media.model.Media;
import com.minhvu.media.repository.MediaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {

    private final FileStorageService fileStorageService;

    private final MediaRepository mediaRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;


    public MediaDto upload(MultipartFile file, Long userId,Long postId) {
        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

        log.info("storing file {}", filename);
        String url;
        try {
            if (file.isEmpty()) {
                log.warn("failed to store empty file {}", filename);
                throw new MediaException("Failed to store empty file " + filename);
            }

            if (filename.contains("..")) {
                // This is a security check
                log.warn("cannot store file with relative path {}", filename);
                throw new MediaException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }

            if (filename.contains("/")) {
                log.warn("cannot store file with relative path {}", filename);
                throw new MediaException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            url = cloudinaryService.upload(file);
        } catch (Exception e) {
            log.error("failed to store file {}", filename, e);
            throw new MediaException("Failed to store file " + filename);
        }

        MediaDto metadata = new MediaDto();
        metadata.setFilename(filename);
        metadata.setUri(url);
        metadata.setUserId(userId);
        metadata.setPostId(postId);

//        MediaDto metadata = fileStorageService.store(file, userId, postId);
        log.info("metadata: {}", metadata);
        Media media = mediaRepository.save(modelMapper.map(metadata, Media.class));
        metadata.setId(media.getId());
        metadata.setCreatedDate(media.getCreatedDate());

        return metadata;
    }
    public void delete(String mediaUuid) {
        Media media = mediaRepository.findByMediaUuid(mediaUuid)
                .orElseThrow(() -> new MediaException("Media not found with uuid: " + mediaUuid));
        fileStorageService.delete(media.getFilename());
        mediaRepository.delete(media);
    }

    public List<MediaDto> getMediaByPostId(Long postId) {
        List<Media> mediaList = mediaRepository.findByPostId(postId);
        return modelMapper.map(mediaList, List.class);
    }

    public void deleteMediaByPostId(Long postId) {
        mediaRepository.findByPostId(postId).forEach(media -> {
            fileStorageService.delete(media.getFilename());
            mediaRepository.delete(media);
        });
    }
}
