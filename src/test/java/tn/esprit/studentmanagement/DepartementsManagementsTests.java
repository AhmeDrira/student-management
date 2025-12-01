package tn.esprit.studentmanagement;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.studentmanagement.entities.Department;
import tn.esprit.studentmanagement.services.IDepartmentService;

@SpringBootTest
@AutoConfigureMockMvc
public class DepartementsManagementsTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IDepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllDepartments() throws Exception {
        List<Department> departments = List.of(
                new Department(1L, "CS", "Building A", "111-222", "Dr. Smith", null),
                new Department(2L, "Math", "Building B", "333-444", "Dr. Jones", null)
        );

        when(departmentService.getAllDepartments()).thenReturn(departments);

        mockMvc.perform(get("/Depatment/getAllDepartment"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(departments.size()))
                .andExpect(jsonPath("$[0].name").value("CS"))
                .andExpect(jsonPath("$[1].head").value("Dr. Jones"));
    }

    @Test
    void testGetDepartmentById() throws Exception {
        Department dept = new Department(1L, "CS", "Building A", "111-222", "Dr. Smith", null);
        when(departmentService.getDepartmentById(1L)).thenReturn(dept);

        mockMvc.perform(get("/Depatment/getDepartment/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("CS"))
                .andExpect(jsonPath("$.head").value("Dr. Smith"));
    }

    @Test
    void testCreateDepartment() throws Exception {
        Department dept = new Department(null, "CS", "Building A", "111-222", "Dr. Smith", null);
        Department savedDept = new Department(1L, "CS", "Building A", "111-222", "Dr. Smith", null);

        when(departmentService.saveDepartment(any(Department.class))).thenReturn(savedDept);

        mockMvc.perform(post("/Depatment/createDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1L))
                .andExpect(jsonPath("$.name").value("CS"));
    }

    @Test
    void testUpdateDepartment() throws Exception {
        Department dept = new Department(1L, "CS", "Building A", "111-222", "Dr. Smith", null);
        when(departmentService.saveDepartment(any(Department.class))).thenReturn(dept);

        mockMvc.perform(put("/Depatment/updateDepartment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dept)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idDepartment").value(1L))
                .andExpect(jsonPath("$.head").value("Dr. Smith"));
    }

    @Test
    void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteDepartment(1L);

        mockMvc.perform(delete("/Depatment/deleteDepartment/{id}", 1L))
                .andExpect(status().isOk());

        verify(departmentService, times(1)).deleteDepartment(1L);
    }
}
