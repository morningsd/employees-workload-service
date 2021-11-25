package edu.demian.controllers;

import edu.demian.entities.Project;
import edu.demian.services.ProjectService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectController {

  private final ProjectService projectService;

  public ProjectController(ProjectService projectService) {
    this.projectService = projectService;
  }

  @GetMapping
  public List<Project> findAll() {
    return projectService.findAll();
  }

  @PostMapping
  public Project save(@RequestBody Project project) {
    return projectService.save(project);
  }

  @GetMapping
  @RequestMapping("/{name}")
  public Project findProjectByName(@PathVariable String name) {
    // TODO think about throwing 404 status code
    return projectService.findByName(name).orElse(null);
  }



}
