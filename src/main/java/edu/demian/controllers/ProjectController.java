package edu.demian.controllers;

import edu.demian.entities.Project;
import edu.demian.services.ProjectService;
import edu.demian.services.UserProjectService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;
  private final UserProjectService userProjectService;


  public ProjectController(ProjectService projectService,
      UserProjectService userProjectService) {
    this.projectService = projectService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public Project findById(@PathVariable UUID id) {
    // TODO read user controller
    return projectService.findById(id).orElse(null);
  }

  @GetMapping
  public List<Project> findAll() {
    return projectService.findAll();
  }

  @PostMapping
  public Project save(@RequestBody Project project) {
    return projectService.save(project);
  }

  @GetMapping("/{name}")
  public Project findByName(@PathVariable String name) {
    // TODO think about throwing 404 status code
    return projectService.findByName(name).orElse(null);
  }

  @PostMapping("/add-user")
  public void addUser(@RequestBody UUID userId, @RequestBody UUID projectId) {
    userProjectService.addProjectToUser(userId, projectId);
  }

  @PutMapping("/{id}")
  public Project replaceProject(@RequestBody Project project, @PathVariable UUID id) {
    return projectService.replace(project, id);
  }

  @PatchMapping("/{id}")
  public Project partialReplaceProject(@RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return projectService.partialReplace(partialUpdates, id);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    projectService.delete(id);
  }

}
