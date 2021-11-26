package edu.demian.controllers;

import edu.demian.entities.Project;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.services.ProjectService;
import edu.demian.services.UserProjectService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  public ProjectController(ProjectService projectService, UserProjectService userProjectService) {
    this.projectService = projectService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Project> findById(@PathVariable UUID id) {
    return new ResponseEntity<>(projectService.findById(id), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Project>> findAll() {
    return new ResponseEntity<>(projectService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Project> save(@RequestBody Project project) {
    return new ResponseEntity<>(projectService.save(project), HttpStatus.OK);
  }

  @GetMapping("/{name}")
  public ResponseEntity<Project> findByName(@PathVariable String name) {
    return new ResponseEntity<>(projectService.findByName(name), HttpStatus.OK);
  }

  @PostMapping("/add-user")
  public ResponseEntity<?> addUser(@RequestBody UUID userId, @RequestBody UUID projectId) {
    userProjectService.addProjectToUser(userId, projectId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Project> replaceProject(
      @RequestBody Project project, @PathVariable UUID id) {
    return new ResponseEntity<>(projectService.replace(project, id), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Project> partialReplaceProject(
      @RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return new ResponseEntity<>(projectService.partialReplace(partialUpdates, id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    projectService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
