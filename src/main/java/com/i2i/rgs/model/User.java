package com.i2i.rgs.model;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 35)
    private String name;

    @Column(name = "eid", nullable = false, unique = true, length = 20)
    private String eid;

    @Column(name = "hashed_password", nullable = false, length = 100)
    private String hashedPassword;

    @Column(name = "is_finance", nullable = false)
    private Boolean isFinance;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;
}
