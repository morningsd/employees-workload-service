package edu.demian.services.impl;

import static edu.demian.services.util.ServiceUtils.applyPatches;

import edu.demian.entities.Department;
import edu.demian.entities.User;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.repositories.UserRepository;
import edu.demian.services.DepartmentService;
import edu.demian.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final DepartmentService departmentService;

  public UserServiceImpl(UserRepository userRepository, DepartmentService departmentService) {
    this.userRepository = userRepository;
    this.departmentService = departmentService;
  }

  @Override
  public User save(User user) {
    userRepository
        .findByEmail(user.getEmail())
        .ifPresent(
            userFromDb -> {
              throw new ResourceAlreadyExistsException(
                  "User with this email: " + userFromDb.getEmail() + " already exists");
            });

    return userRepository.save(user);
  }

  @Transactional
  @Override
  public User setDepartment(UUID userId, UUID departmentId) {
    User user = findById(userId);
    Department department = departmentService.findById(departmentId);
    user.setDepartment(department);
    return user;
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findById(UUID id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No user with id: " + id + " was found"));
  }

  @Transactional
  @Override
  public User replace(User newUser, UUID id) {
    return userRepository
        .findById(id)
        .map(
            user -> {
              user.setFirstName(newUser.getFirstName());
              user.setLastName(newUser.getLastName());
              user.setEmail(newUser.getEmail());
              user.setPassword(newUser.getPassword());
              return user;
            })
        .orElseThrow(
            () -> {
              throw new ResourceNotFoundException("No user with id: " + id + " was found");
            });
  }

  @Transactional
  @Override
  public User partialReplace(Map<String, Object> partialUpdates, UUID id) {
    User userToPatch =
        userRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("No user with id: " + id + " was found"));

    return applyPatches(userToPatch, partialUpdates, User.class);
  }

  @Override
  public User findByEmail(String email) {
    return userRepository
        .findByEmail(email)
        .orElseThrow(() -> new ResourceNotFoundException("No user with given email was found"));
  }

  @Override
  public void delete(UUID id) {
    userRepository.deleteById(id);
  }
}
