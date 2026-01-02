package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.OtRegistrationRequest;
import com.spring.otmanagement.dto.OtRegistrationResponse;
import com.spring.otmanagement.entity.OtRegistration;
import com.spring.otmanagement.entity.Project;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.OtRegistrationRepository;
import com.spring.otmanagement.repository.ProjectRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class OtRegistrationService {
    private final OtRegistrationRepository otRegistrationRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    public OtRegistrationService(OtRegistrationRepository otRegistrationRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.otRegistrationRepository = otRegistrationRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    private OtRegistrationResponse mapToResponse(OtRegistration otRegistration) {
        OtRegistrationResponse responseObject = new OtRegistrationResponse();
        responseObject.setCreatedAt(otRegistration.getCreatedAt());
        responseObject.setId(otRegistration.getId());
        responseObject.setStartTime(otRegistration.getStartTime());
        responseObject.setEndTime(otRegistration.getEndTime());
        if (otRegistration.getProject() != null && otRegistration.getProject().getName() != null) {
            responseObject.setProjectName(otRegistration.getProject().getName());
        } else {
            responseObject.setProjectName(null);
        }

        if (otRegistration.getProject() != null) {
            responseObject.setProjectManager(otRegistration.getProject().getManagerName());
        } else {
            responseObject.setProjectManager(null);
        }
        responseObject.setManagerName(otRegistration.getManager().getName());
        responseObject.setRegistrationHours(otRegistration.getRegistrationHours());
        responseObject.setReason(otRegistration.getReason());
        responseObject.setEmployeeName(otRegistration.getEmployee().getName());
        responseObject.setStatus(otRegistration.getStatus());

        return responseObject;
    }

    public OtRegistrationResponse registerOt(OtRegistrationRequest otRegistrationRequest) {
        OtRegistration newOtRegistration = new OtRegistration();
        if (otRegistrationRequest.getStartTime().isAfter(otRegistrationRequest.getEndTime())) {
            throw new AppException(400, "Error", "Thời gian kết thúc phải sau thời gian bắt đầu");
        }


        Project project = null;
        if (otRegistrationRequest.getProjectId() != null) {
            project = projectRepository.findById(otRegistrationRequest.getProjectId())
                .orElseThrow(() -> new AppException(404, "Error", "Project không tồn tại"));
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
        .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));

        User manager;
        if (otRegistrationRequest.getManagerId() != null) {
            manager = userRepository.findById(otRegistrationRequest.getManagerId())
                .orElseThrow(() -> new AppException(404, "Error", "Manager không tồn tại"));
        } else {
            if (employee.getDepartment() != null && employee.getDepartment().getManager() != null) {
                manager = employee.getDepartment().getManager();
            } else {
                throw new AppException(400, "Error", "Không tìm thấy manager cho phòng ban này");
            }
        }

        Double registrationHours = Duration.between(otRegistrationRequest.getStartTime(), otRegistrationRequest.getEndTime()).toMinutes() / 60.0;

        newOtRegistration.setStartTime(otRegistrationRequest.getStartTime());
        newOtRegistration.setEndTime(otRegistrationRequest.getEndTime());
        newOtRegistration.setProject(project);
        newOtRegistration.setManager(manager);
        newOtRegistration.setEmployee(employee);
        newOtRegistration.setReason(otRegistrationRequest.getReason());
        newOtRegistration.setRegistrationHours(registrationHours);

        OtRegistration savedOt = this.otRegistrationRepository.save(newOtRegistration);

        return this.mapToResponse(savedOt);
    }
}
