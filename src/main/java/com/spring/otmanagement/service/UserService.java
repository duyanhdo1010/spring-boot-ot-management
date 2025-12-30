package com.spring.otmanagement.service;

import com.spring.otmanagement.dto.LoginRequest;
import com.spring.otmanagement.dto.UserCreationRequest;
import com.spring.otmanagement.entity.Department;
import com.spring.otmanagement.entity.User;
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

    public User createUser(UserCreationRequest user) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Department userDepartment = departmentRepository.findById(user.getDepartmentId()).orElseThrow(() -> new RuntimeException("Department không tồn tại"));
        newUser.setDepartment(userDepartment);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        return this.userRepository.save(newUser);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    public String login(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không chính xác");
        }

        return jwtService.generateToken(user.getEmail());
    }
}
