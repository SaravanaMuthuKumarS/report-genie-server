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
                .startMonth(timesheetRequestDto.getStart())
                .endMonth(timesheetRequestDto.getEnd())
                .year(timesheetRequestDto.getYear())
                .client(timesheetRequestDto.getClient())
                .project(timesheetRequestDto.getProject())
                .isApproved(false)
                .build();
    }
}
