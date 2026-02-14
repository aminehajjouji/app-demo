package com.isga.app.service;

import com.isga.app.domain.Student;
import com.isga.app.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setName("Amine Hajjouji");
    }

    @Test
    @DisplayName("Should find all students successfully")
    void shouldFindAllStudents() {
        // Given
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Amine");

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Mohamed");

        List<Student> expectedStudents = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // When
        List<Student> actualStudents = studentService.findAll();

        // Then
        assertNotNull(actualStudents);
        assertEquals(2, actualStudents.size());
        assertEquals("Amine", actualStudents.get(0).getName());
        assertEquals("Mohamed", actualStudents.get(1).getName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should create student successfully")
    void shouldCreateStudent() {
        // Given
        Student newStudent = new Student();
        newStudent.setName("Hassan");

        Student savedStudent = new Student();
        savedStudent.setId(3L);
        savedStudent.setName("Hassan");

        when(studentRepository.save(any(Student.class))).thenReturn(savedStudent);

        // When
        Student result = studentService.create(newStudent);

        // Then
        assertNotNull(result);
        assertEquals(3L, result.getId());
        assertEquals("Hassan", result.getName());
        verify(studentRepository, times(1)).save(newStudent);
    }

    @Test
    @DisplayName("Should find student by id successfully")
    void shouldFindStudentById() {
        // Given
        Long studentId = 1L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));

        // When
        Student result = studentService.findById(studentId);

        // Then
        assertNotNull(result);
        assertEquals(studentId, result.getId());
        assertEquals("Amine Hajjouji", result.getName());
        verify(studentRepository, times(1)).findById(studentId);
    }
}
