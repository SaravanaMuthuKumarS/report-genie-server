package com.i2i.rgs.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotNull(message = "Employee name can not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Employee name must contain Alphabets only")
    private String fullName;

    @NotNull(message = "Email must not be empty")
    @Email(message = "Email should be in valid format")
    private String mailId;

    @NotNull(message = "Password must not be empty")
    @Pattern(regexp = "^[0-9a-zA-Z\\s]+$", message = "Password must contain Alphabets and numbers only")
    private String password;

    @NotNull(message = "IsFinance must not be empty")
    private Boolean isFinance;

    private Set<CreateProjectDto> projects;
}
