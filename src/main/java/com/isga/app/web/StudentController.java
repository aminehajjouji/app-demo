package com.isga.app.web;

import com.isga.app.domain.Student;
import com.isga.app.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> all() { return studentService.findAll(); }

    @GetMapping("/{id}")
    public Student one(@PathVariable Long id) { return studentService.findById(id); }

    @PostMapping
    public ResponseEntity<Student> create(@RequestBody Student s) { return ResponseEntity.ok(studentService.create(s)); }

    @PutMapping("/{id}")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student s) { return ResponseEntity.ok(studentService.update(id, s)); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) { studentService.delete(id); return ResponseEntity.noContent().build(); }
}
