package com.i2i.rgs.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class CreateEmployeeDto {

    @NotNull(message = "Employee name can not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Employee name must contain Alphabets only")
    private String name;

    @NotNull(message = "Employee Id can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9\\s]+$", message = "Employee Id must contain Alphabets and numbers only")
    private String eid;

    @NotNull(message = "Password must not be empty")
    @Pattern(regexp = "^[0-9a-zA-Z\\s]+$", message = "Password must contain Alphabets and numbers only")
    private String password;

    @NotNull(message = "IsFinance must not be empty")
    private Boolean isFinance;

    @Valid
    private CreateProjectDto project;
}
