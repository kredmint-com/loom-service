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
    private String id; // used in leave module

    private String username;
    private String referenceId;
    private String email;
    private String phoneNumber;
    private Date dateOfJoining;
    private String designation;
    private String department;
    private String band;
    private String managerId;
    private String status;
}


