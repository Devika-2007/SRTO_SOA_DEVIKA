package com.student.result;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class ResultServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(ResultServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner initResults(ResultRepository resultRepository) {
        return args -> {
            if (resultRepository.count() == 0) {
                resultRepository.save(Result.builder()
                        .studentId(1L)
                        .subject("Data Structures & Algorithms")
                        .marksObtained(88)
                        .maxMarks(100)
                        .grade("A")
                        .semester("Semester 5")
                        .build());

                resultRepository.save(Result.builder()
                        .studentId(1L)
                        .subject("Operating Systems")
                        .marksObtained(92)
                        .maxMarks(100)
                        .grade("A+")
                        .semester("Semester 5")
                        .build());

                resultRepository.save(Result.builder()
                        .studentId(2L)
                        .subject("Data Structures & Algorithms")
                        .marksObtained(76)
                        .maxMarks(100)
                        .grade("B+")
                        .semester("Semester 5")
                        .build());
            }
        };
    }
}

// Result Entity (Results Table)
@Entity
@Table(name = "results")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class Result {
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

// Result Repository
interface ResultRepository extends JpaRepository<Result, Long> {
    List<Result> findByStudentId(Long studentId);
}

// Result Business Service
@Service
@RequiredArgsConstructor
class ResultBusinessService {
    private final ResultRepository resultRepository;

    public Result addResult(Result result) {
        if (result.getGrade() == null) {
            result.setGrade(calculateGrade(result.getMarksObtained(), result.getMaxMarks()));
        }
        return resultRepository.save(result);
    }

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

// Result Controller
@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
class ResultController {
    private final ResultBusinessService resultBusinessService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Result>> getStudentResults(@PathVariable Long studentId) {
        return ResponseEntity.ok(resultBusinessService.getResultsByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<Result> addMarks(@RequestBody Result result) {
        return ResponseEntity.ok(resultBusinessService.addResult(result));
    }
}
