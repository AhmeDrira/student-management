package tn.esprit.studentmanagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.entities.Student;
import tn.esprit.studentmanagement.services.IStudentService;


@SpringBootTest
@AutoConfigureMockMvc
class StudentManagementApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IStudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testGetAllStudents() throws Exception {
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Computer Science");
        dept.setLocation("Building A");
        dept.setPhone("111-222-333");
        dept.setHead("Dr. Smith");
        List<Student> students = List.of(
                new Student(1L, "John", "Doe", "john@example.com", "1234567890",
                        LocalDate.of(2000, 1, 1), "123 Street", dept, null),
                new Student(2L, "Jane", "Smith", "jane@example.com", "0987654321",
                        LocalDate.of(2001, 2, 2), "456 Avenue", dept, null)
        );

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/students/getAllStudents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(students.size()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].email").value("jane@example.com"));
    }

    @Test
    void testGetStudentById() throws Exception {
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Computer Science");
        dept.setLocation("Building A");
        dept.setPhone("111-222-333");
        dept.setHead("Dr. Smith");
        Student student = new Student(1L, "John", "Doe", "john@example.com", "1234567890",
                LocalDate.of(2000, 1, 1), "123 Street", dept, null);

        when(studentService.getStudentById(1L)).thenReturn(student);

        mockMvc.perform(get("/students/getStudent/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"));
    }

    @Test
    void testCreateStudent() throws Exception {
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Computer Science");
        dept.setLocation("Building A");
        dept.setPhone("111-222-333");
        dept.setHead("Dr. Smith");
        Student student = new Student(null, "John", "Doe", "john@example.com", "1234567890",
                LocalDate.of(2000, 1, 1), "123 Street", dept, null);

        Student savedStudent = new Student(1L, "John", "Doe", "john@example.com", "1234567890",
                LocalDate.of(2000, 1, 1), "123 Street", dept, null);

        when(studentService.saveStudent(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/students/createStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testUpdateStudent() throws Exception {
        Department dept = new Department();
        dept.setIdDepartment(1L);
        dept.setName("Computer Science");
        dept.setLocation("Building A");
        dept.setPhone("111-222-333");
        dept.setHead("Dr. Smith");
        Student student = new Student(1L, "John", "Doe", "john@example.com", "1234567890",
                LocalDate.of(2000, 1, 1), "123 Street", dept, null);

        when(studentService.saveStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(put("/students/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idStudent").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(delete("/students/deleteStudent/{id}", 1L))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }
}
