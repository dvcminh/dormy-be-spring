package com.minhvu.review.repository;

import com.minhvu.review.model.ReviewPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewPhotoRepository extends JpaRepository<ReviewPhoto, UUID> {

}
