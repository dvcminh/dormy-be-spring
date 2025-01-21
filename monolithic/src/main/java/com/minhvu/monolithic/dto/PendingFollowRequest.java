package com.minhvu.monolithic.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PendingFollowRequest {
    private UUID id;
    private String userName;
    private String fullName;
    private String bio;
}
