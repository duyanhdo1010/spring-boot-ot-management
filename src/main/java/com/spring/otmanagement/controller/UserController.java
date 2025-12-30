package com.spring.otmanagement.controller;

import com.spring.otmanagement.dto.LoginRequest;
import com.spring.otmanagement.dto.UserCreationRequest;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/auth/register")
    public User createUser(@RequestBody UserCreationRequest user) {
        return userService.createUser(user);
    }

    @GetMapping("/api/v1/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/auth/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }
}
