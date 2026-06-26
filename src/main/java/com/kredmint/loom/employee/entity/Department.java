package com.kredmint.loom.employee.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    private String id;
    private String name;
    private String code;
}
