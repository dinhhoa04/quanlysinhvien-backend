package com.example.quanlysinhvien.dto.enrollment;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EnrollmentResponse {
    private Long id;

    // Thông tin sinh viên
    private String studentCode;
    private String studentName;

    // Thông tin môn học
    private String subjectCode;
    private String subjectName;

    private String semester;
    private Double score;
}