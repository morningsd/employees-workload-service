package edu.demian.dto;

import java.util.UUID;
import lombok.Data;

@Data
public class UserProjectIdDTO {

  private UUID userId;

  private UUID projectId;

}
