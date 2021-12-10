package edu.demian.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import edu.demian.dto.reports.EmployeesProjectReportDTO;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@NamedNativeQuery(name = "User.getDataForEmployeesWorkloadByDepartmentOnMonthlyBasis",
    query = "select usr.first_name as first, usr.last_name as last, department.name as dname, project.name as pname, user_project.working_hours as occupation, user_project.position_end_date as end_date from user_project left join usr on user_project.user_id = usr.id left join department on usr.department_id = department.id left join project on user_project.project_id = project.id",
    resultSetMapping = "Mapping.EmployeesProjectReportDTO")
@NamedNativeQuery(name = "User.getDataForEmployeesAvailableWithinNext30Days",
    query = "select usr.first_name as first, usr.last_name as last, department.name as dname, project.name as pname, user_project.working_hours as occupation, user_project.position_end_date as end_date from user_project left join usr on user_project.user_id = usr.id left join department on usr.department_id = department.id left join project on user_project.project_id = project.id where user_project.position_end_date > (:fromDate) and user_project.project_id is null",
    resultSetMapping = "Mapping.EmployeesProjectReportDTO")
@SqlResultSetMapping(name = "Mapping.EmployeesProjectReportDTO",
    classes = @ConstructorResult(targetClass = EmployeesProjectReportDTO.class,
        columns = {@ColumnResult(name = "first"),
                   @ColumnResult(name = "last"),
                   @ColumnResult(name = "dname"),
                   @ColumnResult(name = "pname"),
                   @ColumnResult(name = "occupation"),
                   @ColumnResult(name = "end_date")}))
@Entity
@Table(name = "usr")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class User {

  @Id
  @GeneratedValue
  private UUID id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(name = "email", unique = true, nullable = false)
  private String email;

  @Column(name = "password", nullable = false)
  private String password;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
  @JsonManagedReference
  private List<UserProject> projects;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    User user = (User) o;
    return id != null && Objects.equals(id, user.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
