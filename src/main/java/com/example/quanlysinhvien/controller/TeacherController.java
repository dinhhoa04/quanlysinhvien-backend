package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.teacher.TeacherRequest;
import com.example.quanlysinhvien.dto.teacher.TeacherResponse;
import com.example.quanlysinhvien.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    // Xem danh sách (Chỉ Admin mới có quyền quản lý nhân sự)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeacherResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<TeacherResponse>>builder()
                .status(200)
                .message("Lấy danh sách giảng viên thành công")
                .data(teacherService.getAllTeachers())
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<TeacherResponse>builder()
                .status(200)
                .message("Lấy thông tin giảng viên thành công")
                .data(teacherService.getTeacherById(id))
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<TeacherResponse>> create(@RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.<TeacherResponse>builder()
                .status(201)
                .message("Thêm mới giảng viên và tạo tài khoản thành công")
                .data(teacherService.createTeacher(request))
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TeacherResponse>> update(@PathVariable Long id, @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(ApiResponse.<TeacherResponse>builder()
                .status(200)
                .message("Cập nhật thông tin giảng viên thành công")
                .data(teacherService.updateTeacher(id, request))
                .build());
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        teacherService.deleteTeacher(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Đã xóa giảng viên và khóa tài khoản thành công")
                .data(null)
                .build());
    }
}