package edu.demian.services.impl;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.ProjectRepository;
import edu.demian.services.ProjectService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectServiceImpl(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Project save(Project project) {
    projectRepository.findByName(project.getName()).ifPresent(projectFromDb -> {
      throw new ServiceException("Project: " + projectFromDb.getName() + " already exists");
    });
    return projectRepository.save(project);
  }

  @Override
  public Optional<Project> findByName(String name) {
    return projectRepository.findByName(name);
  }

  @Override
  public List<Project> findUsersProjects(User user) {
    return projectRepository.findProjectsByUserProjectsUserId(user.getId());
  }

  @Override
  public List<Project> findAll() {
    return projectRepository.findAll();
  }

}
