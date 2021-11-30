package edu.demian.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "user_project")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UserProject {

  @EmbeddedId private UserProjectId id;

  @ManyToOne
  @MapsId("userId")
  @JoinColumn(name = "user_id")
  @JsonIgnore
  @Exclude
  private User user;

  @ManyToOne
  @MapsId("projectId")
  @JoinColumn(name = "project_id")
//  @JsonIgnore
  @Exclude
  private Project project;

  @Column(name = "working_hours", nullable = false)
  private int workingHours;

  @Column(name = "position_start_date", nullable = false)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd ")
  private LocalDate positionStartDate;

  @Column(name = "position_end_date", nullable = false)
  @JsonDeserialize(using = LocalDateDeserializer.class)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd ")
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
