# SRTO - Smart Resource & Timetable Optimization

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)
![Build](https://img.shields.io/badge/Build-Maven-blue.svg)

SRTO (Smart Resource & Timetable Optimization) is an enterprise-grade Spring Boot 3.x platform designed for educational institutions to intelligently generate conflict-free timetables, manage room/lab utilization, optimize faculty workload, and streamline academic infrastructure.

---

## 🚀 Key Modules & Capabilities

- **Authentication & Role-Based Security**: Admin, Faculty, and Student access controls.
- **Executive Dashboard**: Real-time stats on departments, faculty, rooms, timetable entries, and conflict warnings.
- **Timetable Optimization Engine**: Dynamic collision-free schedule generator with conflict detection (room double-booking, faculty clash, batch overlap).
- **Master Data Management**:
  - Departments Management
  - Courses (Degree Programs) Management
  - Subjects & Lab Requirements Management
  - Faculty Directory & Max Workload Limits
  - Classroom & Lab Infrastructure Register
  - Academic Structure (Years, Semesters, Student Batches)
- **Analytics & Notifications**: System notifications feed, audit logs, and resource utilization reports.

---

## 🛠️ Tech Stack

- **Backend**: Java 21, Spring Boot 3.2.3, Spring MVC, Spring Data JPA, Spring Security
- **Frontend**: Thymeleaf, Bootstrap 5, FontAwesome 6, Custom Dark/Glassmorphism CSS
- **Database**: Dual setup (H2 In-Memory for instant STS zero-config launch + MySQL 8+ production ready)
- **Build Tool**: Maven / Embedded Maven Wrapper (`mvnw`)

---

## 🏁 Quick Start & Run Instructions

### Prerequisites
- JDK 21+ installed on PATH
- Spring Tool Suite (STS) or standard Java IDE

### Running with Maven Wrapper
Execute in terminal:
```bash
./mvnw spring-boot:run
```
Or on Windows:
```cmd
mvnw.cmd spring-boot:run
```

### Accessing the Application
- **URL**: `http://localhost:8080`
- **Login Page**: `http://localhost:8080/login`
- **Default Credentials**:
  - **Username**: `admin`
  - **Password**: `admin123`
- **H2 In-Memory Console**: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:srtodb`, User: `sa`, Password: empty)
