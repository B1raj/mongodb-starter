package com.biraj.employeecurd.repository.impl;

import com.biraj.employeecurd.model.Employee;
import com.biraj.employeecurd.repository.CustomEmployeeRepository;
import com.mongodb.client.result.UpdateResult;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


@Repository
public class CustomEmployeeRepositoryImpl implements CustomEmployeeRepository {

    private final MongoTemplate mongoTemplate;

    public CustomEmployeeRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public UpdateResult updateEmployee(Long id, Employee employeeDetails) {
        Query query = new Query(Criteria.where("id").is(id));
        Update update = new Update();
        update.set("emailId", employeeDetails.getEmailId());
        update.set("firstName", employeeDetails.getFirstName());
        update.set("lastName", employeeDetails.getLastName());

        UpdateResult result = mongoTemplate.updateFirst(query, update, Employee.class);
        System.out.println(result.getModifiedCount() + " document(s) updated..");
        return result;
    }
}
