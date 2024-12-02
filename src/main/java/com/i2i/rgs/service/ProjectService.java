package com.i2i.rgs.service;

import java.util.List;
import java.util.NoSuchElementException;
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
        project.setAudit("USER");
        projectRepository.save(project);
    }

    public Project getModel(String id) {
        try {
            return projectRepository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Project not found for id " + id);
        }
    }

    public ProjectDto getById(String id) {
        Project project = getModel(id);
        return ProjectMapper.modelToDto(project);
    }

    public Set<ProjectDto> getAllProjects() {
        List<Project> projects = projectRepository.findAllByIsDeletedFalse();
        return projects.stream()
                .map(ProjectMapper::modelToDto)
                .collect(Collectors.toSet());
    }

    public Project getByName(String name) {
        Project project = projectRepository.findByName(name);
        if (project == null) {
            throw new NoSuchElementException("Project not found for name " + name);
        }
        return project;
    }
}
