package com.isga.app;

import com.isga.app.domain.Student;
import com.isga.app.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@SpringBootTest
class StudentRepositoryTests {

    @Autowired
    StudentRepository studentRepository;

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> "jdbc:h2:mem:testdb" );
        registry.add("spring.datasource.driver-class-name", () -> "org.h2.Driver" );
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop" );
    }

    @Test
    void saveAndFind() {
        Student s = new Student();
        s.setName("Alice");
        Student saved = studentRepository.save(s);
        assert saved.getId() != null;
        Student fetched = studentRepository.findById(saved.getId()).orElseThrow();
        assert "Alice".equals(fetched.getName());
    }
}
