package com.student.service;

import com.student.entity.StudentEntities.*;
import com.student.repository.StudentRepositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

public class StudentServices {

    public interface AuthService {
        User registerUser(User user);
        Optional<User> findByUsername(String username);
    }

    public interface StudentService {
        List<Student> getAllStudents();
        Optional<Student> getStudentById(Long id);
        Student saveStudent(Student student);
        void deleteStudent(Long id);
    }

    public interface AttendanceService {
        Attendance markAttendance(Attendance attendance);
        List<Attendance> getAttendanceByStudent(Long studentId);
        Map<String, Object> calculatePercentage(Long studentId);
    }

    public interface ResultService {
        Result addResult(Result result);
        List<Result> getResultsByStudent(Long studentId);
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class AuthServiceImpl implements AuthService {
        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public User registerUser(User user) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            if (user.getRole() == null) user.setRole("STUDENT");
            return userRepository.save(user);
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return userRepository.findByUsername(username);
        }
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class StudentServiceImpl implements StudentService {
        private final StudentRepository studentRepository;

        @Override public List<Student> getAllStudents() { return studentRepository.findAll(); }
        @Override public Optional<Student> getStudentById(Long id) { return studentRepository.findById(id); }
        @Override public Student saveStudent(Student s) { return studentRepository.save(s); }
        @Override public void deleteStudent(Long id) { studentRepository.deleteById(id); }
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class AttendanceServiceImpl implements AttendanceService {
        private final AttendanceRepository attendanceRepository;

        @Override
        public Attendance markAttendance(Attendance attendance) {
            if (attendance.getDate() == null) attendance.setDate(LocalDate.now());
            return attendanceRepository.save(attendance);
        }

        @Override
        public List<Attendance> getAttendanceByStudent(Long studentId) {
            return attendanceRepository.findByStudentId(studentId);
        }

        @Override
        public Map<String, Object> calculatePercentage(Long studentId) {
            List<Attendance> records = attendanceRepository.findByStudentId(studentId);
            long total = records.size();
            long present = records.stream().filter(a -> "PRESENT".equalsIgnoreCase(a.getStatus())).count();
            double percentage = total > 0 ? ((double) present / total) * 100 : 0.0;

            Map<String, Object> stats = new HashMap<>();
            stats.put("studentId", studentId);
            stats.put("totalClasses", total);
            stats.put("attendedClasses", present);
            stats.put("attendancePercentage", Math.round(percentage * 100.0) / 100.0);
            return stats;
        }
    }

    @Service
    @RequiredArgsConstructor
    @Transactional
    public static class ResultServiceImpl implements ResultService {
        private final ResultRepository resultRepository;

        @Override
        public Result addResult(Result result) {
            if (result.getGrade() == null) {
                result.setGrade(calculateGrade(result.getMarksObtained(), result.getMaxMarks()));
            }
            return resultRepository.save(result);
        }

        @Override
        public List<Result> getResultsByStudent(Long studentId) {
            return resultRepository.findByStudentId(studentId);
        }

        private String calculateGrade(int marks, int maxMarks) {
            double pct = ((double) marks / (maxMarks > 0 ? maxMarks : 100)) * 100;
            if (pct >= 90) return "A+";
            if (pct >= 80) return "A";
            if (pct >= 70) return "B+";
            if (pct >= 60) return "B";
            if (pct >= 50) return "C";
            return "F";
        }
    }
}
