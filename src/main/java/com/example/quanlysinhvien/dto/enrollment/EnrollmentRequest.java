package com.example.quanlysinhvien.dto.enrollment;

import lombok.Data;

@Data
public class EnrollmentRequest {
    private Long studentId;
    private Long subjectId;
    private String semester;
    private Double score; // Khi mới đăng ký thì gửi null, khi giảng viên chấm điểm thì gửi số
}