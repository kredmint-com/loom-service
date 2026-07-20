package com.kredmint.loom.leave.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document("leave_request")
public class LeaveRequest {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String department;
    private String designation;

    private String managerId;
    private String managerName;

    private LeaveBalance.LeaveType leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean halfDay;
    private HalfDaySession halfDaySession;
    private double totalDays;

    private String reason;

    private List<String> attachmentUrls;
    private LeaveStatus status;
    private LocalDateTime requestedOn;
    private LocalDateTime actionOn;

    private String approvedBy;
    private String approverRemarks;
    private String escalatedTo;

//    private boolean deleted;

    public enum LeaveStatus {
        DRAFT,
        PENDING,
        APPROVED,
        REJECTED,
        CANCELLED,
        WITHDRAWN
    }

    public enum HalfDaySession {
        FIRST_HALF,
        SECOND_HALF
    }
}
