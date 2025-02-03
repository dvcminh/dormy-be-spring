package com.minhvu.monolithic.entity;

import com.minhvu.monolithic.entity.enums.ReportReason;
import com.minhvu.monolithic.entity.enums.ReportStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "reporter_id", nullable = false)
    private AppUser reporter; // Who reported

    @ManyToOne
    @JoinColumn(name = "reported_id")
    private AppUser reportedUser; // If reporting a user

    private UUID reportedContentId; // Post/Comment ID
    private String reportedContentType; // POST, COMMENT, MESSAGE

    @Enumerated(EnumType.STRING)
    private ReportReason reason;

    @Enumerated(EnumType.STRING)
    private ReportStatus status = ReportStatus.PENDING;

    private String resolution; // Admin notes

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private AppUser admin; // Admin who reviewed
}
