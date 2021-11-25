package edu.demian.controllers;

import edu.demian.entities.Department;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
  public Department findById(@PathVariable UUID id) {
    // TODO read in user controller
    return departmentService.findById(id).orElse(null);
  }

  @GetMapping
  public List<Department> findAll() {
    return departmentService.findAll();
  }

  @PostMapping
  public Department save(@RequestBody Department department) {
    return departmentService.save(department);
  }

  @PutMapping("/{id}")
  public Department replaceDepartment(@RequestBody Department department, @PathVariable UUID id) {
    return departmentService.replace(department, id);
  }

  @PatchMapping("/{id}")
  public Department partialReplaceDepartment(@RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return departmentService.partialReplace(partialUpdates, id);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable UUID id) {
    departmentService.delete(id);
  }

}
