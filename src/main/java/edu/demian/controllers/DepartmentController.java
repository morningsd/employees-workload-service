package edu.demian.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.demian.dto.DepartmentDTO;
import edu.demian.entities.Department;
import edu.demian.services.DepartmentService;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
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

  private final ObjectMapper mapper;
  private final DepartmentService departmentService;

  public DepartmentController(ObjectMapper mapper, DepartmentService departmentService) {
    this.mapper = mapper;
    this.departmentService = departmentService;
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDTO> findById(@PathVariable UUID id) {
    return new ResponseEntity<>(
        mapper.convertValue(departmentService.findById(id), DepartmentDTO.class),
        HttpStatus.OK);
  }

  @GetMapping("/name={name}")
  public ResponseEntity<DepartmentDTO> findByName(@PathVariable String name) {
    return new ResponseEntity<>(
        mapper.convertValue(
            departmentService.findByName(name), DepartmentDTO.class),
        HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<List<DepartmentDTO>> findAll() {
    return new ResponseEntity<>(
        mapper.convertValue(
            departmentService.findAll(), new TypeReference<List<DepartmentDTO>>() {}),
        HttpStatus.OK);
  }

  @PostMapping
  public ResponseEntity<DepartmentDTO> save(@Valid @RequestBody DepartmentDTO departmentDTO) {
    Department department = mapper.convertValue(departmentDTO, Department.class);
    return new ResponseEntity<>(
        mapper.convertValue(departmentService.save(department), DepartmentDTO.class), HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<DepartmentDTO> replaceDepartment(
      @RequestBody DepartmentDTO departmentDTO, @PathVariable UUID id) {
    Department department = mapper.convertValue(departmentDTO, Department.class);
    return new ResponseEntity<>(
        mapper.convertValue(
            departmentService.replace(department, id), DepartmentDTO.class),
        HttpStatus.OK);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDTO> partialReplaceDepartment(
      @RequestBody Map<String, Object> partialUpdates, @PathVariable UUID id) {
    return new ResponseEntity<>(
        mapper.convertValue(
            departmentService.partialReplace(partialUpdates, id),
            DepartmentDTO.class),
        HttpStatus.OK);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    departmentService.delete(id);
    return new ResponseEntity<>(HttpStatus.OK);
  }
}
