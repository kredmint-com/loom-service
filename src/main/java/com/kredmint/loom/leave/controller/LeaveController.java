package com.kredmint.loom.leave.controller;

import com.kredmint.loom.leave.entity.LeaveBalance;
import com.kredmint.loom.leave.entity.LeaveRequest;
import com.kredmint.loom.leave.service.LeaveBalanceService;
import com.kredmint.loom.leave.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    @PostMapping("/raise")
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return leaveService.getEmployeeRequests(
                employeeId,
                PageRequest.of(page, size));
    }

    @GetMapping("/pending/{managerId}")
    public Page<LeaveRequest> getPendingApprovals(
            @PathVariable String managerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return leaveService.getPendingApprovals(
                managerId,
                PageRequest.of(page, size));
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

    @GetMapping("/balance/{id}") //LB001
    public LeaveBalance getLeaveBalanceById(@PathVariable String id) {
        return leaveBalanceService.getLeaveBalanceById(id);
    }

    @GetMapping("/balance/employee/{employeeId}")
    public List<LeaveBalance> getEmployeeLeaveBalances(@PathVariable String employeeId) {
        return leaveBalanceService.getEmployeeLeaveBalances(employeeId);
    }
    @PutMapping("/balance/{id}") //LB001
    public LeaveBalance updateLeaveBalance(@RequestBody LeaveBalance leaveBalance) {
        return leaveBalanceService.updateLeaveBalance(leaveBalance);
    }
    @DeleteMapping("/balance/{id}") //LB001
    public void deleteLeaveBalance(@PathVariable String id) {
        leaveBalanceService.deleteLeaveBalance(id);
    }
}
