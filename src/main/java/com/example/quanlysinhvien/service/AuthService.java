package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.auth.ChangePasswordRequest;
import com.example.quanlysinhvien.dto.auth.LoginRequest;
import com.example.quanlysinhvien.dto.auth.UserProfileResponse;
import com.example.quanlysinhvien.entity.User;
import com.example.quanlysinhvien.repository.UserRepository;
import com.example.quanlysinhvien.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service // Đánh dấu đây là tầng Logic của Spring Boot
public class AuthService {

    // AuthenticationManager: Công cụ của Spring Security giúp đối chiếu username/password
    @Autowired
    private AuthenticationManager authenticationManager;

    // JwtUtils: Class tự viết (sắp làm) để tạo chuỗi Token
    @Autowired
    private JwtUtils jwtUtils;

    // UserRepository: Gọi xuống database để tìm User
    @Autowired
    private UserRepository userRepository;

    // PasswordEncoder: Công cụ mã hóa/giải mã mật khẩu (BCrypt)
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Hàm xử lý đăng nhập
    public String authenticateAndGenerateToken(LoginRequest request) {
        // Giao việc kiểm tra tài khoản/mật khẩu cho Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // Nếu qua được dòng trên (không báo lỗi), nghĩa là pass đúng.
        // Ta lưu trạng thái "đã đăng nhập" vào hệ thống
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Nhờ JwtUtils tạo một chuỗi mã hóa và trả về cho Controller
        return jwtUtils.generateJwtToken(authentication);
    }

    // 2. Hàm lấy thông tin cá nhân
    public UserProfileResponse getUserProfile(String username) {
        // Tìm user trong database, nếu không có thì ném ra lỗi
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản: " + username));

        // Gom dữ liệu vào DTO để trả về
        UserProfileResponse profile = new UserProfileResponse();
        profile.setId(user.getId());
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());

        // Lấy quyền đầu tiên của User này gán vào profile
        if (!user.getRoles().isEmpty()) {
            profile.setRole(user.getRoles().iterator().next().getName());
        }

        return profile;
    }

    // 3. Hàm đổi mật khẩu
    public void changeUserPassword(String username, ChangePasswordRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản"));

        // Dùng PasswordEncoder kiểm tra xem mật khẩu cũ (người dùng nhập)
        // có khớp với chuỗi đã mã hóa trong database không
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không chính xác!");
        }

        // Nếu khớp, mã hóa mật khẩu mới và lưu xuống DB
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}