package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.enrollment.EnrollmentRequest;
import com.example.quanlysinhvien.dto.enrollment.EnrollmentResponse;
import com.example.quanlysinhvien.entity.Enrollment;
import com.example.quanlysinhvien.entity.Student;
import com.example.quanlysinhvien.entity.Subject;
import com.example.quanlysinhvien.repository.EnrollmentRepository;
import com.example.quanlysinhvien.repository.StudentRepository;
import com.example.quanlysinhvien.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    // Chuyển Entity sang DTO (Lôi tên Sinh viên và tên Môn học ra để dễ nhìn)
    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentCode(enrollment.getStudent().getStudentCode())
                .studentName(enrollment.getStudent().getFullName())
                .subjectCode(enrollment.getSubject().getSubjectCode())
                .subjectName(enrollment.getSubject().getName())
                .semester(enrollment.getSemester())
                .score(enrollment.getScore())
                .build();
    }

    // Lấy tất cả bảng điểm
    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Lấy bảng điểm của 1 sinh viên cụ thể
    public List<EnrollmentResponse> getEnrollmentsByStudent(Long studentId) {
        return enrollmentRepository.findByStudentId(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ĐĂNG KÝ MÔN HỌC (Tạo mới)
    public EnrollmentResponse enrollSubject(EnrollmentRequest request) {
        // Kiểm tra xem đã đăng ký môn này chưa để tránh đăng ký đúp
        if (enrollmentRepository.existsByStudentIdAndSubjectId(request.getStudentId(), request.getSubjectId())) {
            throw new RuntimeException("Sinh viên đã đăng ký môn học này rồi!");
        }

        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

        Enrollment enrollment = new Enrollment();
        enrollment.setStudent(student);
        enrollment.setSubject(subject);
        enrollment.setSemester(request.getSemester());
        enrollment.setScore(request.getScore()); // Thường là null khi mới đăng ký, chờ giảng viên chấm

        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);
        return mapToResponse(savedEnrollment);
    }

    // NHẬP ĐIỂM / CẬP NHẬT
    public EnrollmentResponse updateScore(Long id, EnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi đăng ký học"));

        // Chỉ cho phép cập nhật điểm và học kỳ (Không cho đổi môn học hay sinh viên khác)
        enrollment.setScore(request.getScore());
        enrollment.setSemester(request.getSemester());

        Enrollment updatedEnrollment = enrollmentRepository.save(enrollment);
        return mapToResponse(updatedEnrollment);
    }

    // HỦY ĐĂNG KÝ (Xóa cứng luôn, vì bảng trung gian này không quan trọng việc xóa mềm)
    public void deleteEnrollment(Long id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi đăng ký học"));
        enrollmentRepository.delete(enrollment);
    }
}