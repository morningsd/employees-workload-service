package edu.demian.controllers;

import edu.demian.entities.User;
import edu.demian.exceptions.ServiceException;
import edu.demian.services.UserProjectService;
import edu.demian.services.UserService;
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
@RequestMapping("/users")
public class UserController {

  private final UserService userService;
  private final UserProjectService userProjectService;

  public UserController(UserService userService, UserProjectService userProjectService) {
    this.userService = userService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<User> findById(@PathVariable UUID id) {
    User user =
        userService
            .findById(id)
            .orElseThrow(() -> new ServiceException("No user with given id was found"));
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<User>> findAll() {
    return new ResponseEntity<>(userService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<User> register(@RequestBody User user, @RequestBody UUID departmentId) {
    return new ResponseEntity<>(userService.save(user, departmentId), HttpStatus.OK);
  }

  @PostMapping("/add-project")
  public ResponseEntity<?> addProject(@RequestBody UUID userId, @RequestBody UUID projectId) {
    userProjectService.addProjectToUser(userId, projectId);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<User> replaceUser(@RequestBody User user, @PathVariable UUID id) {
    return new ResponseEntity<>(userService.replace(user, id), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<User> partialReplaceUser(
      @RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return new ResponseEntity<>(userService.partialReplace(partialUpdates, id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    userService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
