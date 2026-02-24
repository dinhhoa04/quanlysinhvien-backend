package com.example.quanlysinhvien.dto.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {
    private long totalDepartments;
    private long totalTeachers;
    private long totalStudents;
    private long totalSubjects;
    private long totalEnrollments;
}