package com.kredmint.loom.asset;

import com.kredmint.loom.employee.entity.Employee;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetAssignment {
    private Long id;
    private Asset asset;
    private Employee employee;
    private LocalDate assignedDate;
    private LocalDate returnedDate;
    
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, RETURNED
    
    private String comments;
}
