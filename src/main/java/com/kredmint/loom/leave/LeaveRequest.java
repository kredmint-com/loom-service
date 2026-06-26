package com.kredmint.loom.leave;

import com.kredmint.loom.employee.Employee;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {
    private Long id;
    private Employee employee;
    private LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalDays;
    private String reason;
    
    @Builder.Default
    private LeaveStatus status = LeaveStatus.PENDING;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
