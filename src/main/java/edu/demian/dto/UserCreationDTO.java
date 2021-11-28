package edu.demian.dto;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class UserCreationDTO {

  private UUID id;

  private String firstName;

  private String lastName;

  private String email;

  private String password;

  private DepartmentDTO department;

  private Set<ProjectDTO> projects;

}
