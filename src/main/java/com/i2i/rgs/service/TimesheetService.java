package com.i2i.rgs.service;

import com.i2i.rgs.dto.EmployeeTimesheetResponse;
import com.i2i.rgs.model.TimeSheet;
import com.i2i.rgs.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetService {

    private final TimesheetRepository repository;

    public TimesheetService(TimesheetRepository repository) {
        this.repository = repository;
    }

    public List<EmployeeTimesheetResponse> getTimesheet(
            LocalDate start, LocalDate end, String client, String project) {

        List<TimeSheet> timesheets = repository.findByDateBetweenAndClientAndProject(start, end, client, project);

        return timesheets.stream()
                .collect(Collectors.groupingBy(TimeSheet::getEmpName))
                .entrySet()
                .stream()
                .map(entry -> {
                    String empName = entry.getKey();
                    List<TimeSheet> empTimesheets = entry.getValue();

                    int totalBillable = empTimesheets.stream().mapToInt(TimeSheet::getBillableHrs).sum();
                    int totalNonBillable = empTimesheets.stream().mapToInt(TimeSheet::getNonBillableHrs).sum();
                    int totalLeave = empTimesheets.stream().mapToInt(TimeSheet::getLeave).sum();

                    return new EmployeeTimesheetResponse(empName, client, project, totalBillable, totalNonBillable, totalLeave);
                })
                .collect(Collectors.toList());
    }
}


