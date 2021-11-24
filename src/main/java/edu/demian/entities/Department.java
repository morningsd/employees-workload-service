package edu.demian.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
@Table(name = "department")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Department {

  @Id
  @GeneratedValue
  @Column(name = "department_id")
  private UUID id;

  @Column(name = "name", unique = true)
  @Size(max = 255)
  @NotBlank
  private String name;

  @Column(name = "description")
  @Size(max = 1024)
  @NotBlank
  private String description;

  @OneToMany(mappedBy = "department")
  @Exclude
  private List<User> users;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
      return false;
    }
    Department that = (Department) o;
    return id != null && Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return getClass().hashCode();
  }
}
