package edu.demian.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
  private UUID id;

  @Column(name = "name", unique = true, nullable = false)
  private String name;

  @Column(name = "description", length = 1024, nullable = false)
  private String description;

  @OneToMany(mappedBy = "department")
  @JsonIgnore
  @Exclude
  private Set<User> users;

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
