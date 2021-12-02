package edu.demian.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.dto.UserCreationDTO;
import edu.demian.dto.UserDTO;
import edu.demian.dto.UserProjectIdDTO;
import edu.demian.entities.User;
import edu.demian.services.UserProjectService;
import edu.demian.services.UserService;
import java.util.List;
import java.util.Map;
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
@RequestMapping("/users")
public class UserController {

  private final ObjectMapper mapper;
  private final UserService userService;
  private final UserProjectService userProjectService;

  public UserController(
      ObjectMapper mapper, UserService userService, UserProjectService userProjectService) {
    this.mapper = mapper;
    this.userService = userService;
    this.userProjectService = userProjectService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> findById(@PathVariable UUID id) {
    return new ResponseEntity<>(
        mapper.convertValue(userService.findById(id), UserDTO.class), HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<UserDTO>> findAll() {
    List<User> all = userService.findAll();
    return new ResponseEntity<>(
        mapper.convertValue(all, new TypeReference<List<UserDTO>>() {}), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<UserDTO> register(
      @Valid @RequestBody UserCreationDTO userCreationDTO) {
    User user = mapper.convertValue(userCreationDTO, User.class);
    userService.save(user, userCreationDTO.getDepartmentId());
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/add-project")
  public ResponseEntity<Void> addProject(@Valid @RequestBody UserProjectIdDTO userProjectIdDTO) {
    userProjectService.addProjectToUser(
        userProjectIdDTO.getUserId(), userProjectIdDTO.getProjectId());
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> replaceUser(
      @Valid @RequestBody UserCreationDTO userCreationDTO, @PathVariable UUID id) {
    User user = mapper.convertValue(userCreationDTO, User.class);
    return new ResponseEntity<>(
        mapper.convertValue(userService.replace(user, id), UserDTO.class), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<UserDTO> partialReplaceUser(
      @RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return new ResponseEntity<>(
        mapper.convertValue(userService.partialReplace(partialUpdates, id), UserDTO.class),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    userService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
