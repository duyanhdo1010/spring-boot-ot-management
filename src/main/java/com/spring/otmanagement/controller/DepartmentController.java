package com.spring.otmanagement.controller;

import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.service.DepartmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentController {
    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @PostMapping("/")
    public Department createDepartment(@RequestBody Department department) {
        return this.departmentService.createDepartment(department);
    }

    @GetMapping("/")
    public List<Department> getAllDepartments() {
        return this.departmentService.getAllDepartments();
    }

    @PatchMapping("/{id}")
    public Department updateDepartment(@PathVariable Long id, @RequestBody Department department) {
        return this.departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public void deleteDepartment(@PathVariable Long id) {
        this.departmentService.deleteDepartment(id);
    }
}
