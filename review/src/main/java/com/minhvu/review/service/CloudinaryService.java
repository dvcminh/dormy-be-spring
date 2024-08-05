package com.minhvu.review.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CloudinaryService {
    String uploadProductImage(MultipartFile imageFile) throws IOException;
}
