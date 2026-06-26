package com.kredmint.loom.approval;

import com.kredmint.loom.employee.entity.Employee;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApprovalHistory {
    private Long id;
    private ApprovalRequest approvalRequest;
    private Employee approver;
    private ApprovalStatus action;
    private LocalDateTime actionDate;
    private String comments;
}
