package edu.demian.dto;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class ProjectDTO {

  @NotNull
  private UUID id;

  @NotNull
  @Size(min = 2, max = 255)
  private String name;

  @Size(max = 1024)
  private String description;

}
