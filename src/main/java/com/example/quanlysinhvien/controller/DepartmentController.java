package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.department.DepartmentRequest;
import com.example.quanlysinhvien.dto.department.DepartmentResponse;
import com.example.quanlysinhvien.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    // 1. Lấy danh sách (Ai đăng nhập rồi cũng xem được)
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getAll() {
        // Chỗ này có 2 dấu >> là ĐÚNG, vì là <List<DepartmentResponse>>
        return ResponseEntity.ok(ApiResponse.<List<DepartmentResponse>>builder()
                .status(200)
                .message("Lấy danh sách khoa thành công")
                .data(departmentService.getAllDepartments())
                .build());
    }

    // 2. Xem chi tiết (Ai đăng nhập rồi cũng xem được)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getById(@PathVariable Long id) {
        // Đã sửa lại thành 1 dấu >
        return ResponseEntity.ok(ApiResponse.<DepartmentResponse>builder()
                .status(200)
                .message("Lấy thông tin khoa thành công")
                .data(departmentService.getDepartmentById(id))
                .build());
    }

    // 3. Thêm mới (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentResponse>> create(@RequestBody DepartmentRequest request) {
        // Đã sửa lại thành 1 dấu >
        return ResponseEntity.ok(ApiResponse.<DepartmentResponse>builder()
                .status(201)
                .message("Thêm khoa mới thành công")
                .data(departmentService.createDepartment(request))
                .build());
    }

    // 4. Cập nhật (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentResponse>> update(@PathVariable Long id, @RequestBody DepartmentRequest request) {
        // Đã sửa lại thành 1 dấu >
        return ResponseEntity.ok(ApiResponse.<DepartmentResponse>builder()
                .status(200)
                .message("Cập nhật khoa thành công")
                .data(departmentService.updateDepartment(id, request))
                .build());
    }

    // 5. Xóa mềm (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Xóa khoa thành công")
                .data(null)
                .build());
    }
}