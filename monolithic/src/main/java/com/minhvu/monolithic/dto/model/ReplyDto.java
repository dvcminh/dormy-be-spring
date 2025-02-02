package com.minhvu.monolithic.dto.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private UUID id;

    private  String text;

    private LocalDateTime createdAt;

    private  UUID UserId;

    private  String userName;

    private  String UserprofilePicture;

    private  UUID parentCommentId;
}
