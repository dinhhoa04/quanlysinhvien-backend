package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.student.StudentRequest;
import com.example.quanlysinhvien.dto.student.StudentResponse;
import com.example.quanlysinhvien.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Xem danh sách (Cả Admin và Giảng viên đều xem được)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<StudentResponse>>builder()
                .status(200)
                .message("Lấy danh sách sinh viên thành công")
                .data(studentService.getAllStudents())
                .build());
    }

    // Xem chi tiết 1 sinh viên (Cả Admin và Giảng viên đều xem được)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<StudentResponse>builder()
                .status(200)
                .message("Lấy thông tin sinh viên thành công")
                .data(studentService.getStudentById(id))
                .build());
    }

    // Thêm mới (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<StudentResponse>> create(@RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.<StudentResponse>builder()
                .status(201)
                .message("Thêm mới sinh viên và tạo tài khoản thành công")
                .data(studentService.createStudent(request))
                .build());
    }

    // Cập nhật (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentResponse>> update(@PathVariable Long id, @RequestBody StudentRequest request) {
        return ResponseEntity.ok(ApiResponse.<StudentResponse>builder()
                .status(200)
                .message("Cập nhật thông tin sinh viên thành công")
                .data(studentService.updateStudent(id, request))
                .build());
    }

    // Xóa (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Đã xóa sinh viên và khóa tài khoản thành công")
                .data(null)
                .build());
    }
}