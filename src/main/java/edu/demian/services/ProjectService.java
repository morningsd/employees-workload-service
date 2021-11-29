package edu.demian.services;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ProjectService {

  Project save(Project project);

  Project findByName(String name);

  List<Project> findUsersProjects(User user);

  List<Project> findAll();

  Project findById(UUID id);

  Project replace(Project project, UUID id);

  Project partialReplace(Project project, UUID id);

  void delete(UUID id);
}
