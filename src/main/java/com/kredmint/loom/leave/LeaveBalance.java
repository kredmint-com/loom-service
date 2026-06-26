package com.kredmint.loom.leave;

import com.kredmint.loom.employee.entity.Employee;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveBalance {
    private Long id;
    private Employee employee;
    private LeaveType leaveType;
    private Double totalAllocated;
    
    @Builder.Default
    private Double usedLeaves = 0.0;
    
    private Double remainingLeaves;
    private Integer year;

    public void updateRemaining() {
        this.remainingLeaves = (this.totalAllocated != null ? this.totalAllocated : 0.0) - (this.usedLeaves != null ? this.usedLeaves : 0.0);
    }
}
