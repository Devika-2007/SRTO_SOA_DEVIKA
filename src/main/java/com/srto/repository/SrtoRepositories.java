package com.srto.repository;

import com.srto.entity.SrtoEntities.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class SrtoRepositories {

    @Repository
    public interface RoleRepository extends JpaRepository<Role, Long> {
        Optional<Role> findByName(String name);
    }

    @Repository
    public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUsername(String username);
        Optional<User> findByEmail(String email);
    }

    @Repository
    public interface DepartmentRepository extends JpaRepository<Department, Long> {
        Optional<Department> findByCode(String code);
    }

    @Repository
    public interface CourseRepository extends JpaRepository<Course, Long> {
        Optional<Course> findByCode(String code);
        List<Course> findByDepartmentId(Long departmentId);
    }

    @Repository
    public interface SubjectRepository extends JpaRepository<Subject, Long> {
        Optional<Subject> findByCode(String code);
        List<Subject> findByDepartmentId(Long departmentId);
    }

    @Repository
    public interface FacultyRepository extends JpaRepository<Faculty, Long> {
        Optional<Faculty> findByEmployeeId(String employeeId);
        List<Faculty> findByDepartmentId(Long departmentId);
    }

    @Repository
    public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
        Optional<Classroom> findByRoomNumber(String roomNumber);
        List<Classroom> findByIsLab(boolean isLab);
    }

    @Repository
    public interface AcademicYearRepository extends JpaRepository<AcademicYear, Long> {
        Optional<AcademicYear> findByIsCurrentTrue();
    }

    @Repository
    public interface SemesterRepository extends JpaRepository<Semester, Long> {
        List<Semester> findByAcademicYearId(Long academicYearId);
    }

    @Repository
    public interface StudentBatchRepository extends JpaRepository<StudentBatch, Long> {
        List<StudentBatch> findBySemesterId(Long semesterId);
    }

    @Repository
    public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, Long> {
        List<TimetableEntry> findByBatchId(Long batchId);
        List<TimetableEntry> findByFacultyId(Long facultyId);
        List<TimetableEntry> findByClassroomId(Long classroomId);
        
        List<TimetableEntry> findByDayOfWeekAndClassroomIdAndStartTimeLessThanAndEndTimeGreaterThan(
            String dayOfWeek, Long classroomId, LocalTime endTime, LocalTime startTime
        );

        List<TimetableEntry> findByDayOfWeekAndFacultyIdAndStartTimeLessThanAndEndTimeGreaterThan(
            String dayOfWeek, Long facultyId, LocalTime endTime, LocalTime startTime
        );

        List<TimetableEntry> findByDayOfWeekAndBatchIdAndStartTimeLessThanAndEndTimeGreaterThan(
            String dayOfWeek, Long batchId, LocalTime endTime, LocalTime startTime
        );
    }

    @Repository
    public interface NotificationRepository extends JpaRepository<Notification, Long> {
        List<Notification> findTop10ByOrderByTimestampDesc();
    }

    @Repository
    public interface SettingRepository extends JpaRepository<Setting, Long> {
        Optional<Setting> findByKeyName(String keyName);
    }
}
