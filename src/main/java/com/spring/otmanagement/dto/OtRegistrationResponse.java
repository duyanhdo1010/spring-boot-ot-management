package com.spring.otmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.spring.otmanagement.entity.OtStatus;

import java.time.LocalDateTime;

public class OtRegistrationResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    private Double registrationHours;
    private String reason;

    private Long id;

    private String projectName;
    private String projectManager;

    private String employeeName;
    private String managerName;

    private OtStatus status;

    public OtRegistrationResponse() {}

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRegistrationHours(Double registrationHours) {
        this.registrationHours = registrationHours;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectManager() {
        return projectManager;
    }

    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getManagerName() {
        return managerName;
    }

    public Double getRegistrationHours() {
        return registrationHours;
    }

    public OtStatus getStatus() {
        return status;
    }

    public void setStatus(OtStatus status) {
        this.status = status;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

}
