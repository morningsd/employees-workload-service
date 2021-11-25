package edu.demian.services;

import edu.demian.entities.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

  User save(User user);

  List<User> findAll();

  Optional<User> findById(UUID id);

}
