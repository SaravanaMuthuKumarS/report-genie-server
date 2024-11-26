package com.i2i.rgs.controller;

import java.util.Map;
import java.util.Set;

import com.i2i.rgs.dto.CreateProjectDto;
import com.i2i.rgs.dto.ProjectDto;
import com.i2i.rgs.helper.SuccessResponse;
import com.i2i.rgs.service.ProjectService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<SuccessResponse> addProject(@RequestBody CreateProjectDto project) {
        projectService.addProject(project);
        return SuccessResponse.setSuccessResponseCreated("Project added successfully", null);
    }

    @GetMapping
    public ResponseEntity<SuccessResponse> getProjects(@RequestBody String name) {
        Set<String> projects = projectService.getAllProjects();
        return SuccessResponse.setSuccessResponseOk("Project fetched successfully", Map.of("projects", projects));
    }

    @GetMapping("/{projectName}")
    public ResponseEntity<SuccessResponse> getProject(@PathVariable String projectName) {
        ProjectDto project = projectService.getByName(projectName);
        return SuccessResponse.setSuccessResponseOk("Project fetched successfully", Map.of("project", project));
    }
}
