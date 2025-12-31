package com.spring.otmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class ProjectCreationRequest {
    @NotBlank
    private String name;

    @JsonProperty("manager_id")
    private Long managerId;

    public ProjectCreationRequest() {}

    public String getName() {
        return name;
    }

    public Long getManagerId() {
        return managerId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
}
