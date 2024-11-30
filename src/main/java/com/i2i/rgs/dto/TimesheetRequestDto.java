package com.i2i.rgs.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TimesheetRequestDto {
    private String start;
    private String end;
    private int year;
    private String client;
    private String project;

}

