package com.biraj.employeecurd.repository;

import com.biraj.employeecurd.model.Employee;
import com.mongodb.client.result.UpdateResult;


public interface CustomEmployeeRepository {

    UpdateResult updateEmployee(Long id, Employee employee);
}


