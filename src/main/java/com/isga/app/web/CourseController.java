package com.isga.app.web;

import com.isga.app.domain.Course;
import com.isga.app.service.CourseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public List<Course> all() { return courseService.findAll(); }

    @GetMapping("/{id}")
    public Course one(@PathVariable Long id) { return courseService.findById(id); }

    @GetMapping("/student/{studentId}")
    public List<Course> byStudent(@PathVariable Long studentId) { return courseService.findByStudent(studentId); }

    @PostMapping("/student/{studentId}")
    public ResponseEntity<Course> createForStudent(@PathVariable Long studentId, @RequestBody Course c) { return ResponseEntity.ok(courseService.createForStudent(studentId, c)); }

    @PutMapping("/{id}")
    public ResponseEntity<Course> update(@PathVariable Long id, @RequestBody Course c) { return ResponseEntity.ok(courseService.update(id, c)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { courseService.delete(id); return ResponseEntity.noContent().build(); }
}
