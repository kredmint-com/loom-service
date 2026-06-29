package com.kredmint.loom.employee.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    private String id;

    private String username;
    private String employeeCode;
    private Date dateOfJoining;
    private String designation;
    private String department;
    private String managerId;
    private String status;
}
