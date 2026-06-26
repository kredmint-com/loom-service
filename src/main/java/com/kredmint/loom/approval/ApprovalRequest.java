package com.kredmint.loom.approval;

import com.kredmint.loom.employee.Employee;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalRequest {
    private Long id;
    private String entityType; // e.g. "LeaveRequest", "AssetAssignment"
    private Long entityId;     // Reference to the target entity's ID
    private Employee requester;
    private Employee currentApprover;
    
    @Builder.Default
    private ApprovalStatus status = ApprovalStatus.PENDING;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
