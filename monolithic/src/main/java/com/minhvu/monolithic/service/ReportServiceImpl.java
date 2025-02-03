package com.minhvu.monolithic.service;

import com.minhvu.monolithic.dto.mapper.ReportMapper;
import com.minhvu.monolithic.dto.model.ReportDTO;
import com.minhvu.monolithic.dto.response.ReportResponse;
import com.minhvu.monolithic.dto.response.page.PageData;
import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Report;
import com.minhvu.monolithic.entity.enums.ReportStatus;
import com.minhvu.monolithic.repository.ReportRepository;
import com.minhvu.monolithic.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final ReportMapper reportMapper;

    @Override
    public ReportResponse submitReport(UUID reporterId, ReportDTO reportDTO) {
        AppUser reporter = userRepository.findById(reporterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Report report = new Report();
        report.setReporter(reporter);
        report.setReportedContentId(reportDTO.getReportedContentId());
        report.setReportedContentType(reportDTO.getReportedContentType());
        report.setReason(reportDTO.getReason());
        report.setStatus(ReportStatus.PENDING);

        // Auto-ban logic: If a user gets 5+ valid reports, auto-ban them
        if (reportDTO.getReportedUserId() != null) {
            AppUser reportedUser = userRepository.findById(reportDTO.getReportedUserId())
                    .orElseThrow(() -> new RuntimeException("Reported user not found"));

            report.setReportedUser(reportedUser);
            long totalReports = reportRepository.countByReportedUserAndStatus(reportedUser, ReportStatus.PENDING);

            if (totalReports >= 5) {
                reportedUser.setIsBanned(true);
                userRepository.save(reportedUser);
            }
        }

        return reportMapper.toReportResponse(reportRepository.save(report));
    }

    @Override
    public ReportResponse reviewReport(UUID adminId, UUID reportId, ReportStatus status, String resolution) {
        AppUser admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found"));

        report.setStatus(status);
        report.setResolution(resolution);
        report.setAdmin(admin);

        return reportMapper.toReportResponse(reportRepository.save(report));
    }

    @Override
    public PageData<ReportResponse> getReports(ReportStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Report> reportPage = reportRepository.findAllByStatus(status, pageable);
        Page<ReportResponse> reportResponses = reportPage.map(reportMapper::toReportResponse);
        return new PageData<>(reportResponses);
    }

    @Override
    public ReportResponse getReport(UUID reportId) {
        return reportMapper.toReportResponse(reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found")));
    }

    @Override
    public void deleteReport(UUID reportId) {
        reportRepository.deleteById(reportId);
    }
}
