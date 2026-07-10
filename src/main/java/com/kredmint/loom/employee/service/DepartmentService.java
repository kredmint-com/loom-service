package com.kredmint.loom.employee.service;

import com.kredmint.loom.employee.entity.Department;
import com.kredmint.loom.employee.repository.DepartmentRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    public Department createDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }


    public Department getDepartmentById(String id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public void deleteDepartment(String id) {
        departmentRepository.deleteById(id);
    }

    public Department updateDepartment(String id, Department department) {
        if (departmentRepository.existsById(id)) {
            department.setId(id);
            return departmentRepository.save(department);
        }
        return null;
    }

}

