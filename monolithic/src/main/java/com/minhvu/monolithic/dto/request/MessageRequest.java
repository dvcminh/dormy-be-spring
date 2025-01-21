package com.minhvu.monolithic.dto.request;

import lombok.Data;

@Data
public class MessageRequest {
    private String phone;
    private String message;
}

