package com.student.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

public class StudentEntities {

    // 1. Users Table (Auth Module)
    @Entity
    @Table(name = "users")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String username;

        @Column(nullable = false)
        private String password;

        @Column(unique = true, nullable = false)
        private String email;

        @Column(nullable = false)
        private String role; // ADMIN, STUDENT, TEACHER
    }

    // 2. Students Table (Student Module)
    @Entity
    @Table(name = "students")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Student {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String rollNumber;

        @Column(nullable = false)
        private String name;

        @Column(unique = true, nullable = false)
        private String email;

        private String department;
        private String academicYear;
        private String phone;
    }

    // 3. Attendance Table (Attendance Module)
    @Entity
    @Table(name = "attendance")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Attendance {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long studentId;

        @Column(nullable = false)
        private LocalDate date;

        private String subject;

        @Column(nullable = false)
        private String status; // PRESENT, ABSENT
    }

    // 4. Results Table (Result Module)
    @Entity
    @Table(name = "results")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Result {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private Long studentId;

        @Column(nullable = false)
        private String subject;

        private int marksObtained;
        private int maxMarks;
        private String grade;
        private String semester;
    }
}
