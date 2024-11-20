package com.i2i.rgs.mapper;

import com.i2i.rgs.dto.CreateProjectDto;
import com.i2i.rgs.model.Project;

public class ProjectMapper {
    public static CreateProjectDto modelToDto(Project project) {
        return CreateProjectDto.builder()
                .name(project.getName())
                .clientName(project.getClientName())
                .build();
    }
}
