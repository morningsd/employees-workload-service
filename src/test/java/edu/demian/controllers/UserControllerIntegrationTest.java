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
import edu.demian.entities.User;
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
public class UserControllerIntegrationTest {

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private UserController userController;

  private MockMvc mockMvc;
  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(userController)
        .setControllerAdvice(new GlobalControllerExceptionHandler())
        .build();
  }

  @Test
  public void webApplicationContext_GetDepartmentControllerBean_ItExists() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(webApplicationContext.getBean("userController"));
  }

  @Test
  final void findAll_DataExists_ReturnInstances() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(4)))
        .andExpect(jsonPath("$[0].firstName", is("first1_h2")))
        .andExpect(jsonPath("$[1].lastName", is("last2_h2")));
  }

  @Test
  final void findById_DataExists_ReturnInstance() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.get("/users/{id}", "6f597682-fd20-4676-a94c-85f20dbff099"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName", is("first1_h2")))
        .andExpect(jsonPath("$.lastName", is("last1_h2")));
  }

  @Test
  final void replaceDepartment_DataExists_ReturnUpdatedInstance() throws Exception {
    User user =
        User.builder()
            .firstName("first2_h2_updated")
            .lastName("last2_h2_updated")
            .email("email2_updated@gmail.com")
            .password("password2_updated")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.put("/users/{id}", "7f597682-fd20-4676-a94c-85f20dbff099")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName", is("first2_h2_updated")))
        .andExpect(jsonPath("$.lastName", is("last2_h2_updated")));
  }

  @Test
  final void partialReplaceDepartment_DataExists_ReturnPartlyUpdatedInstance() throws Exception {
    User user =
        User.builder()
            .lastName("last3_h2_updated_partially")
            .build();

    mockMvc
        .perform(
            MockMvcRequestBuilders.patch("/users/{id}","8f597682-fd20-4676-a94c-85f20dbff099")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.firstName", is("first3_h2")))
        .andExpect(jsonPath("$.lastName", is("last3_h2_updated_partially")));
  }

  @Test
  final void delete_DataExists_ReturnNothing() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/users/{id}", "9f597682-fd20-4676-a94c-85f20dbff099"))
        .andExpect(status().isOk());
  }

}
