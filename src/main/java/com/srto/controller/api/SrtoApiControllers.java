package com.srto.controller.api;

import com.srto.entity.SrtoEntities.*;
import com.srto.service.SrtoModularServices.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SrtoApiControllers {

    private final DepartmentService departmentService;
    private final CourseService courseService;
    private final SubjectService subjectService;
    private final FacultyService facultyService;
    private final ClassroomService classroomService;
    private final TimetableService timetableService;
    private final DashboardService dashboardService;

    // --- REST APIs: Departments ---
    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestBody Department department) {
        return new ResponseEntity<>(departmentService.saveDepartment(department), HttpStatus.CREATED);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    // --- REST APIs: Courses ---
    @GetMapping("/courses")
    public ResponseEntity<List<Course>> getCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping("/courses")
    public ResponseEntity<Course> createCourse(@RequestBody Course course) {
        return new ResponseEntity<>(courseService.saveCourse(course), HttpStatus.CREATED);
    }

    // --- REST APIs: Subjects ---
    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PostMapping("/subjects")
    public ResponseEntity<Subject> createSubject(@RequestBody Subject subject) {
        return new ResponseEntity<>(subjectService.saveSubject(subject), HttpStatus.CREATED);
    }

    // --- REST APIs: Faculty ---
    @GetMapping("/faculty")
    public ResponseEntity<List<Faculty>> getFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }

    @PostMapping("/faculty")
    public ResponseEntity<Faculty> createFaculty(@RequestBody Faculty faculty) {
        return new ResponseEntity<>(facultyService.saveFaculty(faculty), HttpStatus.CREATED);
    }

    // --- REST APIs: Classrooms ---
    @GetMapping("/classrooms")
    public ResponseEntity<List<Classroom>> getClassrooms() {
        return ResponseEntity.ok(classroomService.getAllClassrooms());
    }

    @PostMapping("/classrooms")
    public ResponseEntity<Classroom> createClassroom(@RequestBody Classroom classroom) {
        return new ResponseEntity<>(classroomService.saveClassroom(classroom), HttpStatus.CREATED);
    }

    // --- REST APIs: Timetable ---
    @GetMapping("/timetable")
    public ResponseEntity<List<TimetableEntry>> getTimetable() {
        return ResponseEntity.ok(timetableService.getAllEntries());
    }

    @PostMapping("/timetable/generate/{batchId}")
    public ResponseEntity<List<TimetableEntry>> generateTimetable(@PathVariable Long batchId) {
        return ResponseEntity.ok(timetableService.generateTimetable(batchId));
    }

    // --- REST APIs: Metrics ---
    @GetMapping("/metrics")
    public ResponseEntity<Map<String, Object>> getMetrics() {
        return ResponseEntity.ok(dashboardService.getDashboardMetrics());
    }
}
