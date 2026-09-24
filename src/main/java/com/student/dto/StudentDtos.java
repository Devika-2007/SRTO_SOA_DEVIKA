package com.student.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class StudentDtos {

    @Data @NoArgsConstructor @AllArgsConstructor
    public static class AuthRequest {
        @NotBlank private String username;
        @NotBlank private String password;
    }

    @Data @AllArgsConstructor
    public static class AuthResponse {
        private String token;
        private String username;
        private String role;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class StudentDto {
        private Long id;
        @NotBlank private String rollNumber;
        @NotBlank private String name;
        @Email @NotBlank private String email;
        private String department;
        private String academicYear;
        private String phone;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AttendanceDto {
        private Long id;
        @NotNull private Long studentId;
        private LocalDate date;
        private String subject;
        @NotBlank private String status;
    }

    @Data @NoArgsConstructor @AllArgsConstructor @Builder
    public static class ResultDto {
        private Long id;
        @NotNull private Long studentId;
        @NotBlank private String subject;
        private int marksObtained;
        private int maxMarks;
        private String grade;
        private String semester;
    }
}
