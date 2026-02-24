package com.example.quanlysinhvien.repository;

import com.example.quanlysinhvien.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 1. Tìm User dựa vào username (dùng khi đăng nhập để kiểm tra mật khẩu)
    Optional<User> findByUsername(String username);

    // 2. Kiểm tra xem username đã tồn tại chưa (dùng khi đăng ký/tạo mới tài khoản để tránh trùng lặp)
    Boolean existsByUsername(String username);

    // 3. Kiểm tra xem email đã tồn tại chưa
    Boolean existsByEmail(String email);
}