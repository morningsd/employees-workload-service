package edu.demian.services;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import java.util.List;
import java.util.Optional;

public interface ProjectService {

  Project save(Project project);

  Optional<Project> findByName(String name);

  List<Project> findUsersProjects(User user);

  List<Project> findAll();
}
