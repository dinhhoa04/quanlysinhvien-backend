package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.dashboard.DashboardResponse;
import com.example.quanlysinhvien.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Lấy dữ liệu tổng quan (Chỉ Admin)
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/overview")
    public ResponseEntity<ApiResponse<DashboardResponse>> getOverview() {
        return ResponseEntity.ok(ApiResponse.<DashboardResponse>builder()
                .status(200)
                .message("Lấy dữ liệu thống kê tổng quan thành công")
                .data(dashboardService.getOverview())
                .build());
    }
}