package com.kredmint.loom.leave.service;

import com.kredmint.loom.approval.ApprovalRequest;
import com.kredmint.loom.approval.ApprovalService;
import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.service.EmployeeService;
import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.entity.LeaveRequest;
import com.kredmint.loom.leave.repository.LeaveBalanceRepository;
import com.kredmint.loom.leave.repository.LeaveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private LeaveBalanceRepository leaveBalanceRepository;

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ApprovalService approvalService;

    public LeaveRequest raiseRequest(LeaveRequest request) {
        if (!validateLeaveRequest(request)) {
            return null;
        }
        request.setTotalDays(calculateLeaveDays(request));
        assignManager(request);
        request.setStatus(LeaveRequest.LeaveStatus.PENDING);
        request.setRequestedOn(LocalDateTime.now());

        LeaveRequest savedRequest = leaveRepository.save(request);

        //creatingApproval
        ApprovalRequest approvalRequest = new ApprovalRequest();

        approvalRequest.setEntityId(savedRequest.getId());
        approvalRequest.setEntityType(ApprovalRequest.ApprovalEntityType.LEAVE);
        approvalRequest.setEmpId(savedRequest.getEmployeeId());
        approvalRequest.setApproverId(savedRequest.getManagerId());
        approvalRequest.setApproverName(savedRequest.getManagerName());

        approvalService.create(approvalRequest);
        return savedRequest;

    }

    public LeaveRequest getRequestById(String requestId) {
        return leaveRepository.findById(requestId).orElse(null);
    }

    public Page<LeaveRequest> getEmployeeRequests(String employeeId, Pageable pageable) {

        Query query = new Query();
        query.addCriteria(Criteria.where("employeeId").is(employeeId));

        long total = mongoTemplate.count(query, LeaveRequest.class);
        query.with(pageable);

        List<LeaveRequest> leaveRequests = mongoTemplate.find(query, LeaveRequest.class);

        return new PageImpl<>(leaveRequests, pageable, total);
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
            throw new RuntimeException("Leave request not found.");
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
            throw new RuntimeException("Leave request not found");
        }
        if (existingRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Only pending leave request can be updated");
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


    public double calculateLeaveDays(LeaveRequest request) {

        long totalDays = ChronoUnit.DAYS.between(
                request.getStartDate(),
                request.getEndDate()) + 1;

        if (request.isHalfDay()) {
            return totalDays - 0.5;
        }
        return totalDays;
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
        double leaveDays = calculateLeaveDays(request);
        return leaveDays <= leaveBalance.getAvailableBalance();
    }

    public void assignManager(LeaveRequest request) {
        Employee employee = employeeService.getById(request.getEmployeeId());
        Employee manager = employeeService.getById(employee.getManagerId());
        request.setManagerId(manager.getId());
        request.setManagerName(manager.getUsername());
    }

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
   // }

