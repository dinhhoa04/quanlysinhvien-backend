package com.example.quanlysinhvien.component;

import com.example.quanlysinhvien.entity.Role;
import com.example.quanlysinhvien.entity.User;
import com.example.quanlysinhvien.repository.RoleRepository;
import com.example.quanlysinhvien.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Tự động tạo 3 quyền cơ bản nếu chưa có
        if (roleRepository.findByName("ROLE_ADMIN").isEmpty()) {
            roleRepository.save(new Role(null, "ROLE_ADMIN", "Quản trị viên", null, null));
            roleRepository.save(new Role(null, "ROLE_TEACHER", "Giảng viên", null, null));
            roleRepository.save(new Role(null, "ROLE_STUDENT", "Sinh viên", null, null));
        }

        // 2. Tự động tạo tài khoản Admin với mật khẩu "123456"
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456")); // Mã hóa mật khẩu
            admin.setEmail("admin@school.edu.vn");

            // Lấy quyền Admin từ DB và gán cho user này
            Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
            System.out.println("Đã tạo thành công tài khoản Admin mặc định!");
        }
    }
}