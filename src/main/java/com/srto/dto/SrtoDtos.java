package com.srto.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;

public class SrtoDtos {

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class DepartmentDto {
        private Long id;
        @NotBlank(message = "Department code is required")
        private String code;
        @NotBlank(message = "Department name is required")
        private String name;
        private String headOfDepartment;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class CourseDto {
        private Long id;
        @NotBlank private String code;
        @NotBlank private String name;
        @NotNull private Long departmentId;
        private int durationYears;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class SubjectDto {
        private Long id;
        @NotBlank private String code;
        @NotBlank private String name;
        private int credits;
        private int weeklyHours;
        private boolean isLab;
        @NotNull private Long departmentId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class FacultyDto {
        private Long id;
        @NotBlank private String employeeId;
        @NotBlank private String name;
        private String email;
        private String designation;
        private int maxWeeklyHours;
        private Long departmentId;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class ClassroomDto {
        private Long id;
        @NotBlank private String roomNumber;
        private String building;
        private int capacity;
        private boolean isLab;
        private boolean isAvailable;
        private String equipment;
    }

    @Data @Builder @NoArgsConstructor @AllArgsConstructor
    public static class TimetableSlotDto {
        private Long id;
        private String dayOfWeek;
        private LocalTime startTime;
        private LocalTime endTime;
        private Long subjectId;
        private Long facultyId;
        private Long classroomId;
        private Long batchId;
        private String status;
    }
}
