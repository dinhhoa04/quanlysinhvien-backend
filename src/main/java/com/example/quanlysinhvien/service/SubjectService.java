package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.subject.SubjectRequest;
import com.example.quanlysinhvien.dto.subject.SubjectResponse;
import com.example.quanlysinhvien.entity.Subject;
import com.example.quanlysinhvien.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    // Hàm chuyển đổi từ Entity sang DTO
    private SubjectResponse mapToResponse(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .subjectCode(subject.getSubjectCode())
                .name(subject.getName())
                .credits(subject.getCredits())
                .status(subject.getStatus())
                .build();
    }

    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findByStatusTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học với ID: " + id));
        return mapToResponse(subject);
    }

    // THÊM MỚI MÔN HỌC
    public SubjectResponse createSubject(SubjectRequest request) {
        // Kiểm tra trùng Mã môn học
        if (subjectRepository.existsBySubjectCode(request.getSubjectCode())) {
            throw new RuntimeException("Mã môn học đã tồn tại!");
        }

        Subject subject = new Subject();
        subject.setSubjectCode(request.getSubjectCode());
        subject.setName(request.getName());
        subject.setCredits(request.getCredits());

        Subject savedSubject = subjectRepository.save(subject);
        return mapToResponse(savedSubject);
    }

    // CẬP NHẬT MÔN HỌC
    public SubjectResponse updateSubject(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

        // Nếu đổi mã môn học, phải check xem mã mới có bị trùng với môn khác không
        if (!subject.getSubjectCode().equals(request.getSubjectCode())
                && subjectRepository.existsBySubjectCode(request.getSubjectCode())) {
            throw new RuntimeException("Mã môn học mới đã bị trùng với môn khác!");
        }

        subject.setSubjectCode(request.getSubjectCode());
        subject.setName(request.getName());
        subject.setCredits(request.getCredits());

        Subject updatedSubject = subjectRepository.save(subject);
        return mapToResponse(updatedSubject);
    }

    // XÓA MỀM MÔN HỌC
    public void deleteSubject(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy môn học"));

        subject.setStatus(false);
        subjectRepository.save(subject);
    }
}