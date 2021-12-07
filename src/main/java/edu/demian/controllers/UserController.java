package edu.demian.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.dto.UserCreationDTO;
import edu.demian.dto.UserDTO;
import edu.demian.dto.UserProjectDTO;
import edu.demian.entities.User;
import edu.demian.entities.UserProject;
import edu.demian.entities.UserProjectId;
import edu.demian.services.UserProjectService;
import edu.demian.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
public class UserController {

  private final BCryptPasswordEncoder passwordEncoder;
  private final ObjectMapper mapper;
  private final UserService userService;
  private final UserProjectService userProjectService;

  public UserController(
      BCryptPasswordEncoder passwordEncoder,
      ObjectMapper mapper, UserService userService, UserProjectService userProjectService) {
    this.passwordEncoder = passwordEncoder;
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

  @GetMapping("/available")
  public ResponseEntity<List<UserDTO>> findAllAvailableNow() {
    List<User> available = userService.findAllAvailableNow();
    return new ResponseEntity<>(
        mapper.convertValue(available, new TypeReference<List<UserDTO>>() {}),
        HttpStatus.OK
    );
  }

  @GetMapping("/available/{days}")
  public ResponseEntity<List<UserDTO>> findAllAvailableWithinCoupleOfDays(@PathVariable int days) {
    List<User> available = userService.findAllAvailableWithinCoupleOfDays(days);
    return new ResponseEntity<>(
        mapper.convertValue(available, new TypeReference<List<UserDTO>>() {}),
        HttpStatus.OK
    );
  }

  @PostMapping
  public ResponseEntity<UserDTO> save(@Valid @RequestBody UserCreationDTO userCreationDTO) {
    User user = mapper.convertValue(userCreationDTO, User.class);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userService.save(user);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/{id}")
  public ResponseEntity<UserDTO> setDepartment(@PathVariable UUID id, @RequestBody UUID departmentId) {
    return new ResponseEntity<>(
        mapper.convertValue(userService.setDepartment(id, departmentId), UserDTO.class),
        HttpStatus.OK
    );
  }

  @PostMapping("/add-project")
  public ResponseEntity<Void> addProject(@Valid @RequestBody UserProjectDTO userProjectDTO) {
    UserProject userProject = mapper.convertValue(userProjectDTO, UserProject.class);
    userProject.setId(mapper.convertValue(userProjectDTO.getUserProjectId(), UserProjectId.class));
    userProjectService.addProjectToUser(userProject);
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

  @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
  public ResponseEntity<Void> uploadFromCsv(@RequestParam("file") MultipartFile file) {
    userService.uploadFromCsv(file);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
