package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.subject.SubjectRequest;
import com.example.quanlysinhvien.dto.subject.SubjectResponse;
import com.example.quanlysinhvien.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subjects")
public class SubjectController {

    @Autowired
    private SubjectService subjectService;

    // Xem danh sách (Tất cả mọi người đều xem được)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<SubjectResponse>>builder()
                .status(200)
                .message("Lấy danh sách môn học thành công")
                .data(subjectService.getAllSubjects())
                .build());
    }

    // Xem chi tiết 1 môn (Tất cả mọi người đều xem được)
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_TEACHER', 'ROLE_STUDENT')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.<SubjectResponse>builder()
                .status(200)
                .message("Lấy thông tin môn học thành công")
                .data(subjectService.getSubjectById(id))
                .build());
    }

    // Thêm mới (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> create(@RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.<SubjectResponse>builder()
                .status(201)
                .message("Thêm mới môn học thành công")
                .data(subjectService.createSubject(request))
                .build());
    }

    // Cập nhật (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> update(@PathVariable Long id, @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(ApiResponse.<SubjectResponse>builder()
                .status(200)
                .message("Cập nhật môn học thành công")
                .data(subjectService.updateSubject(id, request))
                .build());
    }

    // Xóa (CHỈ ADMIN)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .status(200)
                .message("Đã xóa môn học thành công")
                .data(null)
                .build());
    }
}