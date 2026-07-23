package com.kredmint.loom.leave.controller;

import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.entity.LeaveRequest;
import com.kredmint.loom.leave.service.LeaveBalanceService;
import com.kredmint.loom.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @Autowired
    private LeaveBalanceService leaveBalanceService;

    //LeaveRequest
    @PostMapping
    public LeaveRequest raiseRequest(@RequestBody LeaveRequest request) {
        return leaveService.raiseRequest(request);
    }

    @GetMapping("/{requestId}")
    public LeaveRequest getRequestById(@PathVariable String requestId) {
        return leaveService.getRequestById(requestId);
    }

    @GetMapping("/employee/{employeeId}")
    public Page<LeaveRequest> getEmployeeRequests(
            @PathVariable String employeeId,
            @PageableDefault Pageable pageable)
             {

        return leaveService.getEmployeeRequests(
                employeeId, pageable);
    }

    @GetMapping("/pending/{managerId}")
    public Page<LeaveRequest> getPendingApprovals(
            @PathVariable String managerId,
            @PageableDefault Pageable pageable) {

        return leaveService.getPendingApprovals(
                managerId, pageable);
    }

    @PutMapping("/status/{requestId}")
    public LeaveRequest updateStatus(
            @PathVariable String requestId,
            @RequestParam LeaveRequest.LeaveStatus status,
            @RequestParam String actedBy,
            @RequestParam String remarks) {

        return leaveService.updateStatus(
                requestId,
                status,
                actedBy,
                remarks);
    }

    @PutMapping("/{requestId}")
    public LeaveRequest updateRequest(
            @PathVariable String requestId,
            @RequestBody LeaveRequest request) {
        return leaveService.updateRequest(requestId, request);
    }

    //LeaveBalance
    @PostMapping("/balance")
    public LeaveBalance createLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
        return leaveBalanceService.createLeaveBalance(leaveBalance);
    }

    @GetMapping("/balance/{id}")
    public LeaveBalance getLeaveBalanceById(@PathVariable String id) {
        return leaveBalanceService.getLeaveBalanceById(id);
    }

    @GetMapping("/balance/employee/{employeeId}")
    public List<LeaveBalance> getEmployeeLeaveBalances(@PathVariable String employeeId) {
        return leaveBalanceService.getEmployeeLeaveBalances(employeeId);
    }
    @PutMapping("/balance/{id}")
    public LeaveBalance updateLeaveBalance(@PathVariable String id, @RequestBody LeaveBalance leaveBalance) {
        return leaveBalanceService.updateLeaveBalance(id, leaveBalance);
    }
    @DeleteMapping("/balance/{id}")
    public void deleteLeaveBalance(@PathVariable String id) {
        leaveBalanceService.deleteLeaveBalance(id);
    }
}
