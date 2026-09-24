package com.student.attendance;

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

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableDiscoveryClient
public class AttendanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(AttendanceServiceApplication.class, args);
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public CommandLineRunner initAttendance(AttendanceRepository attendanceRepository) {
        return args -> {
            if (attendanceRepository.count() == 0) {
                attendanceRepository.save(Attendance.builder()
                        .studentId(1L)
                        .date(LocalDate.now().minusDays(1))
                        .subject("Data Structures")
                        .status("PRESENT")
                        .build());

                attendanceRepository.save(Attendance.builder()
                        .studentId(1L)
                        .date(LocalDate.now())
                        .subject("Operating Systems")
                        .status("PRESENT")
                        .build());

                attendanceRepository.save(Attendance.builder()
                        .studentId(2L)
                        .date(LocalDate.now())
                        .subject("Data Structures")
                        .status("ABSENT")
                        .build());
            }
        };
    }
}

// Attendance Entity (Attendance Table)
@Entity
@Table(name = "attendance")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
class Attendance {
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

// Attendance Repository
interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByStudentId(Long studentId);
}

// Attendance Business Service
@Service
@RequiredArgsConstructor
class AttendanceBusinessService {
    private final AttendanceRepository attendanceRepository;
    private final RestTemplate restTemplate;

    public Attendance markAttendance(Attendance attendance) {
        if (attendance.getDate() == null) attendance.setDate(LocalDate.now());
        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAttendanceByStudent(Long studentId) {
        return attendanceRepository.findByStudentId(studentId);
    }

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

// Attendance Controller
@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
class AttendanceController {
    private final AttendanceBusinessService attendanceBusinessService;

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Attendance>> getAttendanceByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceBusinessService.getAttendanceByStudent(studentId));
    }

    @PostMapping
    public ResponseEntity<Attendance> markAttendance(@RequestBody Attendance attendance) {
        return ResponseEntity.ok(attendanceBusinessService.markAttendance(attendance));
    }

    @GetMapping("/student/{studentId}/percentage")
    public ResponseEntity<Map<String, Object>> getAttendancePercentage(@PathVariable Long studentId) {
        return ResponseEntity.ok(attendanceBusinessService.calculatePercentage(studentId));
    }
}
