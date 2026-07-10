package com.kredmint.loom.employee.service;


import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.repository.EmployeeRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //private List<Employee> employees = new ArrayList<>();

    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getById(String id) {
        return employeeRepository.findById(id).orElse(null);
    }

    public Employee updateEmployee(String id, Employee employee) {
        if (employeeRepository.existsById(id)) {
            employee.setId(id);
            return employeeRepository.save(employee);
        }
        return null;
    }

    public void deleteEmployee(String id) {
        employeeRepository.deleteById(id);
    }

    public List<Employee> getAllJuniors(String managerId) {
        return dfs(managerId);
    }

    private List<Employee> dfs(String managerId) {
        List<Employee> result = new ArrayList<>();
        for (Employee employee : employeeRepository.findByManagerId(managerId)) {
            result.add(employee);
            result.addAll(dfs(employee.getId()));
        }
        return result;
    }
}
