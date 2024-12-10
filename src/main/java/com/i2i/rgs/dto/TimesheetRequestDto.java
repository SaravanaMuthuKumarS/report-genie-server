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
public class TimesheetRequestDto {
    private String selectedFromMonth;
    private String selectedToMonth;
    private int selectedYear;
    private String selectedClient;
    private String selectedProject;

}
