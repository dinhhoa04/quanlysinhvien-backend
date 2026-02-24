package com.example.quanlysinhvien.dto.subject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectResponse {
    private Long id;
    private String subjectCode;
    private String name;
    private Integer credits;
    private Boolean status;
}