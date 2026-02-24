package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    // 1. Lấy danh sách các khoa chưa bị xóa (status = true)
    List<Department> findByStatusTrue();

    // 2. Kiểm tra xem tên khoa đã tồn tại chưa (để tránh Admin tạo 2 khoa trùng tên)
    boolean existsByName(String name);
    long countByStatusTrue(); // Đếm tổng số Khoa đang hoạt động
}