package com.kredmint.loom.employee.service;

import com.kredmint.loom.employee.entity.Department;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepartmentService {
    //@Autowired
    //private DepartmentRepository departmentRepository;
    private List<Department> departments = new ArrayList<>();

    public Department createDepartment(Department department){
        Department department1 = new Department();
        return department;
    }
    public List<Department> getAllDepartments() {
        return  departments;
    }
    public String getAllDepartment() {
        return  "SUCCESS";
    }

    public Department getDepartmentById(String id) {
        for (Department department : departments){
            if (department.getId().equals(id)){
                return  department;
            }
        }
        return null;
    }

    public void deleteDepartment(String id) {
        departments.removeIf(department -> department.getId().equals(id));
    }

    public Department updateDepartment(String id, Department department) {
        for(int i = 0; i < departments.size(); i++){
            Department existingDepartment = departments.get(i);
            if (department.getId().equals(id)){
                departments.set( i,department);
                return department;
            }
        }
        return null;
    }
}
