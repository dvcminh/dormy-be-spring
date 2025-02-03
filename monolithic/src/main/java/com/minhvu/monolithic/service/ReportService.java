package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.model.ReportDTO;
import com.minhvu.monolithic.dto.response.ReportResponse;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.enums.ReportStatus;

import java.util.UUID;

public interface ReportService {
    ReportResponse submitReport(UUID reporterId, ReportDTO reportDTO);

    ReportResponse reviewReport(UUID adminId, UUID reportId, ReportStatus status, String resolution);

    PageData<ReportResponse> getReports(ReportStatus status, int page, int size);

    ReportResponse getReport(UUID reportId);

    void deleteReport(UUID reportId);
}
