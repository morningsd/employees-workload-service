package edu.demian.dto;

import java.util.UUID;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreationDTO {

  @NotNull
  @Size(min = 2, max = 255)
  private String firstName;

  @NotNull
  @Size(min = 2, max = 255)
  private String lastName;

  @Email
  @Size(max = 255)
  @NotBlank
  private String email;

  @NotBlank
  @Size(max = 255)
  private String password;

  private UUID departmentId;
}
