package com.example.quanlysinhvien.dto.teacher;

import lombok.Data;

@Data
public class TeacherRequest {
    // Thông tin cá nhân của giảng viên
    private String fullName;
    private String phone;

    // Khóa ngoại: Chỉ cần truyền ID của khoa mà giảng viên thuộc về
    private Long departmentId;

    // Thông tin để tự động tạo tài khoản đăng nhập (User)
    private String username;
    private String password;
    private String email;
}