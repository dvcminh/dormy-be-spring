package com.minhvu.monolithic.controller;

import com.minhvu.monolithic.dto.model.ReportDTO;
import com.minhvu.monolithic.dto.response.ReportResponse;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.enums.ReportStatus;
import com.minhvu.monolithic.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping("/submit")
    public ResponseEntity<ReportResponse> submitReport(@RequestBody ReportDTO reportDTO,
                                                       @RequestParam UUID reporterId) {
        return ResponseEntity.ok(reportService.submitReport(reporterId, reportDTO));
    }

    @PostMapping("/review/{reportId}")
    public ResponseEntity<ReportResponse> reviewReport(@PathVariable UUID reportId,
                                                       @RequestParam UUID adminId,
                                                       @RequestParam ReportStatus status,
                                                       @RequestParam String resolution) {
        return ResponseEntity.ok(reportService.reviewReport(adminId, reportId, status, resolution));
    }

    @GetMapping("/all")
    public ResponseEntity<PageData<ReportResponse>> getAllReports(@RequestParam(required = false, defaultValue = "0") int page,
                                                                  @RequestParam(required = false, defaultValue = "10") int size,
                                                                  @RequestParam(required = false) ReportStatus status) {
        return ResponseEntity.ok(reportService.getReports(status, page, size));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<ReportResponse> getReport(@PathVariable UUID reportId) {
        return ResponseEntity.ok(reportService.getReport(reportId));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<Void> deleteReport(@PathVariable UUID reportId) {
        reportService.deleteReport(reportId);
        return ResponseEntity.noContent().build();
    }
}
