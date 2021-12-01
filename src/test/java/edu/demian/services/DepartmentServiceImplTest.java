package edu.demian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.demian.entities.Department;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.impl.DepartmentServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

  @InjectMocks private DepartmentServiceImpl departmentService;

  @Mock private DepartmentRepository departmentRepository;

  private Department stub;

  @BeforeEach
  void setUp() {
    stub = Department.builder().name("department1").description("description1").build();
  }

  @Test
  final void save_DepartmentIsAlreadyExists_ThrowException() {
    when(departmentRepository.findByName(anyString())).thenReturn(Optional.of(stub));

    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> departmentService.save(stub),
        "There shouldn't be 2 equal department objects");
  }

  @Test
  final void replace_DepartmentIsAlreadyExists_ReplaceInstance() {
    Department stub2 =
        Department.builder()
            .name("department1_updated")
            .description("description1_updated")
            .build();

    when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

    Department actual = departmentService.replace(stub2, null);

    assertNotNull(actual);
    assertEquals("department1_updated", actual.getName());
    assertEquals("description1_updated", actual.getDescription());
  }

  @Test
  final void save_NoSuchDepartmentYet_ReturnSavedInstance() {
    when(departmentRepository.findByName(anyString())).thenReturn(Optional.empty());
    when(departmentRepository.save(any())).thenReturn(stub);

    Department actual = departmentService.save(stub);

    assertNotNull(actual);
    assertEquals("department1", actual.getName());
  }

    @Test
    final void partialReplace_DepartmentIsAlreadyExists_ReplaceGivenFields() {
      Department department = Department.builder().description("description1_partially_updated").build();

      when(departmentRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

      Department actual = departmentService.partialReplace(department, null);

      assertNotNull(actual);
      assertEquals("department1", actual.getName());
      assertEquals("description1_partially_updated", actual.getDescription());
    }
}
