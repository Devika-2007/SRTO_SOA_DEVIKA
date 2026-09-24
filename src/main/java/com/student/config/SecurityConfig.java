package com.student.config;

import com.student.entity.Attendance;
import com.student.entity.Result;
import com.student.entity.Student;
import com.student.entity.User;
import com.student.repository.AttendanceRepository;
import com.student.repository.ResultRepository;
import com.student.repository.StudentRepository;
import com.student.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            return org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(user.getRole())
                    .build();
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/h2-console/**", "/login", "/api/auth/**").permitAll()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public CommandLineRunner initDatabase(UserRepository userRepository,
                                          StudentRepository studentRepository,
                                          AttendanceRepository attendanceRepository,
                                          ResultRepository resultRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                userRepository.save(User.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .email("admin@student.com")
                        .role("ADMIN")
                        .build());

                userRepository.save(User.builder()
                        .username("student")
                        .password(passwordEncoder.encode("student123"))
                        .email("student@student.com")
                        .role("STUDENT")
                        .build());
            }

            if (studentRepository.count() == 0) {
                Student s1 = studentRepository.save(Student.builder()
                        .rollNumber("CS2025-01")
                        .name("John Doe")
                        .email("john.doe@university.edu")
                        .department("Computer Science")
                        .academicYear("3rd Year")
                        .phone("+1-555-0192")
                        .build());

                Student s2 = studentRepository.save(Student.builder()
                        .rollNumber("CS2025-02")
                        .name("Jane Smith")
                        .email("jane.smith@university.edu")
                        .department("Computer Science")
                        .academicYear("3rd Year")
                        .phone("+1-555-0193")
                        .build());

                attendanceRepository.save(Attendance.builder()
                        .studentId(s1.getId())
                        .date(LocalDate.now().minusDays(1))
                        .subject("Data Structures")
                        .status("PRESENT")
                        .build());

                attendanceRepository.save(Attendance.builder()
                        .studentId(s1.getId())
                        .date(LocalDate.now())
                        .subject("Operating Systems")
                        .status("PRESENT")
                        .build());

                resultRepository.save(Result.builder()
                        .studentId(s1.getId())
                        .subject("Data Structures & Algorithms")
                        .marksObtained(88)
                        .maxMarks(100)
                        .grade("A")
                        .semester("Semester 5")
                        .build());
            }
        };
    }

    @Component
    public static class JwtUtil {
        @Value("${jwt.secret}")
        private String secret;

        @Value("${jwt.expiration}")
        private Long expiration;

        private Key getSigningKey() {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            return Keys.hmacShaKeyFor(keyBytes);
        }

        public String generateToken(String username, String role) {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration))
                    .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                    .compact();
        }

        public boolean validateToken(String token) {
            try {
                Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        public String extractUsername(String token) {
            return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                    .parseClaimsJws(token).getBody().getSubject();
        }
    }
}
