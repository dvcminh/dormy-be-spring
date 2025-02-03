package com.minhvu.monolithic.dto.mapper;

import com.minhvu.monolithic.dto.response.ReportResponse;
import com.minhvu.monolithic.entity.Report;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportMapperImpl implements ReportMapper {
    private final AppUserMapper appUserMapper;


    @Override
    public ReportResponse toReportResponse(Report report) {
        if (report == null) {
            return null;
        }

        ReportResponse reportResponse = new ReportResponse();
        reportResponse.setId(report.getId());
        reportResponse.setReporterId(appUserMapper.toDto(report.getReporter()));
        reportResponse.setReportedUserId(appUserMapper.toDto(report.getReportedUser()));
        reportResponse.setReportedContentId(report.getReportedContentId());
        reportResponse.setReportedContentType(report.getReportedContentType());
        reportResponse.setReason(report.getReason());
        reportResponse.setStatus(report.getStatus());
        reportResponse.setResolution(report.getResolution());
        if (report.getAdmin() != null) {
            reportResponse.setAdminId(report.getAdmin().getId());
        }
        reportResponse.setCreatedAt(report.getCreatedAt());

        return reportResponse;
    }
}
