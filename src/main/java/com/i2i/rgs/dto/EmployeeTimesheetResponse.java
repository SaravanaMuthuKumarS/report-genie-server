package com.i2i.rgs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeTimesheetResponse {
    private String empName;
    private String client;
    private String project;
    private int billable;
    private int nonBillable;
    private int leave;

    public EmployeeTimesheetResponse(String empName, String client, String project, int billable, int nonBillable, int leave) {
        this.empName = empName;
        this.client = client;
        this.project = project;
        this.billable = billable;
        this.nonBillable = nonBillable;
        this.leave = leave;
    }

}

