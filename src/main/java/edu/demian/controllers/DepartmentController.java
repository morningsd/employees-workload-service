package edu.demian.controllers;

import edu.demian.entities.Department;
import edu.demian.services.DepartmentService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping
  public Department save(@RequestBody Department department) {
    return departmentService.save(department);
  }

  @GetMapping
  public List<Department> findAll() {
    return departmentService.findAll();
  }

}
