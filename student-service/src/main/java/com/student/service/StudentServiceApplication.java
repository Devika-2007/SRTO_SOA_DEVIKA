package com.student.service;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SpringBootApplication
@EnableDiscoveryClient
public class StudentServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner initStudents(StudentRepository studentRepository) {
        return args -> {
            if (studentRepository.count() == 0) {
                studentRepository.save(Student.builder()
                        .rollNumber("CS2025-01")
                        .name("John Doe")
                        .email("john.doe@university.edu")
                        .department("Computer Science")
                        .academicYear("3rd Year")
                        .phone("+1-555-0192")
                        .build());

                studentRepository.save(Student.builder()
                        .rollNumber("CS2025-02")
                        .name("Jane Smith")
                        .email("jane.smith@university.edu")
                        .department("Computer Science")
                        .academicYear("3rd Year")
                        .phone("+1-555-0193")
                        .build());
            }
        };
    }
}

// Student Entity (Students Table)
@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class Student {
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

// Student Repository
interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByRollNumber(String rollNumber);
}

// Student Service
@Service
@RequiredArgsConstructor
class StudentBusinessService {
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() { return studentRepository.findAll(); }
    public Optional<Student> getStudentById(Long id) { return studentRepository.findById(id); }
    public Student saveStudent(Student student) { return studentRepository.save(student); }
    public void deleteStudent(Long id) { studentRepository.deleteById(id); }
}

// Student Controller
@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
class StudentController {

    private final StudentBusinessService studentBusinessService;

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentBusinessService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentBusinessService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        return ResponseEntity.ok(studentBusinessService.saveStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentBusinessService.getStudentById(id)
                .map(existing -> {
                    student.setId(existing.getId());
                    return ResponseEntity.ok(studentBusinessService.saveStudent(student));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentBusinessService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
