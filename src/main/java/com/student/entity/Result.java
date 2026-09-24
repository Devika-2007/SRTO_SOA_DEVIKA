package com.student.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Result {
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
