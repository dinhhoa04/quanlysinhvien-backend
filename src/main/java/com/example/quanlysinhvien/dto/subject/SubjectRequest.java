package com.example.quanlysinhvien.dto.subject;
import lombok.Data;

@Data
public class SubjectRequest {
    private String subjectCode;
    private String name;
    private Integer credits;
}