package edu.demian.dto.auth;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthenticationRequestDTO {

  @NotBlank
  @Size(max = 255)
  private String email;

  @NotBlank
  private String password;

}
