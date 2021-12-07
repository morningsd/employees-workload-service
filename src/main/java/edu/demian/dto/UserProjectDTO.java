package edu.demian.dto;

import edu.demian.entities.UserProjectId;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserProjectDTO {

  private UserProjectId userProjectId;

  private int workingHours;

  private LocalDate positionStartDate;

  private LocalDate positionEndDate;

}
