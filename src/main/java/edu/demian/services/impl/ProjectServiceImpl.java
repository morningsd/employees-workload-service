package edu.demian.services.impl;

import edu.demian.entities.Project;
import edu.demian.entities.User;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.repositories.ProjectRepository;
import edu.demian.services.ProjectService;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProjectServiceImpl implements ProjectService {

  private final ProjectRepository projectRepository;

  public ProjectServiceImpl(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Project save(Project project) {
    projectRepository
        .findByName(project.getName())
        .ifPresent(
            projectFromDb -> {
              throw new ResourceAlreadyExistsException(
                  "Project: " + projectFromDb.getName() + " already exists");
            });
    return projectRepository.save(project);
  }

  @Override
  public Project findByName(String name) {
    return projectRepository
        .findByName(name)
        .orElseThrow(
            () -> new ResourceNotFoundException("No project with name: " + name + " was found"));
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
  public Project findById(UUID id) {
    return projectRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No user with id: " + " was found"));
  }

  @Transactional
  @Override
  public Project replace(Project newProject, UUID id) {
    return projectRepository
        .findById(id)
        .map(
            project -> {
              project.setName(newProject.getName());
              project.setDescription(newProject.getDescription());
              return project;
            })
        .orElseThrow(
            () -> {
              throw new ResourceNotFoundException("No project with id: " + id + " was found");
            });
  }

  @Transactional
  @Override
  public Project partialReplace(Project newProject, UUID id) {
    Project projectToUpdate = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No project with id: " + id + " was found"));
    if (newProject.getName() != null && !newProject.getName().isEmpty()) {
      projectToUpdate.setName(newProject.getName());
    }
    if (newProject.getDescription() != null && !newProject.getDescription().isEmpty()) {
      projectToUpdate.setDescription(newProject.getDescription());
    }
    return projectToUpdate;
  }

  @Override
  public void delete(UUID id) {
    projectRepository.deleteById(id);
  }
}
