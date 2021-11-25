package edu.demian.services;

import edu.demian.entities.Department;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {

  Department save(Department department);

  Optional<Department> findByName(String name);

  List<Department> findAll();

  Optional<Department> findById(UUID id);

  Department replace(Department department, UUID id);

  Department partialReplace(Map<String, Object> partialUpdates, UUID id);

  void delete(UUID id);
}
