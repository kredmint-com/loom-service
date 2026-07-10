package com.kredmint.loom.leave.service;

import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.repository.LeaveBalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class LeaveBalanceService {

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    public LeaveBalance createLeaveBalance(LeaveBalance leaveBalance) {
        if (!validateLeaveBalance(leaveBalance)) {
            throw new RuntimeException("Invalid leave balance data.");
        }
       validateBalance(leaveBalance.getOpeningBalance(),"Opening Balance");
        validateBalance(leaveBalance.getAccruedLeave(),"Accured Leave");
        validateBalance(leaveBalance.getCarriedForward(),"Carried Forward");
        validateBalance(leaveBalance.getUsedLeave(),"Used Leave");
        validateBalance(leaveBalance.getPendingLeave(),"Pending Leave");
        validateBalance(leaveBalance.getEncashedLeave(),"Encashed Leave");
        validateBalance(leaveBalance.getMaximumAllowed(),"Maximum Allowed");
        validateBalance(leaveBalance.getCarryForwardLimit(),"Carry Forward Limit");

        if (isDuplicateLeaveBalance(leaveBalance)){
            throw new RuntimeException("Leave balance already exists for this employee.");
        }
        leaveBalance.setAvailableBalance(calculateAvailableBalance(leaveBalance));
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
    public LeaveBalance updateLeaveBalance(String id, LeaveBalance leaveBalance) {
        if (!leaveBalanceRepository.existsById(id)) {
            throw new RuntimeException("Leave balance not found.");
        }
        leaveBalance.setId(id);

        if (!validateLeaveBalance(leaveBalance)) {
            throw new RuntimeException("Invalid leave balance data.");
        }
        validateBalance(leaveBalance.getOpeningBalance(), "Opening Balance");
        validateBalance(leaveBalance.getAccruedLeave(), "Accrued Leave");
        validateBalance(leaveBalance.getCarriedForward(), "Carried Forward");
        validateBalance(leaveBalance.getUsedLeave(), "Used Leave");
        validateBalance(leaveBalance.getPendingLeave(), "Pending Leave");
        validateBalance(leaveBalance.getEncashedLeave(), "Encashed Leave");
        validateBalance(leaveBalance.getMaximumAllowed(), "Maximum Allowed");
        validateBalance(leaveBalance.getCarryForwardLimit(), "Carry Forward Limit");

        leaveBalance.setAvailableBalance(calculateAvailableBalance(leaveBalance));
        return leaveBalanceRepository.save(leaveBalance);
    }

    private boolean validateLeaveBalance(LeaveBalance leaveBalance) {
        return hasRequiredFields(leaveBalance);
    }

    private void validateBalance(double value, String fieldName){
        if (value<0){
            throw new RuntimeException(fieldName+ " cannot be negative.");
        }
    }

        private boolean hasRequiredFields(LeaveBalance leaveBalance) {
            return leaveBalance.getEmployeeId() != null
                    && leaveBalance.getEmployeeName() != null
                    && leaveBalance.getDepartment() != null
                    && leaveBalance.getDesignation() != null
                    && leaveBalance.getLeaveType() != null
                    && leaveBalance.getYear() != null
                    && leaveBalance.getCarryForwardAllowed() != null;
        }

        public LeaveBalance getLeaveBalanceByEmployeeIdAndLeaveTypeAndYear(
                String employeeId,
                LeaveBalance.LeaveType leaveType,
                Integer year){
            return leaveBalanceRepository.findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year).orElse(null);
        }


    private boolean isDuplicateLeaveBalance(LeaveBalance leaveBalance) {
        return getLeaveBalanceByEmployeeIdAndLeaveTypeAndYear(
                        leaveBalance.getEmployeeId(),
                        leaveBalance.getLeaveType(),
                        leaveBalance.getYear()
        )!=null;
    }

    private  double calculateAvailableBalance(LeaveBalance leaveBalance) {
        return leaveBalance.getOpeningBalance()
                +leaveBalance.getAccruedLeave()
                +leaveBalance.getCarriedForward()
                -leaveBalance.getUsedLeave()
                -leaveBalance.getPendingLeave()
                -leaveBalance.getEncashedLeave();
    }

    public void deleteLeaveBalance(String id) {

        if (!leaveBalanceRepository.existsById(id)) {
            throw new RuntimeException("Leave balance not found.");
        }
        leaveBalanceRepository.deleteById(id);
    }
    }
