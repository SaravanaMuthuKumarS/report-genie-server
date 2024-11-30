package com.i2i.rgs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "timesheets")
public class TimeSheet extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "employee_name")
    private String employeeName;

    @Column(name = "work")
    private LocalDate date;

    @Column(name = "billable_hours")
    private int billableHours;

    @Column(name = "non_billable_hours")
    private int nonBillableHours;

    @Column(name = "is_leave")
    private Boolean isLeave;

    @ManyToOne
    private Client client;

    @ManyToOne
    private Project project;
}