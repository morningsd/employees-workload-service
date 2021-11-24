package edu.demian.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.ToString.Exclude;
import org.hibernate.Hibernate;

@Entity
@Table(name = "project")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Project {

  @Id
  @GeneratedValue
  @Column(name = "project_id")
  private UUID id;

  @NotBlank
  @Size(max = 255)
  @Column(name = "name")
  private String name;

  @NotBlank
  @Size(max = 1024)
  @Column(name = "description")
  private String description;

  @OneToMany(mappedBy = "project")
  private List<User> users;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Project project = (Project) o;
    return id != null && Objects.equals(id, project.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
