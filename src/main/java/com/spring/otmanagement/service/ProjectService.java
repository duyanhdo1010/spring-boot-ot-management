package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.ProjectCreationRequest;
import com.spring.otmanagement.dto.ProjectUpdateRequest;
import com.spring.otmanagement.entity.Project;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.ProjectRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public ProjectService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public Project createProject(ProjectCreationRequest project) {
        User manager = this.userRepository.findById(project.getManagerId())
                .orElseThrow(() -> new AppException(404, "Not found Exception", "User không tồn tại"));
        Project newProject = new Project();
        newProject.setName(project.getName());
        newProject.setManager(manager);
        return this.projectRepository.save(newProject);
    }

    public List<Project> getAllProjects() {
        return this.projectRepository.findAll();
    }

    public Project updateProject(Long id, ProjectUpdateRequest project) {
        Project currentProject = this.projectRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Not Found Exception", "Không tìm thấy Project"));

        if (project.getManagerId() != null) {
            User newManager = this.userRepository.findById(project.getManagerId())
                    .orElseThrow(() -> new AppException(404, "Not found Exception", "User không tồn tại"));
            currentProject.setManager(newManager);
        }

        if (project.getName() != null) {
            currentProject.setName(project.getName());
        }

        return this.projectRepository.save(currentProject);
    }

    public void deleteProject(Long id) {
        if (!this.projectRepository.existsById(id)) {
            throw new AppException(404, "Not Found Exception", "Không tìm thấy project");
        }
        this.projectRepository.deleteById(id);
    }
}
