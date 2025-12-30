package com.spring.otmanagement.service;

import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department createDepartment(Department department) {
        return this.departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return this.departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, Department department) {
        if (!this.departmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phòng ban");
        }
        department.setId(id);
        return this.departmentRepository.save(department);
    }

    public void deleteDepartment(Long id) {
        if (!this.departmentRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy phòng ban");
        }
        this.departmentRepository.deleteById(id);
    }
}
