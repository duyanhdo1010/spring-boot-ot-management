package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.ProjectCreationRequest;
import com.spring.otmanagement.dto.ProjectUpdateRequest;
import com.spring.otmanagement.entity.Project;
import com.spring.otmanagement.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController (ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public Project createProject(@RequestBody @Valid ProjectCreationRequest project) {
        return this.projectService.createProject(project);
    }

    @GetMapping
    public List<Project> getAllProjects() {
        return this.projectService.getAllProjects();
    }


    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        this.projectService.deleteProject(id);
    }

    @PatchMapping("/{id}")
    public Project updateProject(@PathVariable Long id, @RequestBody ProjectUpdateRequest project) {
        return this.projectService.updateProject(id, project);
    }
}
