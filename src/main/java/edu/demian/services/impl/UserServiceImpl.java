package edu.demian.services.impl;

import edu.demian.entities.User;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.UserRepository;
import edu.demian.services.DepartmentService;
import edu.demian.services.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final DepartmentService departmentService;

  public UserServiceImpl(UserRepository userRepository, DepartmentService departmentService) {
    this.userRepository = userRepository;
    this.departmentService = departmentService;
  }

  @Override
  public User save(User user, UUID departmentId) {
    userRepository
        .findByEmail(user.getEmail())
        .ifPresent(
            userFromDb -> {
              throw new ServiceException(
                  "User with this email: " + userFromDb.getEmail() + " already exists");
            });
    departmentService
        .findById(departmentId)
        .ifPresentOrElse(
            user::setDepartment,
            () -> {
              throw new ServiceException("No such department found");
            });
    return userRepository.save(user);
  }

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public Optional<User> findById(UUID id) {
    return userRepository.findById(id);
  }

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
              return userRepository.save(user);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException("Can't update user (no existing user with such id)");
            });
  }

  @Override
  public User partialReplace(Map<String, Object> partialUpdates, UUID id) {
    return userRepository
        .findById(id)
        .map(
            user -> {
              partialUpdates.forEach(
                  (field, value) -> {
                    switch (field) {
                      case "firstName":
                        user.setFirstName((String) value);
                        break;
                      case "lastName":
                        user.setLastName((String) value);
                        break;
                      case "email":
                        user.setEmail((String) value);
                        break;
                      case "password":
                        user.setPassword((String) value);
                        break;
                      default:
                        throw new ServiceException("User has no field " + value);
                    }
                  });
              return userRepository.save(user);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException("Can't update user (no existing user with such id)");
            });
  }

  @Override
  public void delete(UUID id) {
    userRepository.deleteById(id);
  }
}
