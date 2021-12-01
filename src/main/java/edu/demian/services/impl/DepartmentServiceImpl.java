package edu.demian.services.impl;

import edu.demian.entities.Department;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceHasNoSuchPropertyException;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.DepartmentService;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

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
              throw new ResourceAlreadyExistsException(
                  "Department: " + departmentFromDb.getName() + " already exists");
            });
    return departmentRepository.save(department);
  }

  @Override
  public Department findByName(String name) {
    return departmentRepository
        .findByName(name)
        .orElseThrow(
            () -> new ResourceNotFoundException("No department with name: " + name + " was found"));
  }

  @Override
  public List<Department> findAll() {
    return departmentRepository.findAll();
  }

  @Override
  public Department findById(UUID id) {
    return departmentRepository
        .findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException("No department with id: " + id + " was found"));
  }

  @Transactional
  @Override
  public Department replace(Department newDepartment, UUID id) {
    return departmentRepository
        .findById(id)
        .map(
            department -> {
              department.setName(newDepartment.getName());
              department.setDescription(newDepartment.getDescription());
              return department;
            })
        .orElseThrow(
            () -> {
              throw new ResourceNotFoundException("No department with id: " + id + " was found");
            });
  }

  @Transactional
  @Override
  public Department partialReplace(Map<String, Object> partialUpdates, UUID id) {
    Department departmentToPatch =
        departmentRepository
            .findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException("No department with id: " + id + " was found"));

    partialUpdates.remove("id");
    partialUpdates.forEach(
        (k, v) -> {
          Field field = ReflectionUtils.findField(Department.class, k);
          if (field == null) {
            throw new ResourceHasNoSuchPropertyException("Department has no field: " + k);
          }
          field.setAccessible(true);
          ReflectionUtils.setField(field, departmentToPatch, v);
        });
    return departmentToPatch;
  }

  @Override
  public void delete(UUID id) {
    departmentRepository.deleteById(id);
  }
}
