package com.example.quanlysinhvien.dto.teacher;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherResponse {
    private Long id;
    private String fullName;
    private String phone;

    // Thay vì trả về ID khoa, ta trả về hẳn Tên Khoa để Frontend dễ in ra màn hình
    private String departmentName;

    // Trả về thông tin tài khoản cơ bản
    private String username;
    private String email;

    private Boolean status;
}