package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.department.DepartmentRequest;
import com.example.quanlysinhvien.dto.department.DepartmentResponse;
import com.example.quanlysinhvien.entity.Department;
import com.example.quanlysinhvien.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    // Hàm phụ: Chuyển đổi từ Entity (Database) sang DTO (Trả về)
    private DepartmentResponse mapToResponse(Department dept) {
        return DepartmentResponse.builder()
                .id(dept.getId())
                .name(dept.getName())
                .description(dept.getDescription())
                .status(dept.getStatus())
                .createdAt(dept.getCreatedAt())
                .build();
    }

    // 1. Lấy tất cả Khoa đang hoạt động
    public List<DepartmentResponse> getAllDepartments() {
        return departmentRepository.findByStatusTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // 2. Lấy chi tiết 1 Khoa theo ID
    public DepartmentResponse getDepartmentById(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa với ID: " + id));
        return mapToResponse(dept);
    }

    // 3. Thêm mới Khoa
    public DepartmentResponse createDepartment(DepartmentRequest request) {
        if (departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên khoa đã tồn tại!");
        }

        Department dept = new Department();
        dept.setName(request.getName());
        dept.setDescription(request.getDescription());
        // Trạng thái (status) mặc định là true theo thiết kế Entity

        Department savedDept = departmentRepository.save(dept);
        return mapToResponse(savedDept);
    }

    // 4. Cập nhật Khoa
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa với ID: " + id));

        // Nếu tên bị đổi sang một tên khác đã có trong database thì báo lỗi
        if (!dept.getName().equals(request.getName()) && departmentRepository.existsByName(request.getName())) {
            throw new RuntimeException("Tên khoa đã tồn tại!");
        }

        dept.setName(request.getName());
        dept.setDescription(request.getDescription());

        Department updatedDept = departmentRepository.save(dept);
        return mapToResponse(updatedDept);
    }

    // 5. Xóa mềm (Soft Delete)
    public void deleteDepartment(Long id) {
        Department dept = departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khoa với ID: " + id));

        dept.setStatus(false); // Chuyển trạng thái thành Inactive
        departmentRepository.save(dept);
    }
}