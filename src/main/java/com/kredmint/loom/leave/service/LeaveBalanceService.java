package com.kredmint.loom.leave.service;

import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.repository.LeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;


@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalance createLeaveBalance(LeaveBalance leaveBalance) {
        if (!validateLeaveBalance(leaveBalance)) {
            throw new RuntimeException("Invalid leave balance data.");
        }
        if (isDuplicateLeaveBalance(leaveBalance)) {
            throw new RuntimeException("Leave balance already exists for this employee.");
        }
        leaveBalance.setAvailableBalance(
                calculateAvailableBalance(leaveBalance));
        return leaveBalanceRepository.save(leaveBalance);
    }

    public LeaveBalance getLeaveBalanceById(String id) {
        return leaveBalanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave balance not found."));
    }

    public List<LeaveBalance> getEmployeeLeaveBalances(String employeeId) {
        List<LeaveBalance> leaveBalances =
                leaveBalanceRepository.findByEmployeeId(employeeId);
        if (leaveBalances.isEmpty()) {
            throw new RuntimeException("No leave balances found for this employee.");
        }
        return leaveBalances;
    }
    public LeaveBalance updateLeaveBalance(LeaveBalance leaveBalance) {

        if (!leaveBalanceRepository.existsById(leaveBalance.getId())) {
            throw new RuntimeException("Leave balance not found.");
        }

        if (!validateLeaveBalance(leaveBalance)) {
            throw new RuntimeException("Invalid leave balance data.");
        }

        calculateAvailableBalance(leaveBalance);

        return leaveBalanceRepository.save(leaveBalance);
    }

    private boolean validateLeaveBalance(LeaveBalance leaveBalance) {
            return hasRequiredFields(leaveBalance)
                    && isNonNegative(leaveBalance.getOpeningBalance())
                    && isNonNegative(leaveBalance.getMaximumAllowed())
                    && (leaveBalance.getCarryForwardLimit() == null
                    || isNonNegative(leaveBalance.getCarryForwardLimit()));
    }

        private boolean hasRequiredFields(LeaveBalance leaveBalance) {
            return leaveBalance.getEmployeeId() != null
                    && leaveBalance.getEmployeeName() != null
                    && leaveBalance.getDepartment() != null
                    && leaveBalance.getDesignation() != null
                    && leaveBalance.getLeaveType() != null
                    && leaveBalance.getYear() != null
                    && leaveBalance.getOpeningBalance() != null
                    && leaveBalance.getMaximumAllowed() != null
                    && leaveBalance.getCarryForwardAllowed() != null;
        }

        private boolean isNonNegative(BigDecimal value) {
            return value.compareTo(BigDecimal.ZERO) >= 0;
        }

    private boolean isDuplicateLeaveBalance(LeaveBalance leaveBalance) {
        return leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(
                        leaveBalance.getEmployeeId(),
                        leaveBalance.getLeaveType(),
                        leaveBalance.getYear()
                )
                .isPresent();
    }

    private  BigDecimal  calculateAvailableBalance(LeaveBalance leaveBalance) {
        BigDecimal availableBalance = getValue(leaveBalance.getOpeningBalance())
                .add(getValue(leaveBalance.getAccruedLeave()))
                .add(getValue(leaveBalance.getCarriedForward()))
                .subtract(getValue(leaveBalance.getUsedLeave()))
                .subtract(getValue(leaveBalance.getPendingLeave()))
                .subtract(getValue(leaveBalance.getEncashedLeave()));
        leaveBalance.setAvailableBalance(availableBalance);

        return availableBalance;
    }

    private BigDecimal getValue(BigDecimal value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        return value;
    }

    public void deleteLeaveBalance(String id) {

        if (!leaveBalanceRepository.existsById(id)) {
            throw new RuntimeException("Leave balance not found.");
        }

        leaveBalanceRepository.deleteById(id);
    }
    }
