package com.i2i.rgs.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProjectDto {

    private String id;

    @NotNull(message = "Project name can not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Project name must contain Alphabets only")
    private String name;

    @NotNull(message = "Client name can not be empty")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Client name must contain Alphabets only")
    private String clientName;
}
