package com.minhvu.monolithic.dto.mapper;

import com.minhvu.monolithic.dto.response.ReportResponse;
import com.minhvu.monolithic.entity.Report;

public interface ReportMapper {
    ReportResponse toReportResponse(Report report);
}
