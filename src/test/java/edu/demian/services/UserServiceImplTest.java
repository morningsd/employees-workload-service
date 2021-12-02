package edu.demian.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import edu.demian.entities.Department;
import edu.demian.entities.User;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.repositories.UserRepository;
import edu.demian.services.impl.UserServiceImpl;
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
public class UserServiceImplTest {

  @InjectMocks private UserServiceImpl userService;

  @Mock private UserRepository userRepository;
  @Mock private DepartmentService departmentService;

  private User stub;

  @BeforeEach
  void setUp() {
    stub =
        User.builder()
            .firstName("First")
            .lastName("Last")
            .email("example@gmail.com")
            .password("password")
            .build();
  }

  @Test
  final void save_UserIsAlreadyExists_ThrowException() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(stub));

    assertThrows(
        ResourceAlreadyExistsException.class,
        () -> userService.save(stub, null),
        "There shouldn't be 2 equal user objects");
  }

  @Test
  final void replace_UserIsAlreadyExists_ReplaceInstance() {
    User stub2 =
        User.builder()
            .firstName("First_updated")
            .lastName("Last_updated")
            .email("new@gmail.com")
            .password("password234")
            .build();

    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

    User actual = userService.replace(stub2, null);

    assertNotNull(actual);
    assertEquals("First_updated", actual.getFirstName());
    assertEquals("Last_updated", actual.getLastName());
    assertEquals("new@gmail.com", actual.getEmail());
    assertEquals("password234", actual.getPassword());
  }

  @Test
  final void save_NoSuchUserYet_ReturnSavedInstance() {
    when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
    when(userRepository.save(any())).thenReturn(stub);

    User actual = userService.save(stub, null);

    assertNotNull(actual);
    assertEquals("First", actual.getFirstName());
  }

  @Test
  final void partialReplace_UserIsAlreadyExists_ReplaceGivenFields() {
    Map<String, Object> partialUpdates = new HashMap<>();
    partialUpdates.put("lastName", "Last_partially_updated");

    when(userRepository.findById(any())).thenReturn(Optional.ofNullable(stub));

    User actual = userService.partialReplace(partialUpdates, null);

    assertNotNull(actual);
    assertEquals("First", actual.getFirstName());
    assertEquals("Last_partially_updated", actual.getLastName());
  }
}
