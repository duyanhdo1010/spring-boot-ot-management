package com.spring.otmanagement.dto;

import com.spring.otmanagement.entity.User;

public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String departmentName;

    public UserResponse(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.departmentName = user.getDepartment() == null ? "Chưa có phòng ban" : user.getDepartment().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
