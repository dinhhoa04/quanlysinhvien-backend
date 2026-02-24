package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // 1. Lấy danh sách sinh viên chưa bị xóa
    List<Student> findByStatusTrue();

    // 2. Kiểm tra xem Mã sinh viên đã tồn tại chưa (Dùng để chặn lúc thêm mới)
    boolean existsByStudentCode(String studentCode);

    // 3. Tìm sinh viên theo Mã sinh viên (Sẽ rất hữu ích sau này)
    Optional<Student> findByStudentCode(String studentCode);
    long countByStatusTrue(); // Đếm tổng số Sinh viên
}