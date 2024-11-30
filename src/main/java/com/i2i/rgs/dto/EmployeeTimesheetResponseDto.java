package com.i2i.rgs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeTimesheetResponseDto {
    private String employeeName;
    private String client;
    private String project;
    private int billableHours;
    private int nonBillableHours;
    private int leaveCount;
    private int totalHours;
}
