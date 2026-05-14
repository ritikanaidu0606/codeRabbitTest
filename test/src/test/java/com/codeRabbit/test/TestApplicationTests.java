package com.codeRabbit.test;

import com.codeRabbit.test.model.Student;
import com.codeRabbit.test.repository.StudentRepository;
import com.codeRabbit.test.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TestApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        testStudent = new Student();
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john@example.com");
        testStudent.setAge(20);
    }

    // Test Case 1: Application context loads successfully
    @Test
    void contextLoads() {
        assertNotNull(studentRepository);
        assertNotNull(studentService);
    }

    // Test Case 2: Save student
    @Test
    void testSaveStudent() {
        Student savedStudent = studentService.saveStudent(testStudent);
        assertNotNull(savedStudent.getId());
        assertEquals("John", savedStudent.getFirstName());
    }

    // Test Case 3: Get all students
    @Test
    void testGetAllStudents() {
        studentService.saveStudent(testStudent);
        assertTrue(studentService.getAllStudents().size() > 0);
    }

    // Test Case 4: Get student by ID
    @Test
    void testGetStudentById() {
        Student saved = studentService.saveStudent(testStudent);
        assertTrue(studentService.getStudentById(saved.getId()).isPresent());
    }

    // Test Case 5: Update student
    @Test
    void testUpdateStudent() {
        Student saved = studentService.saveStudent(testStudent);
        saved.setFirstName("Jane");
        Student updated = studentService.saveStudent(saved);
        assertEquals("Jane", updated.getFirstName());
    }

    // Test Case 6: Delete student
    @Test
    void testDeleteStudent() {
        Student saved = studentService.saveStudent(testStudent);
        studentService.deleteStudent(saved.getId());
        assertFalse(studentService.getStudentById(saved.getId()).isPresent());
    }

    // Test Case 7: Create student via API
    @Test
    void testCreateStudentAPI() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType("application/json")
                .content("{\"firstName\":\"Jane\",\"lastName\":\"Smith\",\"email\":\"jane@example.com\",\"age\":21}"))
                .andExpect(status().isOk());
    }

    // Test Case 8: Get all students via API
    @Test
    void testGetAllStudentsAPI() throws Exception {
        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    // Test Case 9: Get student by ID via API
    @Test
    void testGetStudentByIdAPI() throws Exception {
        Student saved = studentService.saveStudent(testStudent);
        mockMvc.perform(get("/api/students/" + saved.getId()))
                .andExpect(status().isOk());
    }

    // Test Case 10: Update student via API
    @Test
    void testUpdateStudentAPI() throws Exception {
        Student saved = studentService.saveStudent(testStudent);
        mockMvc.perform(put("/api/students/" + saved.getId())
                .contentType("application/json")
                .content("{\"firstName\":\"Updated\",\"lastName\":\"Name\",\"email\":\"updated@example.com\",\"age\":22}"))
                .andExpect(status().isOk());
    }

    // Test Case 11: Delete student via API
    @Test
    void testDeleteStudentAPI() throws Exception {
        Student saved = studentService.saveStudent(testStudent);
        mockMvc.perform(delete("/api/students/" + saved.getId()))
                .andExpect(status().isNoContent());
    }
}