package com.example.quanlysinhvien.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    // Chức năng: Class này dùng để "hứng" dữ liệu JSON từ ReactJS gửi lên
    // Khi user điền form đăng nhập, ReactJS sẽ gửi username và password vào đây.

    private String username;
    private String password;
}