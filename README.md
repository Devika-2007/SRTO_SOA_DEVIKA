# Student Information & Academic Performance Management Microservices System

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.0-blue.svg)

An enterprise-grade, lightweight Spring Cloud microservices architecture for Student Management, Attendance Tracking, Academic Performance Results, JWT Authentication, Service Discovery, and API Routing.

---

## 🏗️ Architecture & Service Topology

| Microservice Module | Port | Technology Stack | Description |
| :--- | :---: | :--- | :--- |
| **`eureka-server`** | `8761` | Netflix Eureka Server | Service Discovery & Registry |
| **`api-gateway`** | `8080` | Spring Cloud Gateway | API Routing & Client Load Balancing |
| **`auth-service`** | `8081` | Spring Security + JWT | Authentication & User Management (`Users` table) |
| **`student-service`** | `8082` | Spring Data JPA | Student Profile & Details (`Students` table) |
| **`attendance-service`** | `8083` | Spring Data JPA + RestTemplate | Attendance Marking & History (`Attendance` table) |
| **`result-service`** | `8084` | Spring Data JPA + RestTemplate | Academic Marks & Grades (`Results` table) |

---

## ⚡ API Endpoints & Routes (via Gateway at Port 8080)

### 🔑 Authentication Service (`/api/auth`)
- `POST /api/auth/login` - Authenticate user & get JWT token
- `POST /api/auth/register` - Register new user account
- `GET /api/auth/validate?token=...` - Validate JWT token

### 🎓 Student Service (`/api/students`)
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Register a new student
- `PUT /api/students/{id}` - Update student profile
- `DELETE /api/students/{id}` - Remove student

### 📅 Attendance Service (`/api/attendance`)
- `POST /api/attendance` - Mark student attendance
- `GET /api/attendance/student/{studentId}` - Attendance history
- `GET /api/attendance/student/{studentId}/percentage` - Calculate percentage

### 📊 Result Service (`/api/results`)
- `POST /api/results` - Add subject marks
- `GET /api/results/student/{studentId}` - Get academic scorecard & grades

---

## 🛢️ Database Tables

1. **`users`**: `id`, `username`, `password`, `email`, `role`
2. **`students`**: `id`, `rollNumber`, `name`, `email`, `department`, `academicYear`, `phone`
3. **`attendance`**: `id`, `studentId`, `date`, `subject`, `status`
4. **`results`**: `id`, `studentId`, `subject`, `marksObtained`, `maxMarks`, `grade`, `semester`
