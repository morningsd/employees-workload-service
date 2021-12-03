package edu.demian.dto;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import edu.demian.entities.UserProjectId;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Data;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute.Use;

@Data
public class UserProjectDTO {

  private UserProjectId userProjectId;

//  private User user;

//  private Project project;

  private int workingHours;

  private LocalDate positionStartDate;

  private LocalDate positionEndDate;

}
