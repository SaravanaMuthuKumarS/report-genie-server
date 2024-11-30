package com.i2i.rgs.repository;

import com.i2i.rgs.model.Client;
import com.i2i.rgs.model.Project;
import com.i2i.rgs.model.TimeSheet;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<TimeSheet, Long> {
    List<TimeSheet> findByDateBetweenAndClientAndProject(
            LocalDate start, LocalDate end, Client client, Project project);
}