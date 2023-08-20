package com.example.repository;

import com.example.entity.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}