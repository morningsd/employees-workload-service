package edu.demian.services;

import edu.demian.entities.Department;
import java.util.List;
import java.util.Optional;

public interface DepartmentService {

  Department save(Department department);

  Optional<Department> findByName(String name);

  List<Department> findAll();

}
