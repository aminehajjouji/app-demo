package com.isga.app.service;

import com.isga.app.domain.Course;
import com.isga.app.domain.Student;
import com.isga.app.repository.CourseRepository;
import com.isga.app.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {
    private final CourseRepository courseRepository;

    @Autowired
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    public List<Course> findAll() { return courseRepository.findAll(); }
    public Course findById(Long id) { return courseRepository.findById(id).orElseThrow(); }
    public List<Course> findByStudent(Long studentId) { return courseRepository.findByStudentId(studentId); }

    public Course createForStudent(Long studentId, Course c) {
        Student student = studentRepository.findById(studentId).orElseThrow();
        c.setStudent(student);
        return courseRepository.save(c);
    }

    public Course update(Long id, Course c) {
        Course existing = findById(id);
        existing.setTitle(c.getTitle());
        return courseRepository.save(existing);
    }

    public void delete(Long id) { courseRepository.deleteById(id); }
}
