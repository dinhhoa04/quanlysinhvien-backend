package com.example.quanlysinhvien.exception;

import com.example.quanlysinhvien.dto.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Hàm này sẽ tự động "chụp" tất cả các RuntimeException được ném ra từ Service
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException ex) {

        // Đóng gói lỗi vào cái khung ApiResponse quen thuộc của chúng ta
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(400) // 400 Bad Request: Báo hiệu lỗi do phía người dùng nhập sai
                .message(ex.getMessage()) // Lấy đúng câu tiếng Việt mình đã viết trong Service
                .data(null)
                .build();

        return ResponseEntity.badRequest().body(response);
    }

    // Sau này nếu muốn bắt thêm lỗi khác (ví dụ: lỗi Token, lỗi Database...),
    // bạn chỉ cần viết thêm hàm @ExceptionHandler(...) ở dưới đây.
}