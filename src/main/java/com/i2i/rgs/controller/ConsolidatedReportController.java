package com.i2i.rgs.controller;

import java.util.Map;

import com.i2i.rgs.dto.TimesheetRequestDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.ConsolidatedReportService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consolidated")
@RequiredArgsConstructor
public class ConsolidatedReportController {

    private final ConsolidatedReportService consolidatedReportService;

    @PostMapping
    public ResponseEntity<SuccessResponse> createConsolidatedReport(@RequestBody TimesheetRequestDto timesheetRequestDto) {
        consolidatedReportService.createConsolidatedReport(timesheetRequestDto);
        return SuccessResponse.setSuccessResponseCreated("Consolidated Report created successfully",
                null);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getConsolidatedReports() {
        return SuccessResponse.setSuccessResponseOk("Consolidated Reports fetched successfully",
                Map.of("consolidatedReports", consolidatedReportService.getAllConsolidatedReports()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuccessResponse> getConsolidatedReportById(@PathVariable String id) {
        return SuccessResponse.setSuccessResponseOk("Consolidated Report fetched successfully",
                Map.of("timesheet", consolidatedReportService.getById(id)));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<SuccessResponse> approveConsolidatedReport(@PathVariable String id) {
        consolidatedReportService.approveConsolidatedReport(id);
        return SuccessResponse.setSuccessResponseOk("Consolidated Report approved successfully", null);
    }
}
