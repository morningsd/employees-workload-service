package edu.demian.services.impl;

import static edu.demian.services.util.ServiceUtils.applyPatches;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import edu.demian.entities.Department;
import edu.demian.entities.User;
import edu.demian.exceptions.InvalidInputDataException;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.repositories.UserRepository;
import edu.demian.services.DepartmentService;
import edu.demian.services.UserService;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UserServiceImpl implements UserService {


  private final static String CSV_EXTENSION = ".csv";

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
  public List<User> findAllAvailableNow() {
    Instant fromDate = Instant.now();
    return userRepository.findUsersAvailableWithinNextCoupleOfDays(fromDate);
  }

  @Override
  public List<User> findAllAvailableWithinCoupleOfDays(int days) {
    if (days < 0) {
      days = 0;
    }
    Instant fromDate = Instant.now().plus(days, ChronoUnit.DAYS);
    return userRepository.findUsersAvailableWithinNextCoupleOfDays(fromDate);
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

  @Override
  public void uploadFromCsv(MultipartFile multipartFile) {
    String tempFileName = UUID.randomUUID().toString();
    FileReader reader;
    File fileTmp = new File(tempFileName + CSV_EXTENSION);
    try {
      multipartFile.transferTo(fileTmp);
      reader = new FileReader(fileTmp);
    } catch (IOException e) {
      throw new RuntimeException("Couldn't parse given file");
    }

    ColumnPositionMappingStrategy<User> strategy = new ColumnPositionMappingStrategyBuilder<User>().build();
    strategy.setType(User.class);
    String[] columns = new String[] {"firstName", "lastName", "email", "password"};
    strategy.setColumnMapping(columns);

    CsvToBean<User> csv = new CsvToBeanBuilder<User>(reader).withType(User.class).withMappingStrategy(strategy).withSkipLines(1).build();
    List<User> list = csv.parse();

    userRepository.saveAll(list);
    fileTmp.delete();
  }
}
