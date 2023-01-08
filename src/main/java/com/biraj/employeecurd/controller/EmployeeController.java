package com.biraj.employeecurd.controller;

import com.biraj.employeecurd.model.Employee;
import com.biraj.employeecurd.model.ResourceNotFoundException;
import com.biraj.employeecurd.repository.CustomEmployeeRepository;
import com.biraj.employeecurd.repository.EmployeeRepository;
import com.biraj.employeecurd.repository.impl.CustomEmployeeRepositoryImpl;
import com.biraj.employeecurd.service.SequenceGeneratorService;
import com.mongodb.client.result.UpdateResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final SequenceGeneratorService sequenceGeneratorService;

    private final CustomEmployeeRepository customEmployeeRepository;

    @Autowired
    public EmployeeController(EmployeeRepository employeeRepository, SequenceGeneratorService sequenceGeneratorService, CustomEmployeeRepositoryImpl customEmployeeRepositoryImpl) {
        this.employeeRepository = employeeRepository;
        this.sequenceGeneratorService = sequenceGeneratorService;
        this.customEmployeeRepository = customEmployeeRepositoryImpl;
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));
        return ResponseEntity.ok().body(employee);
    }

    @PostMapping("/employees")
    public Employee createEmployee(@Valid @RequestBody Employee employee) {
        employee.setId(sequenceGeneratorService.generateSequence(Employee.SEQUENCE_NAME));
        return employeeRepository.save(employee);
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable(value = "id") Long employeeId,
                                                   @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setLastName(employeeDetails.getLastName());
        employee.setFirstName(employeeDetails.getFirstName());
        final Employee updatedEmployee = employeeRepository.save(employee);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PatchMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployeeUsingMongoTemplate(@PathVariable(value = "id") Long employeeId,
                                                                     @Valid @RequestBody Employee employeeDetails) throws ResourceNotFoundException {

        UpdateResult updateResult = customEmployeeRepository.updateEmployee(employeeId, employeeDetails);
        if (updateResult.getModifiedCount() > 0) {
            return ResponseEntity.ok(employeeDetails);
        } else {
            throw new ResourceNotFoundException("Employee not found for this id :: " + employeeId);
        }
    }


    @DeleteMapping("/employees/{id}")
    public Map<String, Boolean> deleteEmployee(@PathVariable(value = "id") Long employeeId)
            throws ResourceNotFoundException {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found for this id :: " + employeeId));

        employeeRepository.delete(employee);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}