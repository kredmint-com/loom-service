package com.kredmint.loom.employee;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {
    private Long id;
    private String name;
    private String code;
    private Employee manager;
}
