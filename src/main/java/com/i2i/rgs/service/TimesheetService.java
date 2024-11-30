package com.i2i.rgs.service;

import com.i2i.rgs.dto.EmployeeTimesheetResponseDto;
import com.i2i.rgs.dto.TimesheetRequestDto;
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

    public void addTimesheet(int day) {
        TimeSheet timeSheet = TimeSheet.builder()
                .billableHours(6)
                .nonBillableHours(2)
                .isLeave(false)
                .client(clientService.getModel("Opos"))
                .project(projectService.getByName("HL7"))
                .employeeName("Jane")
                .date(LocalDate.of(2024, 11, day))
                .build();
        timesheetRepository.save(timeSheet);
    }

    public List<EmployeeTimesheetResponseDto> getTimesheet(
            TimesheetRequestDto request) {

        LocalDate start = LocalDate.of(request.getYear(), Month.valueOf(request.getStart().toUpperCase()), 1);
        LocalDate end = LocalDate.of(request.getYear(), Month.valueOf(request.getEnd().toUpperCase()), 1).plusMonths(1).minusDays(1);
        Client client = clientService.getModel(request.getClient());
        if (client == null) {
            throw new IllegalArgumentException("Client not found");
        }
        Project project = projectService.getByName(request.getProject());
        if (project == null) {
            throw new IllegalArgumentException("Project not found");
        }
        List<TimeSheet> timesheets = timesheetRepository.findByDateBetweenAndClientAndProject(start, end, client, project);

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
                            .employeeName(empName)
                            .billableHours(totalBillable)
                            .nonBillableHours(totalNonBillable)
                            .leaveCount(totalLeave)
                            .client(request.getClient())
                            .project(request.getProject())
                            .totalHours(totalHours)
                            .build();
                })
                .collect(Collectors.toList());
    }
}

