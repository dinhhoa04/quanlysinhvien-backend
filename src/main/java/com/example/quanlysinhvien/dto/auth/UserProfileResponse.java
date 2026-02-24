package com.example.quanlysinhvien.dto.auth;

import lombok.Data;

@Data
public class UserProfileResponse {
    // Chức năng: Khi ReactJS gọi API lấy thông tin người dùng, ta dùng class này
    // để trả về dữ liệu. Ta KHÔNG trả về class User trong Entity vì nó chứa mật khẩu (rất nguy hiểm).

    private Long id;
    private String username;
    private String email;
    private String role; // Quyền của user (Admin, Teacher, Student)
}