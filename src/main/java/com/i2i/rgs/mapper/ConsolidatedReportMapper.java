package com.i2i.rgs.mapper;

import com.i2i.rgs.dto.ConsolidatedReportDto;
import com.i2i.rgs.dto.TimesheetRequestDto;
import com.i2i.rgs.model.ConsolidatedReport;

public class ConsolidatedReportMapper {
    public static ConsolidatedReportDto modelToDto(ConsolidatedReport consolidatedReport) {
        return ConsolidatedReportDto.builder()
                .id(consolidatedReport.getId())
                .project(consolidatedReport.getProject())
                .build();
    }

    public static ConsolidatedReport dtoToModel(TimesheetRequestDto timesheetRequestDto) {
        return ConsolidatedReport.builder()
                .startMonth(timesheetRequestDto.getSelectedFromMonth())
                .endMonth(timesheetRequestDto.getSelectedToMonth())
                .year(timesheetRequestDto.getSelectedYear())
                .client(timesheetRequestDto.getSelectedClient())
                .project(timesheetRequestDto.getSelectedProject())
                .isApproved(false)
                .build();
    }
}
