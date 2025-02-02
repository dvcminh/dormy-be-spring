package com.minhvu.monolithic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GroupChatDto {
    public UUID id;
    public String name;
    public String image;
}

