package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.student.StudentRequest;
import com.example.quanlysinhvien.dto.student.StudentResponse;
import com.example.quanlysinhvien.entity.Department;
import com.example.quanlysinhvien.entity.Role;
import com.example.quanlysinhvien.entity.Student;
import com.example.quanlysinhvien.entity.User;
import com.example.quanlysinhvien.repository.DepartmentRepository;
import com.example.quanlysinhvien.repository.RoleRepository;
import com.example.quanlysinhvien.repository.StudentRepository;
import com.example.quanlysinhvien.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm chuyển Entity -> DTO
    private StudentResponse mapToResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .dob(student.getDob())
                .className(student.getClassName())
                .departmentName(student.getDepartment().getName())
                .username(student.getUser().getUsername())
                .email(student.getUser().getEmail())
                .status(student.getStatus())
                .build();
    }

    public List<StudentResponse> getAllStudents() {
        return studentRepository.findByStatusTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public StudentResponse getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với ID: " + id));
        return mapToResponse(student);
    }

    // THÊM MỚI SINH VIÊN
    @Transactional
    public StudentResponse createStudent(StudentRequest request) {
        // 1. Kiểm tra trùng lặp mã sinh viên, username, email
        if (studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên đã tồn tại!");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // 2. Tìm Khoa
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khoa"));

        // 3. Tạo User (Tài khoản) trước
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        // Cấp quyền ROLE_STUDENT
        Role studentRole = roleRepository.findByName("ROLE_STUDENT")
                .orElseThrow(() -> new RuntimeException("Hệ thống chưa có quyền ROLE_STUDENT"));
        user.getRoles().add(studentRole);

        User savedUser = userRepository.save(user);

        // 4. Tạo Student và gắn khóa ngoại
        Student student = new Student();
        student.setStudentCode(request.getStudentCode());
        student.setFullName(request.getFullName());
        student.setDob(request.getDob());
        student.setClassName(request.getClassName());
        student.setDepartment(department); // Nối với Khoa
        student.setUser(savedUser);        // Nối với Tài khoản

        Student savedStudent = studentRepository.save(student);

        return mapToResponse(savedStudent);
    }

    // CẬP NHẬT SINH VIÊN
    @Transactional
    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khoa"));

        // Nếu đổi mã sinh viên, phải check xem có trùng với người khác không
        if (!student.getStudentCode().equals(request.getStudentCode())
                && studentRepository.existsByStudentCode(request.getStudentCode())) {
            throw new RuntimeException("Mã sinh viên mới đã bị trùng với người khác!");
        }

        student.setStudentCode(request.getStudentCode());
        student.setFullName(request.getFullName());
        student.setDob(request.getDob());
        student.setClassName(request.getClassName());
        student.setDepartment(department);

        // Xử lý cập nhật Email cho User
        User user = student.getUser();
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã có người khác sử dụng!");
        }
        user.setEmail(request.getEmail());
        userRepository.save(user);

        Student updatedStudent = studentRepository.save(student);
        return mapToResponse(updatedStudent);
    }

    // XÓA MỀM SINH VIÊN
    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên"));

        student.setStatus(false);
        studentRepository.save(student);

        // Khóa luôn tài khoản đăng nhập
        User user = student.getUser();
        user.setStatus(false);
        userRepository.save(user);
    }
}