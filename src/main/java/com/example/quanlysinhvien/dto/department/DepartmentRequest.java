package com.example.quanlysinhvien.dto.department;

import lombok.Data;

@Data
public class DepartmentRequest {
    // Chỉ cần 2 trường này vì ID tự tăng, còn status mặc định là true rồi
    private String name;
    private String description;
}