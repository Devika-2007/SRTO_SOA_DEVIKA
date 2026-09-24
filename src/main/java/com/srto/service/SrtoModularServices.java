package com.srto.service;

import com.srto.entity.SrtoEntities.*;
import java.util.List;
import java.util.Map;

public interface SrtoModularServices {

    interface AuthenticationService {
        User registerUser(User user);
        User findByUsername(String username);
    }

    interface UserService {
        List<User> getAllUsers();
        User getUserById(Long id);
    }

    interface RoleService {
        List<Role> getAllRoles();
    }

    interface DepartmentService {
        List<Department> getAllDepartments();
        Department saveDepartment(Department department);
        Department getDepartmentById(Long id);
        void deleteDepartment(Long id);
    }

    interface CourseService {
        List<Course> getAllCourses();
        Course saveCourse(Course course);
        Course getCourseById(Long id);
        void deleteCourse(Long id);
    }

    interface SubjectService {
        List<Subject> getAllSubjects();
        Subject saveSubject(Subject subject);
        Subject getSubjectById(Long id);
        void deleteSubject(Long id);
    }

    interface FacultyService {
        List<Faculty> getAllFaculty();
        Faculty saveFaculty(Faculty faculty);
        Faculty getFacultyById(Long id);
        void deleteFaculty(Long id);
    }

    interface StudentBatchService {
        List<StudentBatch> getAllBatches();
        StudentBatch saveBatch(StudentBatch batch);
        StudentBatch getBatchById(Long id);
    }

    interface ClassroomService {
        List<Classroom> getAllClassrooms();
        Classroom saveClassroom(Classroom classroom);
        Classroom getClassroomById(Long id);
    }

    interface AcademicYearService {
        List<AcademicYear> getAllAcademicYears();
        AcademicYear saveAcademicYear(AcademicYear year);
    }

    interface SemesterService {
        List<Semester> getAllSemesters();
        Semester saveSemester(Semester semester);
    }

    interface TimetableService {
        List<TimetableEntry> getAllEntries();
        List<TimetableEntry> generateTimetable(Long batchId);
        TimetableEntry saveEntry(TimetableEntry entry);
        void deleteEntry(Long id);
    }

    interface ConflictDetectionService {
        boolean hasConflict(TimetableEntry entry);
        List<TimetableEntry> getConflictingEntries();
    }

    interface OptimizationService {
        List<TimetableEntry> optimizeSchedule(Long batchId);
    }

    interface NotificationService {
        List<Notification> getAllNotifications();
        Notification sendNotification(Notification notification);
    }

    interface DashboardService {
        Map<String, Object> getDashboardMetrics();
    }

    interface AnalyticsService {
        Map<String, Object> getAnalyticsReport();
    }

    interface ReportService {
        Map<String, Object> generateSummaryReport();
    }

    interface SettingsService {
        List<Setting> getAllSettings();
        Setting saveSetting(Setting setting);
    }
}
