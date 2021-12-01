package edu.demian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.demian.entities.Project;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.repositories.ProjectRepository;
import edu.demian.services.impl.ProjectServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceImplTest {

  @InjectMocks private ProjectServiceImpl projectService;

  @Mock private ProjectRepository projectRepository;

  private Project stub;

  @BeforeEach
  void setUp() {
    stub = Project.builder().name("project1").description("description1").build();
  }

  @Test
  final void save_ProjectIsAlreadyExists_ThrowException() {
    when(projectRepository.findByName(anyString())).thenReturn(Optional.of(stub));

    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> projectService.save(stub),
        "There shouldn't be 2 equal project objects");
  }

  @Test
  final void replace_ProjectIsAlreadyExists_ReplaceInstance() {
    Project stub2 =
        Project.builder().name("project1_updated").description("description1_updated").build();

    when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

    Project actual = projectService.replace(stub2, null);

    assertNotNull(actual);
    assertEquals("project1_updated", actual.getName());
    assertEquals("description1_updated", actual.getDescription());
  }

  @Test
  final void save_NoSuchProjectYet_ReturnSavedInstance() {
    when(projectRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(projectRepository.save(any())).thenReturn(stub);

    Project actual = projectService.save(stub);

    assertNotNull(actual);
    assertEquals("project1", actual.getName());
  }

  @Test
  final void partialReplace_ProjectIsAlreadyExists_ReplaceGivenFields() {
    Map<String, Object> partialUpdates = new HashMap<>();
    partialUpdates.put("description", "description1_partially_updated");

    when(projectRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

    Project actual = projectService.partialReplace(partialUpdates, null);

    assertNotNull(actual);
    assertEquals("project1", actual.getName());
    assertEquals("description1_partially_updated", actual.getDescription());
  }
}
