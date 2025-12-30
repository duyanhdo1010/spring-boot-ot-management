package com.spring.otmanagement.config;

import com.spring.otmanagement.repository.UserRepository; // Nhớ import Repo của bạn
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 1. BEAN TÌM NGƯỜI DÙNG
    // Giải thích: Spring không biết cách tìm user trong bảng SQL của bạn.
    // Bạn phải chỉ cho nó: "Hãy dùng hàm findByEmail trong userRepository để tìm nhé".
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    // 2. BEAN XÁC THỰC (Bộ não)
    // Giải thích: Đây là nơi gom 2 thứ lại: "Tìm user ở đâu?" và "Mã hóa pass kiểu gì?".
    // Nó sẽ so sánh password người dùng nhập với password trong DB.
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder()); // Dùng Bean ở dưới
        return authProvider;
    }

    // 3. BEAN QUẢN LÝ (Sếp tổng)
    // Giải thích: Cái này để dùng cho chức năng Login sau này.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // 4. BEAN MÃ HÓA MẬT KHẨU
    // Giải thích: Công cụ để mã hóa và kiểm tra mật khẩu.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}