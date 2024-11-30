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
    private String start;
    private String end;
    private int year;
    private String client;
    private String project;

}
