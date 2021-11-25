package edu.demian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.demian.entities.Department;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.impl.DepartmentServiceImpl;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class DepartmentServiceImplTest {

  AutoCloseable openMocks;

  @InjectMocks DepartmentServiceImpl departmentService;

  @Mock DepartmentRepository departmentRepository;

  Department stub;

  @BeforeEach
  void setUp() {
    openMocks = MockitoAnnotations.openMocks(this);
    stub = Department.builder().name("department1").description("description1").build();
  }

  @AfterEach
  void tearDown() throws Exception {
    openMocks.close();
  }

  @Test
  final void testSave_DepartmentIsAlreadyExists_ExceptionThrown() {
    when(departmentRepository.findByName(anyString())).thenReturn(java.util.Optional.of(stub));

    assertThrows(
        ServiceException.class,
        () -> departmentService.save(stub),
        "There shouldn't be 2 equal department objects");
  }

  @Test
  final void testReplace_DepartmentIsAlreadyExists_ReplaceObject() {
    Department stub2 =
        Department.builder()
            .name("department1_updated")
            .description("description1_updated")
            .build();

    when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(stub));
    when(departmentRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

    Department actual = departmentService.replace(stub2, null);

    assertNotNull(actual);
    assertEquals("department1_updated", actual.getName());
    assertEquals("description1_updated", actual.getDescription());
  }

  @Test
  final void testSave_NoSuchDepartmentYet_ReturnDepartment() {
    when(departmentRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(departmentRepository.save(any())).thenReturn(stub);

    Department actual = departmentService.save(stub);

    assertNotNull(actual);
    assertEquals("department1", actual.getName());
  }

  @Test
  final void testPartialReplace_DepartmentIsAlreadyExists_ReplaceGivenFields() {
    Map<String, Object> partialUpdates = new HashMap<>();
    partialUpdates.put("description", "description1_updated");

    when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(stub));
    when(departmentRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());

    Department actual = departmentService.partialReplace(partialUpdates, null);

    assertNotNull(actual);
    assertEquals("department1", actual.getName());
    assertEquals("description1_updated", actual.getDescription());
  }
}
