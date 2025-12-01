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

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.studentmanagement.entities.Enrollment;
import tn.esprit.studentmanagement.entities.Status;
import tn.esprit.studentmanagement.services.IEnrollment;

@SpringBootTest
@AutoConfigureMockMvc
public class EnrollementManagementTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IEnrollment enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllEnrollments() throws Exception {
        List<Enrollment> enrollments = List.of(
                new Enrollment(1L, LocalDate.of(2025, 1, 10), 95.0, Status.ACTIVE,null,null),
                new Enrollment(2L, LocalDate.of(2025, 2, 5), 88.5, Status.COMPLETED,null,null)
        );

        when(enrollmentService.getAllEnrollments()).thenReturn(enrollments);

        mockMvc.perform(get("/Enrollment/getAllEnrollment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(enrollments.size()))
                .andExpect(jsonPath("$[0].grade").value(95.0))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));
    }

    @Test
    void testGetEnrollmentById() throws Exception {
        Enrollment enrollment = new Enrollment(1L, LocalDate.of(2025, 1, 10), 95.0, Status.ACTIVE,null,null);
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(enrollment);

        mockMvc.perform(get("/Enrollment/getEnrollment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grade").value(95.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testCreateEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment(null, LocalDate.of(2025, 1, 10), 95.0, Status.ACTIVE,null,null);
        Enrollment savedEnrollment = new Enrollment(1L, LocalDate.of(2025, 1, 10), 95.0, Status.ACTIVE,null,null);

        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(savedEnrollment);

        mockMvc.perform(post("/Enrollment/createEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnrollment").value(1L))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void testUpdateEnrollment() throws Exception {
        Enrollment enrollment = new Enrollment(1L, LocalDate.of(2025, 1, 10), 95.0, Status.ACTIVE,null,null);
        when(enrollmentService.saveEnrollment(any(Enrollment.class))).thenReturn(enrollment);

        mockMvc.perform(put("/Enrollment/updateEnrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEnrollment").value(1L))
                .andExpect(jsonPath("$.grade").value(95.0));
    }

    @Test
    void testDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).deleteEnrollment(1L);

        mockMvc.perform(delete("/Enrollment/deleteEnrollment/{id}", 1L))
                .andExpect(status().isOk());

        verify(enrollmentService, times(1)).deleteEnrollment(1L);
    }
}
