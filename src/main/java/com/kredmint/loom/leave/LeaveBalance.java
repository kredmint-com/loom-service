package com.kredmint.loom.leave;

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
    private BigDecimal openingBalance;

    // Leave credited during the year
    private BigDecimal accruedLeave;

    // Leave carried forward from previous year
    private BigDecimal carriedForward;

    // Leave already consumed
    private BigDecimal usedLeave;

    // Pending approval requests
    private BigDecimal pendingLeave;

    // Leave encashed
    private BigDecimal encashedLeave;

    // Current available balance
    private BigDecimal availableBalance;

    // Maximum leave allowed
    private BigDecimal maximumAllowed;

    // Whether carry forward is allowed
    private Boolean carryForwardAllowed;

    // Maximum carry forward limit
    private BigDecimal carryForwardLimit;

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
