package edu.demian.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "user_project")
public class UserProject {

  @EmbeddedId
  private UserProjectId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @MapsId("projectId")
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(name = "working_hours", nullable = false)
  private int workingHours;

  @Column(name = "position_start_date", nullable = false)
  private LocalDate positionStartDate;

  @Column(name = "position_end_date")
  private LocalDate positionEndDate;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    UserProject that = (UserProject) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
