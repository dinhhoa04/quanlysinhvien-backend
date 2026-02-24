package com.example.quanlysinhvien.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter // Dùng Getter/Setter thay vì @Data cho Entity để tránh lỗi vòng lặp (StackOverflow) khi dùng ManyToMany
@NoArgsConstructor
@AllArgsConstructor
@Entity // Báo cho Spring Boot biết đây là một thực thể database
@Table(name = "roles") // Chỉ định chính xác tên bảng trong MySQL
public class Role {

    @Id // Đánh dấu đây là Khóa chính (Primary Key)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tương đương với AUTO_INCREMENT trong SQL
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP) // Lưu cả ngày và giờ
    private Date createdAt = new Date(); // Mặc định gán thời gian hiện tại khi tạo

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();
}