package com.example.quanlysinhvien.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    // Lấy chuỗi bí mật từ file application.yml (Lát nữa ta sẽ cấu hình)
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    // Thời gian sống của Token
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // 1. Tạo JWT từ thông tin đăng nhập
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // 2. Chuyển chuỗi bí mật thành Key mã hóa
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // 3. Lấy Username ngược lại từ chuỗi JWT
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 4. Kiểm tra Token có hợp lệ không (có bị sửa đổi, hết hạn không)
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (JwtException e) {
            System.err.println("Lỗi Token: " + e.getMessage());
        }
        return false;
    }
}