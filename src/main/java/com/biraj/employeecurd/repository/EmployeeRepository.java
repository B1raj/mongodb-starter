package com.biraj.employeecurd.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.biraj.employeecurd.model.*;


@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long> {

}