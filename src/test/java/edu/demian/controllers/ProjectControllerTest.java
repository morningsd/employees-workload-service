package edu.demian.controllers;

import static edu.demian.controllers.DepartmentControllerIntegrationTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.demian.entities.Project;
import edu.demian.services.ProjectService;
import java.util.Arrays;
import java.util.List;
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

public class ProjectControllerTest {

  private AutoCloseable openMocks;

  private MockMvc mockMvc;

  @Mock private ProjectService projectService;

  @InjectMocks private ProjectController projectController;

  @BeforeEach
  public void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(projectController).build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  final void testFindAll_DataGiven_ReturnObjects() throws Exception {
    List<Project> projects =
        Arrays.asList(
            Project.builder().name("project_name1").description("project_description1").build(),
            Project.builder().name("project_name2").description("project_description2").build());

    when(projectService.findAll()).thenReturn(projects);
    mockMvc
        .perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].name", is("project_name1")))
        .andExpect(jsonPath("$[1].description", is("project_description2")));
    verify(projectService, times(1)).findAll();
    verifyNoMoreInteractions(projectService);
  }

  @Test
  final void testFindById_InstanceExists_ReturnObject() throws Exception {
    UUID uuid = UUID.randomUUID();
    Project project =
        Project.builder().id(uuid).name("project_name").description("project_description").build();
    when(projectService.findById(uuid)).thenReturn(project);

    mockMvc
        .perform(MockMvcRequestBuilders.get("/projects/{id}", project.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("project_name")))
        .andExpect(jsonPath("$.description", is("project_description")));
    verify(projectService, times(1)).findById(uuid);
    verifyNoMoreInteractions(projectService);
  }

  @Test
  final void testReplaceProject_InstanceExists_ReturnUpdatedObject() throws Exception {
    UUID uuid = UUID.randomUUID();
    Project project =
        Project.builder().id(uuid).name("project_name").description("project_description").build();
    when(projectService.replace(project, uuid)).thenReturn(project);

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/projects/{id}", project.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(project)))
        .andExpect(status().isOk());
    verify(projectService, times(1)).replace(project, uuid);
  }

//  @Test
//  final void testPartialReplaceProject_InstanceExists_ReturnPartlyUpdatedObject() throws Exception {
//    UUID uuid = UUID.randomUUID();
//    Project project =
//        Project.builder().id(uuid).name("project_name").description("project_description").build();
//    Map<String, Object> partialUpdates = new HashMap<>();
//    when(projectService.partialReplace(any(), any())).thenReturn(project);
//
//    mockMvc
//        .perform(
//            MockMvcRequestBuilders.patch("/projects/{id}", project.getId())
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(asJsonString(partialUpdates)))
//        .andExpect(status().isOk());
//    verify(projectService, times(1)).partialReplace(partialUpdates, project.getId());
//  }

  @Test
  final void testDelete_InstanceExists_ReturnNothing() throws Exception {
    UUID uuid = UUID.randomUUID();
    Project project =
        Project.builder().id(uuid).name("project_name").description("project_description").build();
    doNothing().when(projectService).delete(project.getId());

    mockMvc
        .perform(MockMvcRequestBuilders.delete("/projects/{id}", project.getId()))
        .andExpect(status().isOk());
    verify(projectService, times(1)).delete(project.getId());
  }
}
