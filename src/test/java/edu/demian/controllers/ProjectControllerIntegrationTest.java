package edu.demian.controllers;

import static edu.demian.controllers.DepartmentControllerIntegrationTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.demian.configs.ApplicationConfig;
import edu.demian.configs.H2JpaConfig;
import edu.demian.entities.Project;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith({MockitoExtension.class, SpringExtension.class})
@ContextConfiguration(classes = {ApplicationConfig.class, H2JpaConfig.class})
@WebAppConfiguration
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("classpath:test-data.sql")
public class ProjectControllerIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private ProjectController projectController;

  private MockMvc mockMvc;
  @BeforeEach
  public void setUp() {
    mockMvc =
        MockMvcBuilders
            .standaloneSetup(projectController)
            .setControllerAdvice(new GlobalControllerExceptionHandler())
            .build();
  }

  @Test
  public void webApplicationContext_GetDepartmentControllerBean_ItExists() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(webApplicationContext.getBean("projectController"));
  }

  @Test
  final void findAll_DataExists_ReturnInstances() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/projects"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].name", is("project1_h2")))
        .andExpect(jsonPath("$[1].description", is("description2_h2")));
  }

  @Test
  final void findById_DataExists_ReturnInstance() throws Exception {
    mockMvc
        .perform(
            MockMvcRequestBuilders.get("/projects/{id}", "6d6a78e9-7d10-41da-838a-6266d63a9d44"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("project1_h2")))
        .andExpect(jsonPath("$.description", is("description1_h2")));
  }

  @Test
  final void replaceProject_DataExists_ReturnUpdatedInstance() throws Exception {
    Project project =
        Project.builder()
            .name("project2_h2_updated")
            .description("description2_h2_updated")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/projects/{id}", "7d6a78e9-7d10-41da-838a-6266d63a9d44")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(project)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("project2_h2_updated")))
        .andExpect(jsonPath("$.description", is("description2_h2_updated")));
  }

  @Test
  final void partialReplaceProject_DataExists_ReturnPartlyUpdatedInstance() throws Exception {
    Map<String, Object> partialUpdates = new HashMap<>();
    partialUpdates.put("description", "description3_h2_updated_partially");

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/projects/{id}", "8d6a78e9-7d10-41da-838a-6266d63a9d44")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partialUpdates)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("project3_h2")))
        .andExpect(jsonPath("$.description", is("description3_h2_updated_partially")));
  }

  @Test
  final void delete_DataExists_ReturnNothing() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/projects/{id}", "9d6a78e9-7d10-41da-838a-6266d63a9d44"))
        .andExpect(status().isOk());
  }
}
