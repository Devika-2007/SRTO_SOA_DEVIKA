# Student Information & Academic Performance Management System

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)
![Architecture](https://img.shields.io/badge/Architecture-Monolithic-blue.svg)

A clean, enterprise-grade single monolithic Spring Boot 3.x application managing Student Profiles, Attendance Tracking, Academic Performance Results, and JWT Authentication.

---

## 🚀 Key Modules (Logical Packages within Single Project)

- **`com.student.config`**: Security configuration, password encoding, JWT utilities, database initialization.
- **`com.student.controller`**: MVC Controllers (`WebMvcController`) & REST Controllers (`StudentControllers` for Auth, Student, Attendance, Result APIs).
- **`com.student.dto`**: Data Transfer Objects (`StudentDtos`).
- **`com.student.entity`**: JPA Entities (`User`, `Student`, `Attendance`, `Result`).
- **`com.student.repository`**: JPA Data Repositories (`StudentRepositories`).
- **`com.student.service`**: Business Service Contracts & Implementations (`StudentServices`).

---

## 🛢️ Database Schema (`users`, `students`, `attendance`, `results`)

1. **`users`**: `id`, `username`, `password`, `email`, `role`
2. **`students`**: `id`, `rollNumber`, `name`, `email`, `department`, `academicYear`, `phone`
3. **`attendance`**: `id`, `studentId`, `date`, `subject`, `status`
4. **`results`**: `id`, `studentId`, `subject`, `marksObtained`, `maxMarks`, `grade`, `semester`

---

## ⚡ API Endpoints (All running on http://localhost:8080)

### 🔑 Authentication Module
- `POST /api/auth/login` - Authenticate & generate JWT token
- `POST /api/auth/register` - Register new user

### 🎓 Student Module
- `GET /api/students` - List all students
- `GET /api/students/{id}` - Get student profile
- `POST /api/students` - Register new student
- `PUT /api/students/{id}` - Update student profile
- `DELETE /api/students/{id}` - Delete student

### 📅 Attendance Module
- `POST /api/attendance` - Mark student attendance
- `GET /api/attendance/student/{studentId}` - View student attendance history
- `GET /api/attendance/student/{studentId}/percentage` - Calculate attendance percentage

### 📊 Result Module
- `POST /api/results` - Add subject marks
- `GET /api/results/student/{studentId}` - View academic performance scorecard
