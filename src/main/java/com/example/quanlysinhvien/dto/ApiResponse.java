package com.example.quanlysinhvien.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @Data: Tự động tạo ra các hàm Getter, Setter, toString() nhờ thư viện Lombok
// @Builder: Giúp tạo object dễ dàng hơn (ví dụ: ApiResponse.builder().status(200).build())
// @NoArgsConstructor: Tự động tạo hàm khởi tạo không tham số
// @AllArgsConstructor: Tự động tạo hàm khởi tạo có đầy đủ tham số
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    // <T> ở đây gọi là Generic. Nó đại diện cho một kiểu dữ liệu linh hoạt.
    // Nếu API trả về 1 chuỗi, T sẽ là String. Nếu trả về 1 Sinh viên, T sẽ là Student.

    private int status;      // Chứa mã trạng thái HTTP (VD: 200 là OK, 400 là lỗi, 403 là cấm)
    private String message;  // Lời nhắn gửi về cho Frontend (VD: "Đăng nhập thành công")
    private T data;          // Dữ liệu thực tế trả về. Kiểu dữ liệu sẽ thay đổi linh hoạt nhờ <T>
}

