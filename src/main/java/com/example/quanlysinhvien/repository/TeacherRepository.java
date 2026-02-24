package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    // Lấy danh sách giảng viên chưa bị xóa (status = true)
    List<Teacher> findByStatusTrue();

    // Kiểm tra xem user_id này đã được gắn cho giảng viên nào chưa
    boolean existsByUserId(Long userId);
    long countByStatusTrue(); // Đếm tổng số Giảng viên
}