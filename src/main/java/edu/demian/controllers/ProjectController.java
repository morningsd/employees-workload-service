package edu.demian.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.dto.ProjectDTO;
import edu.demian.entities.Project;
import edu.demian.services.ProjectService;
import edu.demian.services.UserProjectService;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
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

  private final ObjectMapper mapper;
  private final ProjectService projectService;
  private final UserProjectService userProjectService;

  public ProjectController(
      ObjectMapper mapper, ProjectService projectService, UserProjectService userProjectService) {
    this.mapper = mapper;
    this.projectService = projectService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProjectDTO> findById(@PathVariable UUID id) {
    return new ResponseEntity<>(
        mapper.convertValue(projectService.findById(id), new TypeReference<ProjectDTO>() {}),
        HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<ProjectDTO>> findAll() {
    return new ResponseEntity<>(
        mapper.convertValue(projectService.findAll(), new TypeReference<List<ProjectDTO>>() {}),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<ProjectDTO> save(@Valid @RequestBody ProjectDTO projectDTO) {
    Project project = mapper.convertValue(projectDTO, new TypeReference<>() {});
    return new ResponseEntity<>(
        mapper.convertValue(projectService.save(project), new TypeReference<ProjectDTO>() {}),
        HttpStatus.OK);
  }

  @GetMapping("/name={name}")
  public ResponseEntity<ProjectDTO> findByName(@PathVariable String name) {
    return new ResponseEntity<>(
        mapper.convertValue(projectService.findByName(name), new TypeReference<ProjectDTO>() {}),
        HttpStatus.OK);
  }

  @PostMapping("/add-user")
  public ResponseEntity<Void> addUser(@RequestBody UUID userId, @RequestBody UUID projectId) {
    userProjectService.addProjectToUser(userId, projectId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ProjectDTO> replaceProject(
      @Valid @RequestBody ProjectDTO projectDTO, @PathVariable UUID id) {
    Project project = mapper.convertValue(projectDTO, new TypeReference<>() {});
    return new ResponseEntity<>(
        mapper.convertValue(
            projectService.replace(project, id), new TypeReference<ProjectDTO>() {}),
        HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<ProjectDTO> partialReplaceProject(
      @Valid @RequestBody ProjectDTO projectDTO, @PathVariable UUID id) {
    Project project = mapper.convertValue(projectDTO, new TypeReference<>() {});
    return new ResponseEntity<>(
        mapper.convertValue(
            projectService.partialReplace(project, id), new TypeReference<ProjectDTO>() {}),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    projectService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
