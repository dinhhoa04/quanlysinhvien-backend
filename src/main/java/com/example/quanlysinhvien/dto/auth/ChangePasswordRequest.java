package com.example.quanlysinhvien.dto.auth;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    // Chức năng: Dùng để hứng mật khẩu cũ và mật khẩu mới khi user muốn đổi pass.

    private String oldPassword;
    private String newPassword;
}