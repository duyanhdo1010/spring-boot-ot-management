package com.spring.otmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DepartmentUpdateRequest {
    private String name;

    @JsonProperty("manager_id")
    private Long managerId;

    public DepartmentUpdateRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
