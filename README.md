# Student Information & Academic Performance Management System

![Java 21](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot 3](https://img.shields.io/badge/Spring%20Boot-3.2.3-brightgreen.svg)
![Architecture](https://img.shields.io/badge/Architecture-Monolithic-blue.svg)

An enterprise-grade, clean, single monolithic Spring Boot 3.x platform that manages Student Profiles, Attendance Tracking, Academic Performance Results, and JWT Security.

---

## 🏛️ System Overview & Core Modules

- **Authentication Module**: JWT Bearer token generation, login validation, and user registration.
- **Student Module**: Complete student profile directory & registration.
- **Attendance Module**: Daily class attendance marking & percentage calculations.
- **Result Module**: Subject marks entry, automated letter grade calculation (`A+`, `A`, `B+`, `B`, `C`, `F`), and academic scorecards.

---

## 🚀 Running the Project

### Method 1: Command Prompt (CMD) Execution
Run the compiled executable JAR file directly:
```cmd
java -jar target/student-management-system-1.0.0.jar
```

### Method 2: Spring Tool Suite (STS) / IDE Execution
1. Open STS $\rightarrow$ **File** $\rightarrow$ **Import...** $\rightarrow$ **Existing Maven Projects**.
2. Select directory `SRTO DEVIKA`.
3. Right-click `com.student.StudentManagementApplication` $\rightarrow$ **Run As** $\rightarrow$ **Spring Boot App**.

---

## 🌐 Web Interface & Endpoints

- **Web Dashboard**: [http://localhost:8080/](http://localhost:8080/)
- **Login View**: [http://localhost:8080/login](http://localhost:8080/login) *(User: `admin` | Password: `admin123`)*
- **H2 Database Console**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) *(JDBC URL: `jdbc:h2:mem:studentdb`)*

---

## 🧪 Postman API Testing Collection

A pre-configured Postman Collection file `postman_collection.json` is included in the project root.

### Importing to Postman:
1. Open Postman $\rightarrow$ Click **Import**.
2. Select `postman_collection.json` from this project folder.
3. Test requests for Auth, Students, Attendance, and Results!
