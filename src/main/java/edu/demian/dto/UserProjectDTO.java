package edu.demian.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserProjectDTO {

  private UserProjectIdDTO id;

  private UserDTO user;

  private ProjectDTO project;

  private int workingHours;

  private LocalDate positionStartDate;

  private LocalDate positionEndDate;

}
