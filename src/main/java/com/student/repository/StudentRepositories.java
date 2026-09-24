package com.student.repository;

import com.student.entity.StudentEntities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public class StudentRepositories {

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        Optional<User> findByEmail(String email);
    }

    @Repository
    public interface StudentRepository extends JpaRepository<Student, Long> {
        Optional<Student> findByRollNumber(String rollNumber);
        Optional<Student> findByEmail(String email);
    }

    @Repository
    public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
        List<Attendance> findByStudentId(Long studentId);
    }

    @Repository
    public interface ResultRepository extends JpaRepository<Result, Long> {
        List<Result> findByStudentId(Long studentId);
    }
}
