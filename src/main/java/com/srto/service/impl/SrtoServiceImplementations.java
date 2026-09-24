package com.srto.service.impl;

import com.srto.entity.SrtoEntities.*;
import com.srto.repository.SrtoRepositories.*;
import com.srto.service.SrtoModularServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class SrtoServiceImplementations implements
        DepartmentService, CourseService, SubjectService, FacultyService,
        ClassroomService, AcademicYearService, SemesterService, StudentBatchService,
        TimetableService, ConflictDetectionService, OptimizationService,
        NotificationService, DashboardService, AnalyticsService, ReportService, SettingsService {

    private final DepartmentRepository departmentRepository;
    private final CourseRepository courseRepository;
    private final SubjectRepository subjectRepository;
    private final FacultyRepository facultyRepository;
    private final ClassroomRepository classroomRepository;
    private final AcademicYearRepository academicYearRepository;
    private final SemesterRepository semesterRepository;
    private final StudentBatchRepository studentBatchRepository;
    private final TimetableEntryRepository timetableEntryRepository;
    private final NotificationRepository notificationRepository;
    private final SettingRepository settingRepository;

    // --- DepartmentService ---
    @Override public List<Department> getAllDepartments() { return departmentRepository.findAll(); }
    @Override public Department saveDepartment(Department d) { return departmentRepository.save(d); }
    @Override public Department getDepartmentById(Long id) { return departmentRepository.findById(id).orElse(null); }
    @Override public void deleteDepartment(Long id) { departmentRepository.deleteById(id); }

    // --- CourseService ---
    @Override public List<Course> getAllCourses() { return courseRepository.findAll(); }
    @Override public Course saveCourse(Course c) { return courseRepository.save(c); }
    @Override public Course getCourseById(Long id) { return courseRepository.findById(id).orElse(null); }
    @Override public void deleteCourse(Long id) { courseRepository.deleteById(id); }

    // --- SubjectService ---
    @Override public List<Subject> getAllSubjects() { return subjectRepository.findAll(); }
    @Override public Subject saveSubject(Subject s) { return subjectRepository.save(s); }
    @Override public Subject getSubjectById(Long id) { return subjectRepository.findById(id).orElse(null); }
    @Override public void deleteSubject(Long id) { subjectRepository.deleteById(id); }

    // --- FacultyService ---
    @Override public List<Faculty> getAllFaculty() { return facultyRepository.findAll(); }
    @Override public Faculty saveFaculty(Faculty f) { return facultyRepository.save(f); }
    @Override public Faculty getFacultyById(Long id) { return facultyRepository.findById(id).orElse(null); }
    @Override public void deleteFaculty(Long id) { facultyRepository.deleteById(id); }

    // --- StudentBatchService ---
    @Override public List<StudentBatch> getAllBatches() { return studentBatchRepository.findAll(); }
    @Override public StudentBatch saveBatch(StudentBatch b) { return studentBatchRepository.save(b); }
    @Override public StudentBatch getBatchById(Long id) { return studentBatchRepository.findById(id).orElse(null); }

    // --- ClassroomService ---
    @Override public List<Classroom> getAllClassrooms() { return classroomRepository.findAll(); }
    @Override public Classroom saveClassroom(Classroom c) { return classroomRepository.save(c); }
    @Override public Classroom getClassroomById(Long id) { return classroomRepository.findById(id).orElse(null); }

    // --- AcademicYear & Semester ---
    @Override public List<AcademicYear> getAllAcademicYears() { return academicYearRepository.findAll(); }
    @Override public AcademicYear saveAcademicYear(AcademicYear y) { return academicYearRepository.save(y); }
    @Override public List<Semester> getAllSemesters() { return semesterRepository.findAll(); }
    @Override public Semester saveSemester(Semester s) { return semesterRepository.save(s); }

    // --- TimetableService ---
    @Override public List<TimetableEntry> getAllEntries() { return timetableEntryRepository.findAll(); }
    @Override public TimetableEntry saveEntry(TimetableEntry e) { return timetableEntryRepository.save(e); }
    @Override public void deleteEntry(Long id) { timetableEntryRepository.deleteById(id); }

    @Override
    public List<TimetableEntry> generateTimetable(Long batchId) {
        return optimizeSchedule(batchId);
    }

    // --- ConflictDetectionService ---
    @Override
    public boolean hasConflict(TimetableEntry entry) {
        List<TimetableEntry> roomClashes = timetableEntryRepository
            .findByDayOfWeekAndClassroomIdAndStartTimeLessThanAndEndTimeGreaterThan(
                entry.getDayOfWeek(), entry.getClassroom().getId(), entry.getEndTime(), entry.getStartTime()
            );

        List<TimetableEntry> facultyClashes = timetableEntryRepository
            .findByDayOfWeekAndFacultyIdAndStartTimeLessThanAndEndTimeGreaterThan(
                entry.getDayOfWeek(), entry.getFaculty().getId(), entry.getEndTime(), entry.getStartTime()
            );

        List<TimetableEntry> batchClashes = timetableEntryRepository
            .findByDayOfWeekAndBatchIdAndStartTimeLessThanAndEndTimeGreaterThan(
                entry.getDayOfWeek(), entry.getBatch().getId(), entry.getEndTime(), entry.getStartTime()
            );

        long rCount = roomClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();
        long fCount = facultyClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();
        long bCount = batchClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();

        return rCount > 0 || fCount > 0 || bCount > 0;
    }

    @Override
    public List<TimetableEntry> getConflictingEntries() {
        return timetableEntryRepository.findAll().stream()
                .filter(this::hasConflict)
                .toList();
    }

    // --- OptimizationService ---
    @Override
    public List<TimetableEntry> optimizeSchedule(Long batchId) {
        StudentBatch batch = studentBatchRepository.findById(batchId).orElseThrow();
        List<Subject> subjects = subjectRepository.findByDepartmentId(batch.getCourse().getDepartment().getId());
        List<Faculty> faculties = facultyRepository.findByDepartmentId(batch.getCourse().getDepartment().getId());
        List<Classroom> classrooms = classroomRepository.findAll();

        if (subjects.isEmpty() || faculties.isEmpty() || classrooms.isEmpty()) return Collections.emptyList();

        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
        LocalTime[] slots = { LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0), LocalTime.of(12, 0), LocalTime.of(14, 0) };
        List<TimetableEntry> generated = new ArrayList<>();
        Random random = new Random();

        for (String day : days) {
            for (LocalTime slotStart : slots) {
                Subject subject = subjects.get(random.nextInt(subjects.size()));
                Faculty faculty = faculties.get(random.nextInt(faculties.size()));
                Classroom room = classrooms.get(random.nextInt(classrooms.size()));

                TimetableEntry candidate = TimetableEntry.builder()
                        .dayOfWeek(day)
                        .startTime(slotStart)
                        .endTime(slotStart.plusHours(1))
                        .subject(subject)
                        .faculty(faculty)
                        .classroom(room)
                        .batch(batch)
                        .status("OPTIMIZED")
                        .build();

                if (!hasConflict(candidate)) {
                    generated.add(timetableEntryRepository.save(candidate));
                }
            }
        }
        return generated;
    }

    // --- Notifications & Dashboard ---
    @Override public List<Notification> getAllNotifications() { return notificationRepository.findAll(); }
    @Override public Notification sendNotification(Notification n) { return notificationRepository.save(n); }

    @Override
    public Map<String, Object> getDashboardMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalDepartments", departmentRepository.count());
        metrics.put("totalCourses", courseRepository.count());
        metrics.put("totalSubjects", subjectRepository.count());
        metrics.put("totalFaculty", facultyRepository.count());
        metrics.put("totalClassrooms", classroomRepository.count());
        metrics.put("totalBatches", studentBatchRepository.count());
        metrics.put("totalTimetableEntries", timetableEntryRepository.count());
        metrics.put("conflictCount", getConflictingEntries().size());
        return metrics;
    }

    @Override public Map<String, Object> getAnalyticsReport() { return getDashboardMetrics(); }
    @Override public Map<String, Object> generateSummaryReport() { return getDashboardMetrics(); }

    // --- SettingsService ---
    @Override public List<Setting> getAllSettings() { return settingRepository.findAll(); }
    @Override public Setting saveSetting(Setting s) { return settingRepository.save(s); }
}
