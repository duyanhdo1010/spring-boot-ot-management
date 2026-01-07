package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.DepartmentCreationRequest;
import com.spring.otmanagement.dto.DepartmentUpdateRequest;
import com.spring.otmanagement.dto.DepartmentUpdateResponse;
import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.DepartmentRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DepartmentService {
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    public DepartmentService(DepartmentRepository departmentRepository, UserRepository userRepository) {
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
    }

    public Department createDepartment(DepartmentCreationRequest department) {
        Department newDepartment = new Department();
        User manager = this.userRepository.findById(department.getManagerId()).orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy manager"));
        if (this.departmentRepository.existsByManagerId(manager.getId())) {
            throw new AppException(400, "Error", "Không thể gán Manager này vì họ đã quản lý phòng ban khác");
        }
        newDepartment.setName(department.getName());
        newDepartment.setManager(manager);
        return this.departmentRepository.save(newDepartment);
    }

    @Transactional(readOnly = true)
    public List<Department> getAllDepartments() {
        return this.departmentRepository.findAll();
    }

    public DepartmentUpdateResponse updateDepartment(Long id, DepartmentUpdateRequest department) {
        Department existingDepartment = this.departmentRepository.findById(id).orElseThrow(() -> new AppException(404, "Not found", "Không tìm thấy Department"));
        DepartmentUpdateResponse responseDepartment = new DepartmentUpdateResponse();
        User manager = this.userRepository.findById(department.getManagerId()).orElseThrow(() -> new AppException(404, "Not Found", "Không tìm thấy Manager"));
        if (this.departmentRepository.existsByManagerId(manager.getId())) {
            throw new AppException(400, "Error", "Không thể gán Manager này vì họ đã quản lý phòng ban khác");
        }
        existingDepartment.setManager(manager);
        existingDepartment.setName(department.getName());
        Department savedDepartment = this.departmentRepository.save(existingDepartment);
        responseDepartment.setId(savedDepartment.getId());
        responseDepartment.setName(savedDepartment.getName());
        responseDepartment.setManagerName(manager.getName());
        return responseDepartment;
    }

    public void deleteDepartment(Long id) {
        this.departmentRepository.findById(id).orElseThrow(() -> new AppException(404, "Not Found", "Không tìm thấy Department"));
        List<User> employees = this.userRepository.findByDepartmentId(id);
        for (User employee : employees) {
            employee.setDepartment(null);
        }
        this.userRepository.saveAll(employees);
        this.departmentRepository.deleteById(id);
    }
}
