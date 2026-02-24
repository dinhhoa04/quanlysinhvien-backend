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
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Mã sinh viên: Bắt buộc nhập và không được trùng nhau
    @Column(name = "student_code", nullable = false, unique = true, length = 20)
    private String studentCode;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    // Ngày sinh (Chỉ lưu ngày tháng năm, không lưu giờ phút)
    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "class_name", length = 50)
    private String className;

    @Column(name = "status")
    private Boolean status = true;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    // -----------------------------------------------------
    // KHÓA NGOẠI
    // -----------------------------------------------------

    // 1. Nối với bảng Department (Nhiều Sinh viên thuộc 1 Khoa)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    // 2. Nối với bảng User (1 Sinh viên có 1 Tài khoản đăng nhập)
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}