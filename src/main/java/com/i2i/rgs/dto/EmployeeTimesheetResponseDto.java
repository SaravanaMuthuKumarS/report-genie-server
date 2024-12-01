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
    private String id;
    private String name;
    private String client;
    private String project;
    private int billable;
    private int nonBillable;
    private int leaves;
    private int totalHours;
}
