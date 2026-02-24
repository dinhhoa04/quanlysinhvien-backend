package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository // Báo cho Spring biết đây là component giao tiếp với Database
public interface RoleRepository extends JpaRepository<Role, Long> {
    // JpaRepository<Role, Long>:
    // - Role: Tên Entity sẽ làm việc
    // - Long: Kiểu dữ liệu của Khóa chính (cột id)

    // Khai báo thêm hàm tìm quyền theo tên (Spring Data JPA tự động viết SQL cho hàm này)
    // Dùng Optional để tránh lỗi NullPointerException nếu không tìm thấy quyền đó
    Optional<Role> findByName(String name);
}