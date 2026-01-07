package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.DepartmentUpdateRequest;
import com.spring.otmanagement.dto.DepartmentUpdateResponse;
import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.DepartmentRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public Department createDepartment(Department department) {
        return this.departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return this.departmentRepository.findAll();
    }

    public DepartmentUpdateResponse updateDepartment(Long id, DepartmentUpdateRequest department) {
        Department existingDepartment = this.departmentRepository.findById(id).orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy Department"));
        DepartmentUpdateResponse responseDepartment = new DepartmentUpdateResponse();
        User manager = this.userRepository.findById(department.getManagerId()).orElseThrow(() -> new AppException(404, "Not Found", "Không tìm thấy Manager"));
        existingDepartment.setManager(manager);
        existingDepartment.setName(department.getName());
        Department savedDepartment = this.departmentRepository.save(existingDepartment);
        responseDepartment.setId(savedDepartment.getId());
        responseDepartment.setName(savedDepartment.getName());
        responseDepartment.setManagerName(manager.getName());
        return responseDepartment;
    }

    public void deleteDepartment(Long id) {
        if (!this.departmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phòng ban");
        }
        this.departmentRepository.deleteById(id);
    }
}
