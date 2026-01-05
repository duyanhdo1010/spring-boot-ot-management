package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.OtRegistrationRequest;
import com.spring.otmanagement.dto.OtRegistrationResponse;
import com.spring.otmanagement.dto.OtStatusUpdate;
import com.spring.otmanagement.entity.OtRegistration;
import com.spring.otmanagement.entity.OtStatus;
import com.spring.otmanagement.entity.Project;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.OtRegistrationRepository;
import com.spring.otmanagement.repository.ProjectRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

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

    public List<OtRegistrationResponse> getAllOtRegistrations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));
        return this.otRegistrationRepository.findByEmployee_IdOrManager_Id(employee.getId(), employee.getId()).stream().map(ot -> this.mapToResponse(ot)).toList();
    }

    public OtRegistrationResponse getOtRegistration(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));
        OtRegistration otRegistration = this.otRegistrationRepository.findById(id).orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy đăng ký OT"));
        if (!otRegistration.getEmployee().getId().equals(employee.getId())) {
            throw new AppException(403, "Error", "Không xem được đơn này");
        }
        return mapToResponse(otRegistration);
    }

    public OtRegistrationResponse updateOtRegistration(Long id, OtRegistrationRequest otRegistrationRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));

        OtRegistration currentOt = this.otRegistrationRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy đăng ký OT"));

        if (!employee.getId().equals(currentOt.getEmployee().getId())) {
            throw new AppException(403, "Error", "Không có quyền cập nhật đơn này");
        }

        if (otRegistrationRequest.getStartTime().isAfter(otRegistrationRequest.getEndTime())) {
            throw new AppException(400, "Error", "Thời gian kết thúc phải sau thời gian bắt đầu");
        }

        if (otRegistrationRequest.getProjectId() != null) {
            Project project = projectRepository.findById(otRegistrationRequest.getProjectId())
                    .orElseThrow(() -> new AppException(404, "Error", "Project không tồn tại"));
            currentOt.setProject(project);
        } else {
            currentOt.setProject(null);
        }

        if (otRegistrationRequest.getManagerId() != null) {
            User manager = userRepository.findById(otRegistrationRequest.getManagerId())
                    .orElseThrow(() -> new AppException(404, "Error", "Manager không tồn tại"));
            currentOt.setManager(manager);
        } else {
            if (employee.getDepartment() != null && employee.getDepartment().getManager() != null) {
                currentOt.setManager(employee.getDepartment().getManager());
            } else {
                throw new AppException(400, "Error", "Không tìm thấy manager cho phòng ban này");
            }
        }

        currentOt.setStartTime(otRegistrationRequest.getStartTime());
        currentOt.setEndTime(otRegistrationRequest.getEndTime());
        currentOt.setReason(otRegistrationRequest.getReason());

        Double registrationHours = Duration.between(otRegistrationRequest.getStartTime(), otRegistrationRequest.getEndTime()).toMinutes() / 60.0;
        currentOt.setRegistrationHours(registrationHours);

        currentOt.setStatus(OtStatus.PENDING);

        OtRegistration savedOt = this.otRegistrationRepository.save(currentOt);
        return mapToResponse(savedOt);
    }

    public void deleteOtRegistration(Long id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));
        OtRegistration otRegistration = this.otRegistrationRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy đăng ký OT"));
        if (!employee.getId().equals(otRegistration.getEmployee().getId()) || otRegistration.getStatus() != OtStatus.PENDING) {
            throw new AppException(403, "Error", "Không xóa được đơn này");
        }
        this.otRegistrationRepository.delete(otRegistration);
    }

    public OtRegistrationResponse updateOtStatus(Long id, OtStatusUpdate otStatusUpdate) {
        if (otStatusUpdate == null || otStatusUpdate.getOtStatus() == null) {
            throw new AppException(400, "Bad Request", "Hãy chọn status cho đơn");
        }

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User employee = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(404, "Error", "Employee không tồn tại"));

        OtRegistration currentOt = this.otRegistrationRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy đăng ký OT"));

        if (!currentOt.getManager().getId().equals(employee.getId()) || currentOt.getStatus() != OtStatus.PENDING) {
            throw new AppException(403, "Error", "Bạn không có quyền xét duyệt đơn này");
        }

        currentOt.setStatus(otStatusUpdate.getOtStatus());
        currentOt.setManagerNote(otStatusUpdate.getManagerNote());

        OtRegistration updatedOt = this.otRegistrationRepository.save(currentOt);
        return mapToResponse(updatedOt);
    }
}
