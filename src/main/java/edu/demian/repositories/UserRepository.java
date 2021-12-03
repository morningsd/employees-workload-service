package edu.demian.repositories;

import edu.demian.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  @Query(
      value =
          "SELECT * FROM usr u JOIN user_project up ON u.id = up.user_id WHERE up.project_id = ?1",
      nativeQuery = true)
  List<User> findUsersByUserProjectsProjectId(UUID projectId);

  @Query(
      value =
          "select u.id, u.first_name, u.last_name, u.email, u.password, u.department_id from usr u where not exists(select null from user_project up where up.user_id = u.id and up.position_end_date > now())",
      nativeQuery = true)
  List<User> findUsersAvailableNow();
}
