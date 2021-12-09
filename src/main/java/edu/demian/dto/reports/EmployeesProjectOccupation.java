package edu.demian.dto.reports;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EmployeesProjectOccupation {

  private String firstName;

  private String lastName;

  private String departmentName;

  private String projectName;

  private int occupation;

}
