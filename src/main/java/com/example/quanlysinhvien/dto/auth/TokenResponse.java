package com.example.quanlysinhvien.dto.auth;

import lombok.Data;

@Data
public class TokenResponse {
    // Chức năng: Sau khi đăng nhập thành công, Backend tạo ra 1 chuỗi mã hóa (JWT).
    // Class này dùng để gói chuỗi đó lại và gửi về cho ReactJS lưu vào localStorage.

    private String accessToken; // Chứa chuỗi mã hóa JWT
    private String tokenType = "Bearer"; // Loại token chuẩn thường dùng là Bearer

    // Hàm khởi tạo để gán token nhanh
    public TokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}