package com.kredmint.loom.approval;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document("approval_request")
public class ApprovalRequest {

    @Id
    private String id;

    private String entityId;      // LeaveRequest.id
    private ApprovalEntityType entityType; // LEAVE

    private ApprovalStatus status;

    private int level;

    private String empId;
    private String approverId;
    private String approverName;

    private LocalDateTime assignedAt;
    private LocalDateTime actionAt;

    private String remarks;

    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    public enum ApprovalEntityType {
        LEAVE, WORK_FROM_HOME
    }
}