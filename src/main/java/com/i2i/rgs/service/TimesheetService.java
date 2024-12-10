package com.i2i.rgs.service;

import com.i2i.rgs.dto.EmployeeTimesheetResponseDto;
import com.i2i.rgs.model.Client;
import com.i2i.rgs.model.Project;
import com.i2i.rgs.model.TimeSheet;
import com.i2i.rgs.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TimesheetService {

    private final TimesheetRepository timesheetRepository;
    private final ClientService clientService;
    private final ProjectService projectService;
    static int id = 0;

    public void addTimesheet(int day) {
        TimeSheet timeSheet = TimeSheet.builder()
                .billableHours(6)
                .nonBillableHours(2)
                .isLeave(false)
                .client(clientService.getModel("Opos"))
                .project(projectService.getProjectById("HL7"))
                .employeeName("Jane")
                .date(LocalDate.of(2024, 11, day))
                .build();
        timesheetRepository.save(timeSheet);
    }

    public List<EmployeeTimesheetResponseDto> getTimesheet(
            String startMonth, String endMonth, int year, String clientId, String projectId) {

        LocalDate start = LocalDate.of(year, Month.valueOf(startMonth.toUpperCase()), 1);
        LocalDate end = LocalDate.of(year, Month.valueOf(endMonth.toUpperCase()), 1).plusMonths(1).minusDays(1);
        Client client = clientService.getModel(clientId);
        Project project = projectService.getProjectById(projectId);
        List<TimeSheet> timesheets = timesheetRepository.findByDateBetweenAndClientAndProject(start, end, client, project);
        id = 0;
        return timesheets.stream()
                .collect(Collectors.groupingBy(TimeSheet::getEmployeeName))
                .entrySet()
                .stream()
                .map(entry -> {
                    String empName = entry.getKey();
                    List<TimeSheet> empTimesheets = entry.getValue();
                    int totalBillable = 0;
                    int totalNonBillable = 0;
                    int totalLeave = 0;
                    int totalHours = 0;
                    for(TimeSheet timeSheet : empTimesheets) {
                        totalBillable += timeSheet.getBillableHours();
                        totalNonBillable += timeSheet.getNonBillableHours();
                        totalHours += timeSheet.getBillableHours() + timeSheet.getNonBillableHours();
                        totalLeave += timeSheet.getIsLeave() ? 1 : 0;
                    }
                    return EmployeeTimesheetResponseDto.builder()
                            .id(++id + "")
                            .name(empName)
                            .billable(totalBillable)
                            .nonBillable(totalNonBillable)
                            .leaves(totalLeave)
                            .client(clientId)
                            .project(projectId)
                            .totalHours(totalHours)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

