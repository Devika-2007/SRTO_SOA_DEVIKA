package com.student.controller;

import com.student.config.SecurityConfig.*;
import com.student.dto.StudentDtos.*;
import com.student.entity.StudentEntities.*;
import com.student.service.StudentServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StudentControllers {

    private final AuthService authService;
    private final StudentService studentService;
    private final AttendanceService attendanceService;
    private final ResultService resultService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // --- Auth Endpoints ---
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        return authService.findByUsername(request.getUsername())
                .filter(u -> passwordEncoder.matches(request.getPassword(), u.getPassword()))
                .map(u -> ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(u.getUsername(), u.getRole()), u.getUsername(), u.getRole())))
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PostMapping("/auth/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    // --- Student Endpoints ---
    @GetMapping("/students")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/students")
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.saveStudent(student));
    }

    @PutMapping("/students/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentService.getStudentById(id)
                .map(existing -> {
                    student.setId(existing.getId());
                    return ResponseEntity.ok(studentService.saveStudent(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/students/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    // --- Attendance Endpoints ---
    @PostMapping("/attendance")
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attendanceService.markAttendance(attendance));
    }

    @GetMapping("/attendance/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.getAttendanceByStudent(studentId));
    }

    @GetMapping("/attendance/student/{studentId}/percentage")
    public ResponseEntity<Map<String, Object>> getAttendancePercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceService.calculatePercentage(studentId));
    }

    // --- Result Endpoints ---
    @PostMapping("/results")
    public ResponseEntity<Result> addResult(@RequestBody Result result) {
        return ResponseEntity.status(HttpStatus.CREATED).body(resultService.addResult(result));
    }

    @GetMapping("/results/student/{studentId}")
    public ResponseEntity<List<Result>> getResultsByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(resultService.getResultsByStudent(studentId));
    }
}
