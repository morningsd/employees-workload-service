package edu.demian.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DepartmentDTO {

  @NotNull
  private UUID id;

  @NotNull
  @Size(min = 2, max = 255)
  private String name;

  @Size(max = 1024)
  private String description;

  @JsonIgnore
  private Set<UserDTO> users;

}
