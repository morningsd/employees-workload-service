package edu.demian.controllers;

import edu.demian.entities.User;
import edu.demian.services.UserProjectService;
import edu.demian.services.UserService;
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
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserProjectService userProjectService;

  public UserController(UserService userService,
      UserProjectService userProjectService) {
    this.userService = userService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public User findById(@PathVariable UUID id) {
    // TODO think about return type if not found
    return userService.findById(id).orElse(null);
  }

  @GetMapping
  public List<User> findAll() {
    return userService.findAll();
  }

  @PostMapping
  public User register(@RequestBody User user, @RequestBody UUID departmentId) {
    return userService.save(user, departmentId);
  }

  @PostMapping("/add-project")
  public void addProject(@RequestBody UUID userId, @RequestBody UUID projectId) {
    userProjectService.addProjectToUser(userId, projectId);
  }

  @PutMapping("/{id}")
  public User replaceUser(@RequestBody User user, @PathVariable UUID id) {
    return userService.replace(user, id);
  }

  @PatchMapping("/{id}")
  public User partialReplaceUser(@RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return userService.partialReplace(partialUpdates, id);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    userService.delete(id);
  }

}
