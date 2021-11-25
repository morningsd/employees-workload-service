package edu.demian.services.impl;

import edu.demian.entities.User;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.UserRepository;
import edu.demian.services.UserService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User save(User user) {
    userRepository.findByEmail(user.getEmail()).ifPresent(userFromDb -> {
      throw new ServiceException("User with this email: " + userFromDb.getEmail() + " already exists");
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
}
