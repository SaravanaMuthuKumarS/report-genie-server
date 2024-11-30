package com.i2i.rgs.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.i2i.rgs.dto.CreateProjectDto;
import com.i2i.rgs.dto.ProjectDto;
import com.i2i.rgs.mapper.ProjectMapper;
import com.i2i.rgs.model.Project;
import com.i2i.rgs.repository.ProjectRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ClientService clientService;

    public void addProject(CreateProjectDto projectToAdd) {
        Project project = ProjectMapper.dtoToModel(projectToAdd);
        project.setClient(clientService.getModel(projectToAdd.getClientName()));
        projectRepository.save(project);
    }

    public Project getModel(String id) {
        //return projectRepository.findByName(name);
        return projectRepository.findById(id).get();
    }

    public ProjectDto getByName(String name) {
        Project project = getModel(name);
        return ProjectMapper.modelToDto(project);
    }

    public Set<String> getAllProjects() {
        List<Project> projects = projectRepository.findAllByIsDeletedFalse();
        return projects.stream()
                .map(Project::getName)
                .collect(Collectors.toSet());
    }
}
