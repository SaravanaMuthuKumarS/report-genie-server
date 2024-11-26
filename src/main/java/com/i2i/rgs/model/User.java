package com.i2i.rgs.model;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private String id;

    @Column(name = "name", nullable = false, length = 35)
    private String name;

    @Column(name = "eid", nullable = false, unique = true, length = 20)
    private String eid;

    @Column(name = "email", nullable = false, unique = true, length = 50)
    private String email;

    @Column(name = "hashed_password", nullable = false, length = 100)
    private String hashedPassword;

    @Column(name = "is_finance", nullable = false)
    private Boolean isFinance;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.hashedPassword;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
