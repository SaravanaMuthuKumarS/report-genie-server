package com.i2i.rgs.controller;

import com.i2i.rgs.dto.EmployeeTimesheetResponseDto;
import com.i2i.rgs.dto.TimesheetRequestDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.TimesheetService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/timesheets")
@RequiredArgsConstructor
public class TimesheetController {

    private final TimesheetService timesheetService;

    @PostMapping("/{day}")
    public void addTimesheet(@PathVariable String day) {
        timesheetService.addTimesheet(Integer.parseInt(day));
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> getTimesheets(@RequestBody TimesheetRequestDto request) {
        List<EmployeeTimesheetResponseDto> timesheets = timesheetService.getTimesheet(request);
        return SuccessResponse.setSuccessResponseOk("Timesheets fetched successfully",
                Map.of("timesheets", timesheets));
    }
}
