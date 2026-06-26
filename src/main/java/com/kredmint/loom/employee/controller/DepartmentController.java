package com.kredmint.loom.employee.controller;

import com.kredmint.loom.employee.entity.Department;
import com.kredmint.loom.employee.repository.DepartmentRepository;
import com.kredmint.loom.employee.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/department")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/get")
    public String getAllDepartments(){
        return departmentService.getAllDepartment();
    }

    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable String id){
        return departmentService.getDepartmentById(id);
    }

    @PostMapping("/create")
    public Department createDepartment(@RequestBody Department department){
       // log.info("createDepartment");
        return departmentService.createDepartment(department);
    }
    @PutMapping("/{id}")
    public Department updateDepartment(@PathVariable String id, @RequestBody Department department){
        return departmentService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
        public void deleteDepartment(@PathVariable String id){
        departmentService.deleteDepartment(id);
    }
}
