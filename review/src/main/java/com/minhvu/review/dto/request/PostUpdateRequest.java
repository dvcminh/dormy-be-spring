package com.minhvu.review.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequest {
    private String body;
    private List<String> urlsMedia;
}
