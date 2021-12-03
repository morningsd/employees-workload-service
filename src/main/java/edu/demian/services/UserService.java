package edu.demian.services;

import edu.demian.entities.User;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserService {

  User save(User user);

  User setDepartment(UUID userId, UUID departmentId);

  User findByEmail(String email);

  List<User> findAll();

  List<User> findAllAvailableNow();

  User findById(UUID id);

  User replace(User user, UUID id);

  User partialReplace(Map<String, Object> partialUpdates, UUID id);

  void delete(UUID id);
}
