package com.isga.app.service;

import com.isga.app.domain.Student;
import com.isga.app.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() { return studentRepository.findAll(); }
    public Student findById(Long id) { return studentRepository.findById(id).orElseThrow(); }
    public Student create(Student s) { return studentRepository.save(s); }
    public Student update(Long id, Student s) {
        Student existing = findById(id);
        existing.setName(s.getName());
        return studentRepository.save(existing);
    }
    public void delete(Long id) { studentRepository.deleteById(id); }
}
