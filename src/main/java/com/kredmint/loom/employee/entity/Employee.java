package com.kredmint.loom.employee.entity;

import com.kredmint.loom.user.User;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {
    private Long id;
    private String employeeCode;
    private User user;
    private LocalDate dateOfJoining;
    private String designation;
    private Department department;
    private Employee manager;
    
    @Builder.Default
    private String status = "ACTIVE";
}
