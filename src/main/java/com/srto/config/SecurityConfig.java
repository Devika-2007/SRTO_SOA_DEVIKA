package com.srto.config;

import com.srto.entity.SrtoEntities.User;
import com.srto.repository.SrtoRepositories.UserRepository;
import com.srto.service.SrtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return username -> {
            User user = userRepository.findByUsername(username)
                    .orElseGet(() -> userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with username or email: " + username)));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.isEnabled(),
                    true, true, true,
                    user.getRoles().stream()
                            .map(role -> new SimpleGrantedAuthority(role.getName()))
                            .collect(Collectors.toList())
            );
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disabled for local development & simple forms
            .headers(headers -> headers.frameOptions(frame -> frame.disable())) // Enable H2 Console frame
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/h2-console/**", "/login").permitAll()
                .requestMatchers("/admin/**", "/settings/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public CommandLineRunner initData(SrtoService srtoService) {
        return args -> srtoService.seedInitialData();
    }
}
