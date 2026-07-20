package com.kredmint.loom.leave.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Getter
@Setter
@Document("leave_balance")
public class LeaveBalance {

    @Id
    private String id;

    private String employeeId;
    private String employeeName;
    private String department;
    private String designation;

    private LeaveType leaveType;

    private Integer year;
    private double openingBalance;

    // Leave credited during the year
    private double accruedLeave;

    // Leave carried forward from previous year
    private double carriedForward;

    // Leave already consumed
    private double usedLeave;

    // Pending approval requests
    private double pendingLeave;

    // Leave encashed
    private double encashedLeave;

    // Current available balance
    private double availableBalance;

    // Maximum leave allowed
    private double maximumAllowed;

    // Whether carry forward is allowed
    private Boolean carryForwardAllowed;

    // Maximum carry forward limit
    private double carryForwardLimit;

    // Remarks
    private String remarks;

    public enum LeaveType {
        CASUAL,
        SICK,
        EARNED,
        MATERNITY,
        PATERNITY,
        BEREAVEMENT,
        COMPENSATORY,
        UNPAID
    }
}
