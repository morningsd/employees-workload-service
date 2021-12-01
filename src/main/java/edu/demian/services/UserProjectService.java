package edu.demian.services;

import edu.demian.entities.UserProject;
import java.util.UUID;

public interface UserProjectService {

  UserProject save(UserProject userProject);

  void addProjectToUser(UUID userId, UUID projectId);
}
