package edu.demian.services.impl;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.ProjectRepository;
import edu.demian.services.ProjectService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
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

  @Override
  public Optional<Project> findById(UUID id) {
    return projectRepository.findById(id);
  }

  @Override
  public Project replace(Project newProject, UUID id) {
    return projectRepository
        .findById(id)
        .map(
            project -> {
              project.setName(newProject.getName());
              project.setDescription(newProject.getDescription());
              return projectRepository.save(project);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException("Can't update project (no existing project with such id)");
            });
  }

  @Override
  public Project partialReplace(Map<String, Object> partialUpdates, UUID id) {
    return projectRepository
        .findById(id)
        .map(
            project -> {
              partialUpdates.forEach(
                  (field, value) -> {
                    switch (field) {
                      case "name":
                        project.setName((String) value);
                        break;
                      case "description":
                        project.setDescription((String) value);
                        break;
                      default:
                        throw new ServiceException("Project has no field " + value);
                    }
                  });
              return projectRepository.save(project);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException("Can't update project (no existing project with such id)");
            });
  }

  @Override
  public void delete(UUID id) {
    projectRepository.deleteById(id);
  }
}
