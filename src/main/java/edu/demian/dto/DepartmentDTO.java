package edu.demian.dto;

import java.util.Set;
import java.util.UUID;
import lombok.Data;

@Data
public class DepartmentDTO {

  private UUID id;

  private String name;

  private String description;

  private Set<UserDTO> users;

}
