package com.example.quanlysinhvien.dto.department;

import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class DepartmentResponse {
    private Long id;
    private String name;
    private String description;
    private Boolean status;
    private Date createdAt;
}