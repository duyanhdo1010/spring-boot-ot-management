package com.spring.otmanagement.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // 1. Hàm tạo Token
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    // 2. Hàm logic thực sự để build token
    private String createToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Ngày cấp
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration)) // Hết hạn sau 30 phút
                .signWith(getSignKey(), SignatureAlgorithm.HS256) // Ký tên bằng con dấu bí mật
                .compact();
    }

    // 3. Hàm lấy Key chuẩn từ chuỗi String bí mật
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 4. Lấy Username (Email) từ Token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 5. Hàm tổng quát để lấy bất kỳ thông tin nào
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 6. Hàm giải mã toàn bộ Token (Cần Key để mở khóa)
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey()) // Dùng key bí mật để mở
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 7. Kiểm tra Token có hợp lệ không (So ngược lại với UserDetails)
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        // Hợp lệ khi: Username trùng khớp VÀ Token chưa hết hạn
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // 8. Kiểm tra xem Token hết hạn chưa
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 9. Lấy ngày hết hạn từ Token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}