package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.*;
import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.entity.User;
import com.spring.otmanagement.exception.AppException;
import com.spring.otmanagement.repository.DepartmentRepository;
import com.spring.otmanagement.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse createUser(UserCreationRequest user) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Department userDepartment = departmentRepository.findById(user.getDepartmentId())
                .orElseThrow(() -> new AppException(404, "Department not found", "Department không tồn tại"));
        newUser.setDepartment(userDepartment);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        return new UserResponse(this.userRepository.save(newUser));
    }

    public List<UserResponse> getAllUsers() {
        return this.userRepository.findAll().stream().map(user -> new UserResponse(user)).toList();
    }

    public UserResponse updateUser(Long id, UserUpdateRequest user) {
        User updatedUser = this.userRepository.findById(id)
                .orElseThrow(() -> new AppException(404, "User not found", "User không tồn tại"));
        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }

        if (user.getDepartmentId() != null) {
            Department userDepartment = departmentRepository.findById(user.getDepartmentId())
                    .orElseThrow(() -> new AppException(404, "Department not found", "Department không tồn tại"));
            updatedUser.setDepartment(userDepartment);
        }

        if (user.getPassword() != null) {
            updatedUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return new UserResponse(this.userRepository.save(updatedUser));
    }

    public void deleteUser(Long id) {
        if (!this.userRepository.existsById(id)) {
            throw new AppException(404, "User not found", "User không tồn tại");
        }
        this.userRepository.deleteById(id);
    }

    public AuthResponse login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        String access_token = jwtService.generateToken(user.getEmail());
        AuthResponse authResponse = new AuthResponse();
        authResponse.setAccessToken(access_token);
        return authResponse;
    }
}
