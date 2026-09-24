package com.srto.controller;

import com.srto.entity.SrtoEntities.*;
import com.srto.repository.SrtoRepositories.*;
import com.srto.service.SrtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SrtoControllers {

    private final SrtoService srtoService;
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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        model.addAttribute("stats", srtoService.getDashboardStats());
        model.addAttribute("notifications", notificationRepository.findTop10ByOrderByTimestampDesc());
        model.addAttribute("recentTimetables", timetableEntryRepository.findAll());
        return "dashboard";
    }

    // --- Department Controller ---
    @GetMapping("/departments")
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("newDepartment", new Department());
        return "departments";
    }

    @PostMapping("/departments/add")
    public String addDepartment(@ModelAttribute Department department, RedirectAttributes ra) {
        departmentRepository.save(department);
        ra.addFlashAttribute("successMsg", "Department created successfully!");
        return "redirect:/departments";
    }

    // --- Course Controller ---
    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("newCourse", new Course());
        return "courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute Course course, RedirectAttributes ra) {
        courseRepository.save(course);
        ra.addFlashAttribute("successMsg", "Course added successfully!");
        return "redirect:/courses";
    }

    // --- Subject Controller ---
    @GetMapping("/subjects")
    public String listSubjects(Model model) {
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("newSubject", new Subject());
        return "subjects";
    }

    @PostMapping("/subjects/add")
    public String addSubject(@ModelAttribute Subject subject, RedirectAttributes ra) {
        subjectRepository.save(subject);
        ra.addFlashAttribute("successMsg", "Subject saved successfully!");
        return "redirect:/subjects";
    }

    // --- Faculty Controller ---
    @GetMapping("/faculty")
    public String listFaculty(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("departments", departmentRepository.findAll());
        model.addAttribute("newFaculty", new Faculty());
        return "faculty";
    }

    @PostMapping("/faculty/add")
    public String addFaculty(@ModelAttribute Faculty faculty, RedirectAttributes ra) {
        facultyRepository.save(faculty);
        ra.addFlashAttribute("successMsg", "Faculty member added successfully!");
        return "redirect:/faculty";
    }

    // --- Classroom Controller ---
    @GetMapping("/classrooms")
    public String listClassrooms(Model model) {
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("newClassroom", new Classroom());
        return "classrooms";
    }

    @PostMapping("/classrooms/add")
    public String addClassroom(@ModelAttribute Classroom classroom, RedirectAttributes ra) {
        classroomRepository.save(classroom);
        ra.addFlashAttribute("successMsg", "Room/Lab registered successfully!");
        return "redirect:/classrooms";
    }

    // --- Academic Structure Controller ---
    @GetMapping("/academic")
    public String academicStructure(Model model) {
        model.addAttribute("academicYears", academicYearRepository.findAll());
        model.addAttribute("semesters", semesterRepository.findAll());
        model.addAttribute("batches", studentBatchRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        model.addAttribute("newBatch", new StudentBatch());
        return "academic";
    }

    @PostMapping("/academic/batch/add")
    public String addBatch(@ModelAttribute StudentBatch batch, RedirectAttributes ra) {
        studentBatchRepository.save(batch);
        ra.addFlashAttribute("successMsg", "Student Batch added successfully!");
        return "redirect:/academic";
    }

    // --- Timetable Generation & Optimization Controller ---
    @GetMapping("/timetable")
    public String viewTimetable(Model model) {
        model.addAttribute("entries", timetableEntryRepository.findAll());
        model.addAttribute("batches", studentBatchRepository.findAll());
        model.addAttribute("faculties", facultyRepository.findAll());
        model.addAttribute("classrooms", classroomRepository.findAll());
        model.addAttribute("subjects", subjectRepository.findAll());
        return "timetable";
    }

    @PostMapping("/timetable/generate")
    public String generateTimetable(@RequestParam("batchId") Long batchId, RedirectAttributes ra) {
        List<TimetableEntry> generated = srtoService.generateAutomatedTimetable(batchId);
        ra.addFlashAttribute("successMsg", "Generated " + generated.size() + " optimized timetable slots dynamically!");
        return "redirect:/timetable";
    }

    // --- Conflicts & Workload ---
    @GetMapping("/conflicts")
    public String viewConflicts(Model model) {
        List<TimetableEntry> entries = timetableEntryRepository.findAll();
        List<TimetableEntry> conflictingEntries = entries.stream()
                .filter(srtoService::hasConflict)
                .toList();

        model.addAttribute("conflicts", conflictingEntries);
        model.addAttribute("totalChecked", entries.size());
        return "conflicts";
    }

    @GetMapping("/workload")
    public String viewWorkload(Model model) {
        model.addAttribute("faculties", facultyRepository.findAll());
        return "workload";
    }

    // --- Notifications, Analytics, Settings ---
    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("notifications", notificationRepository.findAll());
        return "notifications";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("stats", srtoService.getDashboardStats());
        return "reports";
    }

    @GetMapping("/settings")
    public String settings(Model model) {
        model.addAttribute("settings", settingRepository.findAll());
        return "settings";
    }
}
