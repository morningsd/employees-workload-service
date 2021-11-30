package edu.demian.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Embeddable
public class UserProjectId implements Serializable {

  @Column(name = "user_id")
  private UUID userId;

  @Column(name = "project_id")
  private UUID projectId;

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UserProjectId that = (UserProjectId) o;
    return Objects.equals(userId, that.userId) && Objects.equals(projectId,
        that.projectId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userId, projectId);
  }

}
