package com.example.quanlysinhvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "status")
    private Boolean status = true; // Mặc định tài khoản luôn Active khi mới tạo

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    // CHÚ Ý PHẦN NÀY: Cấu hình quan hệ N-N (Nhiều User có Nhiều Role)
    @ManyToMany(fetch = FetchType.EAGER) // EAGER: Lấy thông tin User là lấy luôn quyền của họ lên
    @JoinTable(
            name = "user_roles", // Tên bảng trung gian trong SQL
            joinColumns = @JoinColumn(name = "user_id"), // Khóa ngoại trỏ về bảng users
            inverseJoinColumns = @JoinColumn(name = "role_id") // Khóa ngoại trỏ về bảng roles
    )
    private Set<Role> roles = new HashSet<>();
    // Dùng Set thay vì List để đảm bảo 1 user không bị gán trùng 1 quyền nhiều lần
}