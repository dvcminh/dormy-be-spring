package com.minhvu.monolithic.dto.request;

import com.minhvu.monolithic.enums.PostType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CreatePostRequestDto {
    @Size(max = 200, message = "Caption cannot be more than 200 characters")
    private String caption;

    @NotNull(message = "Post type is required")
    private PostType postType;

    @NotNull(message = "Post content file is required")
    private String postContentFileUrl;
}
