package edu.demian.services;

import edu.demian.entities.Department;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentService {

  Department save(Department department);

  Department findByName(String name);

  List<Department> findAll();

  Department findById(UUID id);

  Department replace(Department department, UUID id);

  Department partialReplace(Department department, UUID id);

  void delete(UUID id);
}
