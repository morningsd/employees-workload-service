package edu.demian.services.impl;

import edu.demian.entities.Department;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
    this.departmentRepository = departmentRepository;
  }

  @Override
  public Department save(Department department) {
    departmentRepository.findByName(department.getName()).ifPresent(departmentFromDb -> {
      throw new ServiceException("Department: " + departmentFromDb.getName() + " already exists");
    });
    return departmentRepository.save(department);
  }

  @Override
  public Optional<Department> findByName(String name) {
    return departmentRepository.findByName(name);
  }

  @Override
  public List<Department> findAll() {
    return departmentRepository.findAll();
  }
}
