package edu.demian.services.impl;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import edu.demian.entities.UserProject;
import edu.demian.entities.UserProjectId;
import edu.demian.repositories.UserProjectRepository;
import edu.demian.services.ProjectService;
import edu.demian.services.UserProjectService;
import edu.demian.services.UserService;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserProjectServiceImpl implements UserProjectService {

  private final UserProjectRepository userProjectRepository;
  private final UserService userService;
  private final ProjectService projectService;

  public UserProjectServiceImpl(
      UserProjectRepository userProjectRepository,
      UserService userService,
      ProjectService projectService) {
    this.userProjectRepository = userProjectRepository;
    this.userService = userService;
    this.projectService = projectService;
  }

  @Override
  public UserProject save(UserProject userProject) {
    return userProjectRepository.save(userProject);
  }

  @Override
  @Transactional
  public void addProjectToUser(UserProject userProject) {
    UUID userId = userProject.getId().getUserId();
    UUID projectId = userProject.getId().getProjectId();
    User user = userService.findById(userId);
    Project project = projectService.findById(projectId);

    userProject.setUser(user);
    userProject.setProject(project);

    userProjectRepository.save(userProject);
  }
}
