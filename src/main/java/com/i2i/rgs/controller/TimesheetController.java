package com.i2i.rgs.controller;
import com.i2i.rgs.dto.EmployeeTimesheetResponse;
import com.i2i.rgs.dto.TimesheetRequestDto;
import com.i2i.rgs.service.TimesheetService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/api/timesheets")
public class TimesheetController {

    private final TimesheetService service;

    public TimesheetController(TimesheetService service) {
        this.service = service;
    }

    @PostMapping
    public List<EmployeeTimesheetResponse> getTimesheets(@RequestBody TimesheetRequestDto request) {
        LocalDate startDate = LocalDate.parse(request.getStart(), DateTimeFormatter.ofPattern("dd MMMM"));
        LocalDate endDate = LocalDate.parse(request.getEnd(), DateTimeFormatter.ofPattern("dd MMMM"));
        return service.getTimesheet(startDate, endDate, request.getClient(), request.getProject());
    }
}

