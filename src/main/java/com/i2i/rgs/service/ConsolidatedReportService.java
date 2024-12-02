package com.i2i.rgs.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import com.i2i.rgs.dto.ConsolidatedReportDto;
import com.i2i.rgs.dto.EmployeeTimesheetResponseDto;
import com.i2i.rgs.dto.TimesheetRequestDto;
import com.i2i.rgs.mapper.ConsolidatedReportMapper;
import com.i2i.rgs.model.ConsolidatedReport;
import com.i2i.rgs.repository.ConsolidatedReportRepository;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsolidatedReportService {
    private final ConsolidatedReportRepository consolidatedReportRepository;
    private final TimesheetService timesheetService;

    public Set<ConsolidatedReportDto> getAllConsolidatedReports() {
        Set<ConsolidatedReport> consolidatedReports = consolidatedReportRepository.findAllByIsApprovedFalse();
        return consolidatedReports.stream().map(ConsolidatedReportMapper::modelToDto).collect(Collectors.toSet());
    }

    public List<EmployeeTimesheetResponseDto> getById(String id) {
        try {
            ConsolidatedReport consolidatedReport = consolidatedReportRepository.findById(id).orElseThrow();
            return timesheetService.getTimesheet(consolidatedReport.getStartMonth(),
                    consolidatedReport.getEndMonth(), consolidatedReport.getYear(),
                    consolidatedReport.getClient(), consolidatedReport.getProject());
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Consolidated Report not found for id " + id);
        }
    }

    public void approveConsolidatedReport(String id) {
        ConsolidatedReport consolidatedReport;
        try {
            consolidatedReport = consolidatedReportRepository.findById(id).orElseThrow();
        } catch (Exception e) {
            throw new NoSuchElementException("Consolidated Report not found for id " + id);
        }
        consolidatedReport.setIsApproved(true);
        consolidatedReportRepository.save(consolidatedReport);
    }

    public void createConsolidatedReport(TimesheetRequestDto timesheetRequestDto) {
        ConsolidatedReport consolidatedReport = ConsolidatedReportMapper.dtoToModel(timesheetRequestDto);
        consolidatedReportRepository.save(consolidatedReport);
    }
}
