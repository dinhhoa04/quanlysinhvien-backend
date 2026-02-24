package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    // 1. Lấy toàn bộ bảng điểm của 1 sinh viên (Dành cho Sinh viên xem điểm)
    List<Enrollment> findByStudentId(Long studentId);

    // 2. Lấy toàn bộ danh sách sinh viên đang học 1 môn (Dành cho Giảng viên xem lớp)
    List<Enrollment> findBySubjectId(Long subjectId);

    // 3. Kiểm tra xem sinh viên đã đăng ký môn này chưa (Chặn đăng ký 2 lần)
    boolean existsByStudentIdAndSubjectId(Long studentId, Long subjectId);
}