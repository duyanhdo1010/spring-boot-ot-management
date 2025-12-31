package com.spring.otmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "manager_id")
    private User manager;

    public Project(Long id, String name, User manager) {
        this.id = id;
        this.name = name;
        this.manager = manager;
    }

    public Project() {
    }

    public String getManagerName() {
        return manager != null ? manager.getName() : null;
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

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }
}
