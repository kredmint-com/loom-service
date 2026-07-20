package com.kredmint.loom.employee.repository;

import com.kredmint.loom.employee.entity.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {


    List<Employee> findByManagerId(String managerId);

    Employee findByEmployeeCode(String employeeCode);

}