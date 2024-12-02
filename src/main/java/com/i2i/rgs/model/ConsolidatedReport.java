package com.i2i.rgs.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "consolidated_report")
public class ConsolidatedReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "start_month")
    private String startMonth;

    @Column(name = "end_month")
    private String endMonth;

    private int year;

    @Column(name = "client")
    private String client;

    @Column(name = "project")
    private String project;

    @Column(name = "is_approved")
    private Boolean isApproved;
}
