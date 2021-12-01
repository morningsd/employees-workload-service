package edu.demian.services.impl;

import static edu.demian.services.util.ServiceUtils.applyPatches;

import edu.demian.entities.Department;
import edu.demian.exceptions.ResourceAlreadyExistsException;
import edu.demian.exceptions.ResourceNotFoundException;
import edu.demian.repositories.DepartmentRepository;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    return applyPatches(departmentToPatch, partialUpdates, Department.class);
  }

  @Override
  public void delete(UUID id) {
    departmentRepository.deleteById(id);
  }
}
