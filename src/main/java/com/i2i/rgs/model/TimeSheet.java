package com.i2i.rgs.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Set;

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

    @Column(name = "work", nullable = false)
    private Date work_date;

    @Column(name = "number_of_hours", nullable = false)
    private float number_of_hours;

    @Column(name = "work_location", nullable = false)
    private String work_location;

    @Column(name = "billable_type", nullable = false)
    private int billable_type;

}