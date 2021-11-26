package edu.demian.controllers;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.entities.Department;
import edu.demian.services.DepartmentService;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class DepartmentControllerTest {

  private AutoCloseable openMocks;

  private MockMvc mockMvc;

  @Mock private DepartmentService departmentService;

  @InjectMocks private DepartmentController departmentController;

  @BeforeEach
  public void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  final void testFindAll_DataGiven_AllIsOk() throws Exception {
    List<Department> departments =
        Arrays.asList(
            Department.builder().name("department1").description("description1").build(),
            Department.builder().name("department2").description("description2").build());

    when(departmentService.findAll()).thenReturn(departments);
    mockMvc
        .perform(MockMvcRequestBuilders.get("/departments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("department1")))
        .andExpect(jsonPath("$[1].description", is("description2")));
    verify(departmentService, times(1)).findAll();
    verifyNoMoreInteractions(departmentService);
  }

  @Test
  final void testFindById_InstanceExists_AllIsOk() throws Exception {
    UUID uuid = UUID.randomUUID();
    Department department =
        Department.builder().id(uuid).name("departmentWithId").description("description").build();
    when(departmentService.findById(uuid)).thenReturn(Optional.ofNullable(department));

    mockMvc
        .perform(MockMvcRequestBuilders.get("/departments/{id}", department.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("departmentWithId")))
        .andExpect(jsonPath("$.description", is("description")));
    verify(departmentService, times(1)).findById(uuid);
    verifyNoMoreInteractions(departmentService);
  }

  @Test
  final void testReplaceDepartment_InstanceExists_AllIsOk() throws Exception {
    UUID uuid = UUID.randomUUID();
    Department department =
        Department.builder()
            .id(uuid)
            .name("nameToReplace")
            .description("descriptionToReplace")
            .build();
    when(departmentService.replace(department, uuid)).thenReturn(department);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(department)))
        .andExpect(status().isOk());
    verify(departmentService, times(1)).replace(department, uuid);
  }

  @Test
  final void testPartialReplaceDepartment_InstanceExists_AllIsOk() throws Exception {
    UUID uuid = UUID.randomUUID();
    Department department =
        Department.builder()
            .id(uuid)
            .name("department_name")
            .description("department_description")
            .build();
    Map<String, Object> partialUpdates = new HashMap<>();
    when(departmentService.partialReplace(any(), any())).thenReturn(department);

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/departments/{id}", department.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partialUpdates)))
        .andExpect(status().isOk());
    verify(departmentService, times(1)).partialReplace(partialUpdates, department.getId());
  }

  @Test
  final void testDelete_InstanceExists_AllIsOk() throws Exception {
    UUID uuid = UUID.randomUUID();
    Department department =
        Department.builder()
            .id(uuid)
            .name("department_name")
            .description("department_description")
            .build();
    doNothing().when(departmentService).delete(department.getId());

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/departments/{id}", department.getId()))
        .andExpect(status().isOk());
    verify(departmentService, times(1)).delete(department.getId());
  }

  private static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
