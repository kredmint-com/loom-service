package com.kredmint.loom.employee.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {

    @Id
    private String id;
    private String name;
    private String code;
}
