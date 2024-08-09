package com.minhvu.media.service;

import com.cloudinary.Cloudinary;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;
    public String upload(MultipartFile imageFile) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("folder", "product_images");

        Map<?, ?> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), params);

        return uploadResult.get("secure_url").toString();
    }
}
