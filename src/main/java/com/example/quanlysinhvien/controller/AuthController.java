package com.example.quanlysinhvien.controller;

import com.example.quanlysinhvien.dto.ApiResponse;
import com.example.quanlysinhvien.dto.auth.ChangePasswordRequest;
import com.example.quanlysinhvien.dto.auth.LoginRequest;
import com.example.quanlysinhvien.dto.auth.TokenResponse;
import com.example.quanlysinhvien.dto.auth.UserProfileResponse;
import com.example.quanlysinhvien.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

// @RestController: Báo cho Spring Boot biết đây là nơi tiếp nhận API.
// Nó sẽ tự động chuyển đổi kết quả trả về (đối tượng Java) thành định dạng JSON cho ReactJS hiểu.
@RestController

// @RequestMapping: Cấu hình đường dẫn gốc cho toàn bộ class này.
// Tất cả các API trong file này đều sẽ bắt đầu bằng "/api/v1/auth"
@RequestMapping("/api/v1/auth")
public class AuthController {

    // @Autowired: Tự động "tiêm" (inject) AuthService vào đây để Controller có thể
    // gọi các hàm xử lý logic (đăng nhập, đổi mật khẩu...) từ tầng Service.
    @Autowired
    private AuthService authService;

    // 1. API Đăng nhập
    // Sử dụng @PostMapping vì ReactJS sẽ gửi thông tin (username, password) ngầm (ẩn) lên server.
    // Đường dẫn đầy đủ sẽ là: POST http://localhost:8080/api/v1/auth/login
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@RequestBody LoginRequest request) {
        // @RequestBody: Lấy dữ liệu JSON từ ReactJS gửi lên và "nhét" vào object LoginRequest

        // Gọi Service xử lý đăng nhập và lấy chuỗi Token
        String token = authService.authenticateAndGenerateToken(request);

        // Đóng gói kết quả trả về theo chuẩn ApiResponse mà chúng ta đã thống nhất
        ApiResponse<TokenResponse> response = ApiResponse.<TokenResponse>builder()
                .status(200)
                .message("Đăng nhập thành công")
                .data(new TokenResponse(token))
                .build();

        // ResponseEntity.ok(): Trả về HTTP Status Code 200 (Thành công)
        return ResponseEntity.ok(response);
    }

    // 2. API Lấy thông tin cá nhân
    // Sử dụng @GetMapping vì ta chỉ cần "lấy" dữ liệu về, không gửi dữ liệu gì lên.
    // Đường dẫn đầy đủ: GET http://localhost:8080/api/v1/auth/profile
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile(Principal principal) {
        // Principal: Chứa thông tin của người dùng ĐANG ĐĂNG NHẬP (được trích xuất từ chuỗi Token).
        // Hàm principal.getName() sẽ lấy ra username của người đó.

        UserProfileResponse userProfile = authService.getUserProfile(principal.getName());

        ApiResponse<UserProfileResponse> response = ApiResponse.<UserProfileResponse>builder()
                .status(200)
                .message("Lấy thông tin thành công")
                .data(userProfile)
                .build();

        return ResponseEntity.ok(response);
    }

    // 3. API Đổi mật khẩu
    // Sử dụng @PutMapping vì hành động này mang tính chất "cập nhật" dữ liệu đã có.
    // Đường dẫn đầy đủ: PUT http://localhost:8080/api/v1/auth/password
    @PutMapping("/password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestBody ChangePasswordRequest request,
            Principal principal) {

        // Truyền username của người đang đăng nhập và yêu cầu đổi mật khẩu xuống tầng Service
        authService.changeUserPassword(principal.getName(), request);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(200)
                .message("Đổi mật khẩu thành công")
                .data(null) // Đổi pass xong thì không cần trả kèm dữ liệu gì thêm
                .build();

        return ResponseEntity.ok(response);
    }
}