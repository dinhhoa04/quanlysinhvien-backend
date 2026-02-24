package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.enrollment.EnrollmentRequest;
import com.example.quanlysinhvien.dto.enrollment.EnrollmentResponse;
import com.example.quanlysinhvien.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // Xem tất cả bảng điểm (Chỉ Admin và Giảng viên)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<EnrollmentResponse>>builder()
                .status(200)
                .message("Lấy danh sách đăng ký thành công")
                .data(enrollmentService.getAllEnrollments())
                .build());
    }

    // Xem điểm của 1 sinh viên (Cả 3 Role đều được xem)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(ApiResponse.<List<EnrollmentResponse>>builder()
                .status(200)
                .message("Lấy bảng điểm cá nhân thành công")
                .data(enrollmentService.getEnrollmentsByStudent(studentId))
                .build());
    }

    // Đăng ký môn học (Sinh viên tự đăng ký hoặc Admin đăng ký hộ)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_STUDENT')")
    @PostMapping
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enroll(@RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(ApiResponse.<EnrollmentResponse>builder()
                .status(201)
                .message("Đăng ký môn học thành công")
                .data(enrollmentService.enrollSubject(request))
                .build());
    }

    // Nhập điểm (Chỉ Giảng viên và Admin được chấm điểm)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> updateScore(@PathVariable Long id, @RequestBody EnrollmentRequest request) {
        return ResponseEntity.ok(ApiResponse.<EnrollmentResponse>builder()
                .status(200)
                .message("Cập nhật điểm thành công")
                .data(enrollmentService.updateScore(id, request))
                .build());
    }

    // Rút môn / Xóa đăng ký (Chỉ Admin)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Hủy đăng ký môn học thành công")
                .data(null)
                .build());
    }
}