package com.minhvu.monolithic.dto.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDto {
    private UUID id;

    private String userName;

    private UUID userId;

    private String userProfilePicture;

}
