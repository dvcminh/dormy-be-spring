package com.minhvu.monolithic.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CreateGroupChatRequest {
    private String name;
    private String image;
    private List<UUID> userIds;
}
