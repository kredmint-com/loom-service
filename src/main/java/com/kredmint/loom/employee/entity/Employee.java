package com.kredmint.loom.employee.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private String id;

    private String username;
    private String employeeCode;
    private String email;
    private String phoneNumber;
    private Date dateOfJoining;
    private String designation;
    private String department;
    private String band;
    private String managerId;
    private String status;
}


