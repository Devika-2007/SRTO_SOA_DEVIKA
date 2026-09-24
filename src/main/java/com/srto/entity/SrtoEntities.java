package com.srto.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class SrtoEntities {

    @Entity
    @Table(name = "roles")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Role {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String name; // ROLE_ADMIN, ROLE_FACULTY, ROLE_STUDENT
    }

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

        @Column(nullable = false)
        private String fullName;

        @Column(unique = true, nullable = false)
        private String email;

        private boolean enabled = true;

        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
        )
        @Builder.Default
        private Set<Role> roles = new HashSet<>();

        private LocalDateTime createdAt;

        @PrePersist
        public void prePersist() {
            this.createdAt = LocalDateTime.now();
        }
    }

    @Entity
    @Table(name = "departments")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Department {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String code;

        @Column(nullable = false)
        private String name;

        private String headOfDepartment;
    }

    @Entity
    @Table(name = "courses")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Course {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String code;

        @Column(nullable = false)
        private String name;

        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;

        private int durationYears;
    }

    @Entity
    @Table(name = "subjects")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Subject {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String code;

        @Column(nullable = false)
        private String name;

        private int credits;

        private int weeklyHours;

        private boolean isLab;

        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;
    }

    @Entity
    @Table(name = "faculty")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Faculty {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String employeeId;

        @Column(nullable = false)
        private String name;

        private String email;
        private String designation;

        private int maxWeeklyHours;
        private int currentAssignedHours;

        @ManyToOne
        @JoinColumn(name = "department_id")
        private Department department;

        @OneToOne
        @JoinColumn(name = "user_id")
        private User user;
    }

    @Entity
    @Table(name = "classrooms")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Classroom {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String roomNumber;

        private String building;
        private int capacity;
        private boolean isLab;
        private boolean isAvailable = true;
        private String equipment; // Projector, Smartboard, AC, etc.
    }

    @Entity
    @Table(name = "academic_years")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AcademicYear {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String yearName; // e.g. 2025-2026

        private boolean isCurrent;
    }

    @Entity
    @Table(name = "semesters")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Semester {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String name; // Semester 1, Semester 2...

        private int semesterNumber;

        @ManyToOne
        @JoinColumn(name = "academic_year_id")
        private AcademicYear academicYear;
    }

    @Entity
    @Table(name = "student_batches")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class StudentBatch {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String batchName; // e.g., CSE-2024-SecA

        private int studentCount;

        @ManyToOne
        @JoinColumn(name = "course_id")
        private Course course;

        @ManyToOne
        @JoinColumn(name = "semester_id")
        private Semester semester;
    }

    @Entity
    @Table(name = "timetable_entries")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class TimetableEntry {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String dayOfWeek; // MONDAY, TUESDAY...

        private LocalTime startTime;
        private LocalTime endTime;

        @ManyToOne
        @JoinColumn(name = "subject_id")
        private Subject subject;

        @ManyToOne
        @JoinColumn(name = "faculty_id")
        private Faculty faculty;

        @ManyToOne
        @JoinColumn(name = "classroom_id")
        private Classroom classroom;

        @ManyToOne
        @JoinColumn(name = "batch_id")
        private StudentBatch batch;

        private String status; // GENERATED, APPROVED, CONFLICT
    }

    @Entity
    @Table(name = "notifications")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Notification {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;
        private String message;
        private String targetRole; // ALL, ADMIN, FACULTY, STUDENT
        private LocalDateTime timestamp;
        private boolean readStatus;

        @PrePersist
        public void prePersist() {
            this.timestamp = LocalDateTime.now();
        }
    }

    @Entity
    @Table(name = "settings")
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Setting {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true, nullable = false)
        private String keyName;

        private String keyValue;
        private String description;
    }
}
