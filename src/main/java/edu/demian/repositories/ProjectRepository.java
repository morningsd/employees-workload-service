package edu.demian.repositories;

import edu.demian.entities.Project;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

  Optional<Project> findByName(String name);

  @Query(
      value =
          "SELECT * FROM project p JOIN user_project up ON p.id = up.project_id WHERE up.user_id = ?1",
      nativeQuery = true)
  List<Project> findProjectsByUserProjectsUserId(UUID userId);
}
