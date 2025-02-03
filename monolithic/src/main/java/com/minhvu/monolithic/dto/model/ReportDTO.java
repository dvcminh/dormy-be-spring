package com.minhvu.monolithic.dto.model;

import com.minhvu.monolithic.entity.enums.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportDTO {
    private UUID reportedUserId; // Optional (if reporting a user)
    private UUID reportedContentId;
    private String reportedContentType; // POST, COMMENT, MESSAGE
    private ReportReason reason;
}
