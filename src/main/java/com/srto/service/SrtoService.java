package com.srto.service;

import com.srto.entity.SrtoEntities.*;
import com.srto.repository.SrtoRepositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SrtoService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
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
    private final PasswordEncoder passwordEncoder;

    // Seed Initial System Data
    @Transactional
    public void seedInitialData() {
        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
            Role facultyRole = roleRepository.save(Role.builder().name("ROLE_FACULTY").build());
            Role studentRole = roleRepository.save(Role.builder().name("ROLE_STUDENT").build());

            // Default Admin User
            User admin = User.builder()
                    .username("admin")
                    .email("admin@srto.com")
                    .fullName("System Administrator")
                    .password(passwordEncoder.encode("admin123"))
                    .roles(Set.of(adminRole))
                    .enabled(true)
                    .build();
            userRepository.save(admin);

            // Seed Sample Department
            Department cse = departmentRepository.save(Department.builder()
                    .code("CSE")
                    .name("Computer Science & Engineering")
                    .headOfDepartment("Dr. Robert Vance")
                    .build());

            Department ece = departmentRepository.save(Department.builder()
                    .code("ECE")
                    .name("Electronics & Communication")
                    .headOfDepartment("Dr. Elena Rostova")
                    .build());

            // Seed Course
            Course btechCse = courseRepository.save(Course.builder()
                    .code("BTECH-CSE")
                    .name("B.Tech Computer Science")
                    .department(cse)
                    .durationYears(4)
                    .build());

            // Seed Subjects
            Subject ds = subjectRepository.save(Subject.builder()
                    .code("CS301")
                    .name("Data Structures & Algorithms")
                    .credits(4)
                    .weeklyHours(4)
                    .isLab(false)
                    .department(cse)
                    .build());

            Subject osLab = subjectRepository.save(Subject.builder()
                    .code("CS302L")
                    .name("Operating Systems Lab")
                    .credits(2)
                    .weeklyHours(3)
                    .isLab(true)
                    .department(cse)
                    .build());

            // Seed Faculty
            Faculty fac1 = facultyRepository.save(Faculty.builder()
                    .employeeId("FAC001")
                    .name("Prof. Alan Turing")
                    .email("turing@srto.edu")
                    .designation("Associate Professor")
                    .maxWeeklyHours(20)
                    .currentAssignedHours(4)
                    .department(cse)
                    .build());

            Faculty fac2 = facultyRepository.save(Faculty.builder()
                    .employeeId("FAC002")
                    .name("Prof. Grace Hopper")
                    .email("hopper@srto.edu")
                    .designation("Professor")
                    .maxWeeklyHours(18)
                    .currentAssignedHours(3)
                    .department(cse)
                    .build());

            // Seed Classrooms
            Classroom rm101 = classroomRepository.save(Classroom.builder()
                    .roomNumber("LH-101")
                    .building("Academic Block A")
                    .capacity(60)
                    .isLab(false)
                    .equipment("Projector, Smartboard, AC")
                    .isAvailable(true)
                    .build());

            Classroom lab102 = classroomRepository.save(Classroom.builder()
                    .roomNumber("LAB-201")
                    .building("Tech Building B")
                    .capacity(40)
                    .isLab(true)
                    .equipment("40 PCs, High-speed LAN, AC")
                    .isAvailable(true)
                    .build());

            // Seed Academic Year & Semester
            AcademicYear ay2025 = academicYearRepository.save(AcademicYear.builder()
                    .yearName("2025-2026")
                    .isCurrent(true)
                    .build());

            Semester sem3 = semesterRepository.save(Semester.builder()
                    .name("Semester III")
                    .semesterNumber(3)
                    .academicYear(ay2025)
                    .build());

            // Seed Batch
            StudentBatch batchA = studentBatchRepository.save(StudentBatch.builder()
                    .batchName("CSE-2025-SecA")
                    .studentCount(55)
                    .course(btechCse)
                    .semester(sem3)
                    .build());

            // Seed Initial Timetable Entry
            timetableEntryRepository.save(TimetableEntry.builder()
                    .dayOfWeek("MONDAY")
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(10, 0))
                    .subject(ds)
                    .faculty(fac1)
                    .classroom(rm101)
                    .batch(batchA)
                    .status("APPROVED")
                    .build());

            // Seed Initial Notifications
            notificationRepository.save(Notification.builder()
                    .title("System Initialized")
                    .message("SRTO Smart Timetable Optimizer ready for schedule generation.")
                    .targetRole("ALL")
                    .readStatus(false)
                    .build());

            // Seed Settings
            settingRepository.save(Setting.builder()
                    .keyName("INSTITUTION_NAME")
                    .keyValue("SRTO University of Technology")
                    .description("Name displayed across reports and headers")
                    .build());
        }
    }

    // Dashboard Statistics DTO Helper
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalDepartments", departmentRepository.count());
        stats.put("totalFaculty", facultyRepository.count());
        stats.put("totalClassrooms", classroomRepository.count());
        stats.put("totalBatches", studentBatchRepository.count());
        stats.put("totalTimetableEntries", timetableEntryRepository.count());

        // Conflict check count
        List<TimetableEntry> entries = timetableEntryRepository.findAll();
        int conflictCount = 0;
        for (TimetableEntry e : entries) {
            if (hasConflict(e)) {
                conflictCount++;
            }
        }
        stats.put("conflictCount", conflictCount);
        return stats;
    }

    // Conflict Detection Engine
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

        long roomOtherCount = roomClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();
        long facultyOtherCount = facultyClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();
        long batchOtherCount = batchClashes.stream().filter(e -> !e.getId().equals(entry.getId())).count();

        return roomOtherCount > 0 || facultyOtherCount > 0 || batchOtherCount > 0;
    }

    // Dynamic Optimization Engine (AI-Ready Algorithm)
    @Transactional
    public List<TimetableEntry> generateAutomatedTimetable(Long batchId) {
        StudentBatch batch = studentBatchRepository.findById(batchId).orElseThrow();
        List<Subject> subjects = subjectRepository.findByDepartmentId(batch.getCourse().getDepartment().getId());
        List<Faculty> faculties = facultyRepository.findByDepartmentId(batch.getCourse().getDepartment().getId());
        List<Classroom> classrooms = classroomRepository.findAll();

        if (subjects.isEmpty() || faculties.isEmpty() || classrooms.isEmpty()) {
            return Collections.emptyList();
        }

        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY"};
        LocalTime[] slots = {
            LocalTime.of(9, 0), LocalTime.of(10, 0),
            LocalTime.of(11, 0), LocalTime.of(12, 0),
            LocalTime.of(14, 0), LocalTime.of(15, 0)
        };

        List<TimetableEntry> generated = new ArrayList<>();
        Random random = new Random();

        for (String day : days) {
            for (LocalTime slotStart : slots) {
                LocalTime slotEnd = slotStart.plusHours(1);
                Subject subject = subjects.get(random.nextInt(subjects.size()));
                Faculty faculty = faculties.get(random.nextInt(faculties.size()));
                
                // Pick room suitable for lab requirement if applicable
                Classroom room = classrooms.stream()
                        .filter(c -> c.isLab() == subject.isLab() && c.getCapacity() >= batch.getStudentCount())
                        .findFirst()
                        .orElse(classrooms.get(0));

                TimetableEntry candidate = TimetableEntry.builder()
                        .dayOfWeek(day)
                        .startTime(slotStart)
                        .endTime(slotEnd)
                        .subject(subject)
                        .faculty(faculty)
                        .classroom(room)
                        .batch(batch)
                        .status("GENERATED")
                        .build();

                // Check conflict before adding
                if (!hasConflict(candidate)) {
                    generated.add(timetableEntryRepository.save(candidate));
                }
            }
        }
        return generated;
    }
}
