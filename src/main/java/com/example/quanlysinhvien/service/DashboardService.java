package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.dashboard.DashboardResponse;
import com.example.quanlysinhvien.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    public DashboardResponse getOverview() {
        return DashboardResponse.builder()
                .totalDepartments(departmentRepository.countByStatusTrue())
                .totalTeachers(teacherRepository.countByStatusTrue())
                .totalStudents(studentRepository.countByStatusTrue())
                .totalSubjects(subjectRepository.countByStatusTrue())
                .totalEnrollments(enrollmentRepository.count()) // Đếm tất cả bảng điểm
                .build();
    }
}