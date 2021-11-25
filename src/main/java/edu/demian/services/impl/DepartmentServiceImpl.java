package edu.demian.services.impl;

import edu.demian.entities.Department;
import edu.demian.exceptions.ServiceException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class DepartmentServiceImpl implements DepartmentService {

  private final DepartmentRepository departmentRepository;

  public DepartmentServiceImpl(DepartmentRepository departmentRepository) {
    this.departmentRepository = departmentRepository;
  }

  @Override
  public Department save(Department department) {
    departmentRepository
        .findByName(department.getName())
        .ifPresent(
            departmentFromDb -> {
              throw new ServiceException(
                  "Department: " + departmentFromDb.getName() + " already exists");
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

  @Override
  public Optional<Department> findById(UUID id) {
    return departmentRepository.findById(id);
  }

  @Override
  public Department replace(Department newDepartment, UUID id) {
    return departmentRepository
        .findById(id)
        .map(
            department -> {
              department.setName(newDepartment.getName());
              department.setDescription(newDepartment.getDescription());
              return departmentRepository.save(department);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException(
                  "Can't update department (no existing department with such id)");
            });
  }

  @Override
  public Department partialReplace(Map<String, Object> partialUpdates, UUID id) {
    return departmentRepository
        .findById(id)
        .map(
            department -> {
              partialUpdates.forEach(
                  (field, value) -> {
                    switch (field) {
                      case "name":
                        department.setName((String) value);
                        break;
                      case "description":
                        department.setDescription((String) value);
                        break;
                      default:
                        throw new ServiceException("Department has no field " + value);
                    }
                  });
              return departmentRepository.save(department);
            })
        .orElseThrow(
            () -> {
              throw new ServiceException(
                  "Can't update department (no existing department with such id)");
            });
  }

  @Override
  public void delete(UUID id) {
    departmentRepository.deleteById(id);
  }
}
