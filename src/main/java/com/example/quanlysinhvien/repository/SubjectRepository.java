package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByStatusTrue();
    boolean existsBySubjectCode(String subjectCode);
    long countByStatusTrue(); // Đếm tổng số Môn học
}