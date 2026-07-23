package com.kredmint.loom.approval;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/approvals")
public class ApprovalController {

    @Autowired
    private ApprovalService approvalService;

    @PostMapping("/create")
    public ApprovalRequest create(@RequestBody ApprovalRequest approvalRequest){
        return approvalService.create(approvalRequest);
    }

    @GetMapping("/{approvalRequestId}")
    public ApprovalRequest getById(@PathVariable String approvalRequestId){
        return approvalService.getById(approvalRequestId);
    }

    @GetMapping("/entity")
    public ApprovalRequest getByEntity(@RequestParam String entityId,
                                       @RequestParam ApprovalRequest.ApprovalEntityType entityType) {
        return approvalService.getByEntity(entityId, entityType);
    }

    @PutMapping("/{approvalRequestId}/forward")
    public ApprovalRequest forward(@PathVariable String approvalRequestId,
                                   @RequestParam String approverId,
                                   @RequestParam String remarks) {
        return approvalService.forward(approvalRequestId, approverId, remarks);
    }

    @PutMapping("/{approvalRequestId}/withdraw")
    public ApprovalRequest withdraw(@PathVariable String approvalRequestId,
                                    @RequestParam String employeeId) {
        return approvalService.withdraw(approvalRequestId, employeeId);
    }

    @GetMapping("/pending")
    public Page<ApprovalRequest> getPendingApprovals(@RequestParam String approverId,
                                                    @PageableDefault(size = 10, page = 0) Pageable pageable) {
        return approvalService.getPendingApprovals(approverId, pageable);
    }

    @GetMapping("/history")
    public List<ApprovalRequest> getApprovalHistory(@RequestParam String approverId) {
        return approvalService.getApprovalHistory(approverId);
    }

    @PutMapping("/{approvalRequestId}/action")
    public ApprovalRequest updateStatus(@PathVariable String approvalRequestId,
                                        @RequestBody  ApprovalRequestDto request,
                                        @RequestParam ApprovalStatus status) {

        return approvalService.updateStatus(
                approvalRequestId,
                request,
                status);
    }
}

