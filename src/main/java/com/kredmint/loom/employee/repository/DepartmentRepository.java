package com.kredmint.loom.employee.repository;

import com.kredmint.loom.employee.entity.Department;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DepartmentRepository extends MongoRepository<Department, String>{
}
