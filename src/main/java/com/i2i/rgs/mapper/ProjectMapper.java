package com.i2i.rgs.mapper;

import com.i2i.rgs.dto.CreateProjectDto;
import com.i2i.rgs.dto.ProjectDto;
import com.i2i.rgs.model.Project;

public class ProjectMapper {

    public static Project dtoToModel(CreateProjectDto projectDto) {
        return Project.builder()
                .name(projectDto.getName())
                .build();
    }

    public static ProjectDto modelToDto(Project project) {
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .build();
    }
}
