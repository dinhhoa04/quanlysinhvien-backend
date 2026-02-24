package com.example.quanlysinhvien.dto.student;

import lombok.Data;
import java.util.Date;

@Data
public class StudentRequest {
    // Thông tin cá nhân
    private String studentCode;
    private String fullName;
    private Date dob; // Định dạng gửi từ Postman sẽ là: "yyyy-MM-dd"
    private String className;

    // ID của Khoa
    private Long departmentId;

    // Thông tin tạo tài khoản User
    private String username;
    private String password;
    private String email;
}