package com.example.quanlysinhvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "enrollments")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. Nối với bảng Student (Khóa ngoại)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // 2. Nối với bảng Subject (Khóa ngoại)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    // Điểm số (Dùng kiểu Double (Object) thay vì double (nguyên thủy) để có thể gán giá trị null khi sinh viên mới đăng ký, chưa có điểm)
    @Column(name = "score")
    private Double score;

    // Học kỳ (Ví dụ: "Học kỳ 1 - 2024")
    @Column(name = "semester", length = 50)
    private String semester;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
}