package com.example.quanlysinhvien.service;

import com.example.quanlysinhvien.dto.teacher.TeacherRequest;
import com.example.quanlysinhvien.dto.teacher.TeacherResponse;
import com.example.quanlysinhvien.entity.Department;
import com.example.quanlysinhvien.entity.Role;
import com.example.quanlysinhvien.entity.Teacher;
import com.example.quanlysinhvien.entity.User;
import com.example.quanlysinhvien.repository.DepartmentRepository;
import com.example.quanlysinhvien.repository.RoleRepository;
import com.example.quanlysinhvien.repository.TeacherRepository;
import com.example.quanlysinhvien.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hàm chuyển từ Entity sang DTO
    private TeacherResponse mapToResponse(Teacher teacher) {
        return TeacherResponse.builder()
                .id(teacher.getId())
                .fullName(teacher.getFullName())
                .phone(teacher.getPhone())
                .departmentName(teacher.getDepartment().getName()) // Lấy tên khoa từ khóa ngoại
                .username(teacher.getUser().getUsername())         // Lấy username từ khóa ngoại
                .email(teacher.getUser().getEmail())
                .status(teacher.getStatus())
                .build();
    }

    public List<TeacherResponse> getAllTeachers() {
        return teacherRepository.findByStatusTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public TeacherResponse getTeacherById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên với ID: " + id));
        return mapToResponse(teacher);
    }

    // THÊM MỚI GIẢNG VIÊN (Dùng @Transactional để đảm bảo toàn vẹn dữ liệu)
    @Transactional
    public TeacherResponse createTeacher(TeacherRequest request) {
        // 1. Kiểm tra tài khoản/email trùng lặp
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        // 2. Kiểm tra xem Khoa (Department) có tồn tại không
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khoa với ID: " + request.getDepartmentId()));

        // 3. TẠO TÀI KHOẢN (USER) TRƯỚC
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu
        user.setEmail(request.getEmail());

        // Lấy quyền ROLE_TEACHER gán cho user này
        Role teacherRole = roleRepository.findByName("ROLE_TEACHER")
                .orElseThrow(() -> new RuntimeException("Hệ thống chưa có quyền ROLE_TEACHER"));
        user.getRoles().add(teacherRole);

        // Lưu User xuống database để có ID
        User savedUser = userRepository.save(user);

        // 4. TẠO GIẢNG VIÊN (TEACHER) VÀ NỐI KHÓA NGOẠI
        Teacher teacher = new Teacher();
        teacher.setFullName(request.getFullName());
        teacher.setPhone(request.getPhone());
        teacher.setDepartment(department); // Gắn khóa ngoại Department
        teacher.setUser(savedUser);        // Gắn khóa ngoại User

        // Lưu Teacher xuống database
        Teacher savedTeacher = teacherRepository.save(teacher);

        return mapToResponse(savedTeacher);
    }

    @Transactional
    public TeacherResponse updateTeacher(Long id, TeacherRequest request) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy Khoa"));

        // Cập nhật thông tin giảng viên
        teacher.setFullName(request.getFullName());
        teacher.setPhone(request.getPhone());
        teacher.setDepartment(department);

        // Cập nhật email của tài khoản (Nếu email thay đổi thì kiểm tra xem có bị trùng không)
        User user = teacher.getUser();
        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã có người khác sử dụng!");
        }
        user.setEmail(request.getEmail());
        userRepository.save(user); // Lưu lại User

        Teacher updatedTeacher = teacherRepository.save(teacher);
        return mapToResponse(updatedTeacher);
    }

    @Transactional
    public void deleteTeacher(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giảng viên"));

        // 1. Xóa mềm giảng viên
        teacher.setStatus(false);
        teacherRepository.save(teacher);

        // 2. Khóa luôn tài khoản đăng nhập của giảng viên đó
        User user = teacher.getUser();
        user.setStatus(false);
        userRepository.save(user);
    }
}