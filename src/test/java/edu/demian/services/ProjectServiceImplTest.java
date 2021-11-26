package edu.demian.services;

import edu.demian.entities.Project;
import edu.demian.repositories.ProjectRepository;
import edu.demian.services.impl.ProjectServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProjectServiceImplTest {

  private AutoCloseable openMocks;

  @InjectMocks
  private ProjectServiceImpl projectService;

  @Mock
  private ProjectRepository projectRepository;

  private Project stub;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    stub = Project.builder().name("project1").description("description1").build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  final void testSave_ProjectIsAlreadyExists_ExceptionThrown() {
    
  }
}
