package com.kredmint.loom.leave.service;

import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.service.EmployeeService;
import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.entity.LeaveRequest;
import com.kredmint.loom.leave.repository.LeaveBalanceRepository;
import com.kredmint.loom.leave.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class LeaveService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    public LeaveRequest raiseRequest(LeaveRequest request) {
        if (!validateLeaveRequest(request)) {
            return null;
        }
        request.setTotalDays(calculateLeaveDays(request));
        assignManager(request);
        request.setStatus(LeaveRequest.LeaveStatus.PENDING);
        request.setRequestedOn(LocalDateTime.now());
        return leaveRepository.save(request);
    }

    public LeaveRequest getRequestById(String requestId) {
        return leaveRepository.findById(requestId).orElse(null);
    }

    public Page<LeaveRequest> getEmployeeRequests(String employeeId, Pageable pageable) {
        return leaveRepository.findByEmployeeId(employeeId, pageable);
    }

    public Page<LeaveRequest> getPendingApprovals(String managerId, Pageable pageable) {
        return leaveRepository.findByManagerIdAndStatus(managerId, LeaveRequest.LeaveStatus.PENDING, pageable);
    }

    public LeaveRequest updateStatus(String requestId,
                                     LeaveRequest.LeaveStatus status,
                                     String actedBy,
                                     String remarks) {

        LeaveRequest request = getRequestById(requestId);

        if (request == null) {
            return null;
        }
        request.setStatus(status);
        request.setApprovedBy(actedBy);
        request.setApproverRemarks(remarks);
        request.setActionOn(LocalDateTime.now());

        return leaveRepository.save(request);
    }

    public LeaveRequest updateRequest(String requestId, LeaveRequest request) {
        LeaveRequest existingRequest = getRequestById(requestId);

        if (existingRequest == null) {
            return null;
        }

        if (existingRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            return null;
        }
        existingRequest.setLeaveType(request.getLeaveType());
        existingRequest.setStartDate(request.getStartDate());
        existingRequest.setEndDate(request.getEndDate());
        existingRequest.setHalfDay(request.isHalfDay());
        existingRequest.setHalfDaySession(request.getHalfDaySession());
        existingRequest.setReason(request.getReason());
        existingRequest.setTotalDays(calculateLeaveDays(request));

        if (!validateLeaveRequest(existingRequest)) {
            return null;
        }
        assignManager(existingRequest);
        return leaveRepository.save(existingRequest);
    }

    public boolean validateLeaveRequest(LeaveRequest request) {
        if (request.getEmployeeId() == null ||
                request.getLeaveType() == null ||
                request.getStartDate() == null ||
                request.getEndDate() == null ||
                request.getReason() == null) {
            return false;
        }
        if (request.getStartDate().isAfter(request.getEndDate())) {
            return false;
        }
        return hasSufficientLeaveBalance(request.getEmployeeId(), request);
    }


    public BigDecimal calculateLeaveDays(LeaveRequest request) {
        long totalDays = ChronoUnit.DAYS.between(
                request.getStartDate(),
                request.getEndDate()) + 1;
        if (request.isHalfDay()) {
            return BigDecimal.valueOf(totalDays)
                    .subtract(BigDecimal.valueOf(0.5));
        }
        return BigDecimal.valueOf(totalDays);
    }

    public boolean hasSufficientLeaveBalance(String employeeId, LeaveRequest request) {

        LeaveBalance leaveBalance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(
                        employeeId,
                        request.getLeaveType(),
                        request.getStartDate().getYear()
                ).orElse(null);

        if (leaveBalance == null) {
            return false;
        }
        BigDecimal leaveDays = calculateLeaveDays(request);
        return leaveDays.compareTo(leaveBalance.getAvailableBalance()) <= 0;
    }

        public void assignManager (LeaveRequest request){
            Employee employee = employeeService.getById(request.getEmployeeId());
            Employee manager = employeeService.getById(employee.getManagerId());
            request.setManagerId(manager.getId());
            request.setManagerName(manager.getUsername());
        }





   // public void notifyManager(LeaveRequest request) {
        // TODO:
        // Notify the manager about a newly created
        // or updated leave request.
    //}

    //public void notifyEmployee(LeaveRequest request) {
        // TODO:
        // Notify the employee about any status change
        // or action taken on the request.
    }

