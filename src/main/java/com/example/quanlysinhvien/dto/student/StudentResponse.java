package com.example.quanlysinhvien.dto.student;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String studentCode;
    private String fullName;
    private Date dob;
    private String className;

    // Tên khoa (Lấy từ khóa ngoại)
    private String departmentName;

    // Tài khoản và Email (Lấy từ khóa ngoại)
    private String username;
    private String email;

    private Boolean status;
}