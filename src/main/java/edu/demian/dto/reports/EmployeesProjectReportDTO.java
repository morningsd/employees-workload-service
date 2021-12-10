package edu.demian.dto.reports;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import lombok.Getter;

@Getter
public class EmployeesProjectReportDTO {

  private String firstName;

  private String lastName;

  private String departmentName;

  private String projectName;

  private int occupation;

  private Date positionEndDate;


  public EmployeesProjectReportDTO(String firstName, String lastName, String departmentName,
      String projectName, int occupation, Date positionEndDate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.departmentName = departmentName;
    this.projectName = projectName;
    this.occupation = occupation;
    this.positionEndDate = positionEndDate;
  }


  public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
    return dateToConvert.toInstant()
        .atZone(ZoneId.systemDefault())
        .toLocalDate();
  }
}
