package edu.demian.repositories;

import edu.demian.entities.UserProject;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProjectRepository extends JpaRepository<UserProject, UUID> {



}
