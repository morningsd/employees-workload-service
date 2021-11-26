package edu.demian.controllers;

import edu.demian.entities.Department;
import edu.demian.exceptions.ServiceException;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/departments")
public class DepartmentController {

  private final DepartmentService departmentService;

  public DepartmentController(DepartmentService departmentService) {
    this.departmentService = departmentService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<Department> findById(@PathVariable UUID id) {
    Department department =
        departmentService
            .findById(id)
            .orElseThrow(() -> new ServiceException("No department with given id was found"));
    return new ResponseEntity<>(department, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<Department>> findAll() {
    return new ResponseEntity<>(departmentService.findAll(), HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<Department> save(@RequestBody Department department) {
    return new ResponseEntity<>(departmentService.save(department), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Department> replaceDepartment(
      @RequestBody Department department, @PathVariable UUID id) {
    return new ResponseEntity<>(departmentService.replace(department, id), HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<Department> partialReplaceDepartment(
      @RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return new ResponseEntity<>(
        departmentService.partialReplace(partialUpdates, id), HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable UUID id) {
    departmentService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
