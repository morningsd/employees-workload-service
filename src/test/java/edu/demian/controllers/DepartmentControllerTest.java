package edu.demian.controllers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.demian.configs.JpaConfig;
import edu.demian.services.DepartmentService;
import javax.servlet.ServletContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

public class DepartmentControllerTest {

  private AutoCloseable openMocks;

  private MockMvc mockMvc;

  @Mock
  private DepartmentService departmentService;

  @InjectMocks
  private DepartmentController departmentController;

  @BeforeEach
  public void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(departmentController).build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

}

//@ExtendWith(SpringExtension.class)
//@ContextConfiguration(classes = {JpaConfig.class})
//@WebAppConfiguration(value = "src/main/java/edu/demian/configs")
//public class DepartmentControllerTest {
//
//  @Autowired private WebApplicationContext webApplicationContext;
//
//  private MockMvc mockMvc;
//
//  @BeforeEach
//  public void setUp() {
//    mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//  }
//
//  @Test
//  public void testContext_ServletContextAvailable_ItProvidesDepartmentController() {
//    ServletContext servletContext = webApplicationContext.getServletContext();
//
//    assertNotNull(servletContext);
//    assertTrue(servletContext instanceof MockServletContext);
//    assertNotNull(webApplicationContext.getBean("departmentController"));
//  }
//}
