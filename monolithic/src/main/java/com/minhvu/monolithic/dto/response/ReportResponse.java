package com.minhvu.monolithic.dto.response;

import com.minhvu.monolithic.dto.model.AppUserDto;
import com.minhvu.monolithic.entity.enums.ReportReason;
import com.minhvu.monolithic.entity.enums.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    private UUID id;
    private AppUserDto reporterId;
    private AppUserDto reportedUserId;
    private UUID reportedContentId;
    private String reportedContentType;
    private String additionalText;
    private ReportReason reason;
    private ReportStatus status;
    private String resolution;
    private UUID adminId;
    private LocalDateTime createdAt;
}
