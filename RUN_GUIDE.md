# Complete Execution Guide (CMD & STS)

This guide provides step-by-step instructions to run the **Student Information & Academic Performance Management System** via Command Prompt (CMD) and Spring Tool Suite (STS).

---

## 🛠️ Step 1: Running via Command Prompt (CMD)

1. Open Command Prompt (`cmd.exe`).
2. Navigate to the project root directory:
   ```cmd
   cd "C:\Users\johns\Desktop\SRTO DEVIKA"
   ```
3. Run the executable Spring Boot JAR file:
   ```cmd
   java -jar target/student-management-system-1.0.0.jar
   ```
4. Verify output logs show:
   ```text
   Tomcat started on port 8080 (http) with context path ''
   Started StudentManagementApplication in X.XXX seconds
   ```

---

## 🍃 Step 2: Running via Spring Tool Suite (STS)

1. Open Spring Tool Suite (STS).
2. Click **File** $\rightarrow$ **Import...** $\rightarrow$ **Maven** $\rightarrow$ **Existing Maven Projects** $\rightarrow$ Click **Next**.
3. Browse to the root directory `C:\Users\johns\Desktop\SRTO DEVIKA` and click **Finish**.
4. In Project Explorer, expand `src/main/java`.
5. Navigate to `com.student.StudentManagementApplication.java`.
6. Right-click `StudentManagementApplication.java` $\rightarrow$ **Run As** $\rightarrow$ **Spring Boot App**.

---

## 📮 Step 3: Testing Endpoints with Postman

1. Open **Postman**.
2. Click **Import** in the top-left corner.
3. Select `postman_collection.json` located in the root directory.
4. Execute test requests:
   - **Login**: `POST http://localhost:8080/api/auth/login`
   - **Get Students**: `GET http://localhost:8080/api/students`
   - **Mark Attendance**: `POST http://localhost:8080/api/attendance`
   - **View Attendance Percentage**: `GET http://localhost:8080/api/attendance/student/1/percentage`
   - **Add Results**: `POST http://localhost:8080/api/results`
   - **Get Results**: `GET http://localhost:8080/api/results/student/1`
