package com.minhvu.monolithic.repository;

import com.minhvu.monolithic.entity.AppUser;
import com.minhvu.monolithic.entity.Report;
import com.minhvu.monolithic.entity.enums.ReportStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID> {
    List<Report> findByStatus(ReportStatus status);

    List<Report> findByReportedUserAndStatus(AppUser user, ReportStatus status);

    Page<Report> findAllByStatus(ReportStatus status,
                                 Pageable pageable);

    long countByReportedUserAndStatus(AppUser reportedUser, ReportStatus reportStatus);
}
