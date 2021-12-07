package edu.demian.controllers;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.configs.ApplicationConfig;
import edu.demian.configs.H2JpaConfig;
import edu.demian.entities.Department;
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
public class DepartmentControllerIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

//  @Autowired
//  private FilterChainProxy springSecurityFilterChain;

  @Autowired
  private DepartmentController departmentController;

  private MockMvc mockMvc;
  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders
//        .webAppContextSetup(webApplicationContext)
        .standaloneSetup(departmentController)
        .setControllerAdvice(new GlobalControllerExceptionHandler())
//        .addFilter(springSecurityFilterChain)
        .build();
  }

  @Test
  public void webApplicationContext_GetDepartmentControllerBean_ItExists() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(webApplicationContext.getBean("departmentController"));
  }

  @Test
  final void findAll_DataExists_ReturnInstances() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/departments"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].name", is("department1_h2")))
        .andExpect(jsonPath("$[1].description", is("description2_h2")));
  }

  @Test
  final void findById_DataExists_ReturnInstance() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/departments/{id}", "65123e14-e418-459e-91c0-4712fdb5170a"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("department1_h2")))
        .andExpect(jsonPath("$.description", is("description1_h2")));
  }

  @Test
  final void replaceDepartment_DataExists_ReturnUpdatedInstance() throws Exception {
    Department department =
        Department.builder()
            .name("department2_h2_updated")
            .description("description2_h2_updated")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/departments/{id}", "75123e14-e418-459e-91c0-4712fdb5170a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(department)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("department2_h2_updated")))
        .andExpect(jsonPath("$.description", is("description2_h2_updated")));
  }

  @Test
  final void partialReplaceDepartment_DataExists_ReturnPartlyUpdatedInstance() throws Exception {
    Map<String, Object> partialUpdates = new HashMap<>();
    partialUpdates.put("description", "description3_h2_updated_partially");

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/departments/{id}","85123e14-e418-459e-91c0-4712fdb5170a")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(partialUpdates)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.name", is("department3_h2")))
        .andExpect(jsonPath("$.description", is("description3_h2_updated_partially")));
  }

  @Test
  final void delete_DataExists_ReturnNothing() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/departments/{id}", "95123e14-e418-459e-91c0-4712fdb5170a"))
        .andExpect(status().isOk());
  }

  static String asJsonString(Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }
}
