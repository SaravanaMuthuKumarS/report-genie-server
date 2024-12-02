package com.i2i.rgs.repository;

import java.util.Set;

import com.i2i.rgs.model.ConsolidatedReport;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsolidatedReportRepository extends JpaRepository<ConsolidatedReport, String> {
    Set<ConsolidatedReport> findAllByIsApprovedFalse();
}
