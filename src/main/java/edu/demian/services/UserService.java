package edu.demian.services;

import edu.demian.entities.User;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User save(User user, UUID departmentId);

  List<User> findAll();

  User findById(UUID id);

  User replace(User user, UUID id);

  User partialReplace(User user, UUID id);

  void delete(UUID id);
}
