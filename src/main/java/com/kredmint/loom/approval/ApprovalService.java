package com.kredmint.loom.approval;

import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalService {

    @Autowired
    private ApprovalRepository approvalRepository;

    @Autowired
    private EmployeeService employeeService;

    public ApprovalRequest create(ApprovalRequest approvalRequest) {

        approvalRequest.setStatus(ApprovalStatus.PENDING);
        approvalRequest.setCreatedAt(LocalDateTime.now());
        approvalRequest.setAssignedAt(LocalDateTime.now());

        return approvalRepository.save(approvalRequest);
    }

    public ApprovalRequest getById(String approvalRequestId) {
        return approvalRepository.findById(approvalRequestId)
                .orElseThrow(()-> new RuntimeException("Id not found"));
    }

    public ApprovalRequest getByEntity(String entityId, ApprovalRequest.ApprovalEntityType entityType) {
        return approvalRepository.findByEntityIdAndEntityType(entityId, entityType).orElseThrow(()-> new RuntimeException("Entity not found"));
    }

    public ApprovalRequest forward(String approvalRequestId,
                                   String approverId,
                                   String remarks) {

     ApprovalRequest approvalRequest = getById(approvalRequestId);

     if(!approvalRequest.getApproverId().equals(approverId)){
         throw new RuntimeException("You aren't authorized to approve this approval.");
     }

     if(approvalRequest.getStatus() != ApprovalStatus.PENDING){
         throw new RuntimeException("Only pending approval requests can be forwarded.");
     }

        Employee currentApprover = employeeService.getById(approverId);

     if(currentApprover.getManagerId() == null){
         throw new RuntimeException("No manager found for this approval.");
     }
     Employee nextApprover = employeeService.getById(currentApprover.getManagerId());

     approvalRequest.setStatus(ApprovalStatus.FORWARDED);
     approvalRequest.setRemarks(remarks);
     approvalRequest.setActionAt(LocalDateTime.now());

     approvalRepository.save(approvalRequest);

     ApprovalRequest newRequest = new ApprovalRequest();

        newRequest.setEntityId(approvalRequest.getEntityId());
        newRequest.setEntityType(approvalRequest.getEntityType());
        newRequest.setEmpId(approvalRequest.getEmpId());

        newRequest.setApproverId(nextApprover.getId());
        newRequest.setApproverName(nextApprover.getUsername());

        return create(newRequest);

    }

    public ApprovalRequest withdraw(String approvalRequestId,
                                    String employeeId) {

    ApprovalRequest approvalRequest = getById(approvalRequestId);

    if (approvalRequest==null){
        throw new  RuntimeException("Approval request not found");
    }

    if(!approvalRequest.getEmpId().equals(employeeId)){
        throw new RuntimeException("You aren't authorized to withdraw this request.");
    }

    if(approvalRequest.getStatus() != ApprovalStatus.PENDING){
        throw new RuntimeException("Only pending approval requests can be withdrawn.");
    }

    approvalRequest.setStatus(ApprovalStatus.WITHDRAWN);
    approvalRequest.setActionAt(LocalDateTime.now());
    approvalRequest.setCompletedAt(LocalDateTime.now());

    return approvalRepository.save(approvalRequest);
    }

    public Page<ApprovalRequest> getPendingApprovals(String approverId,
                                                     Pageable pageable) {
       return approvalRepository.findByApproverIdAndStatus(approverId, ApprovalStatus.PENDING, pageable);
    }

    public List<ApprovalRequest> getApprovalHistory(String approverId) {
       return approvalRepository.findByApproverIdAndStatus(approverId, ApprovalStatus.APPROVED);
    }

    public ApprovalRequest updateStatus(String approvalRequestId,
                                        ApprovalRequestDto request,
                                        ApprovalStatus status) {

        ApprovalRequest approvalRequest = getById(approvalRequestId);


        if (approvalRequest.getStatus() != ApprovalStatus.PENDING) {
            throw new RuntimeException("Approval request is not pending.");
        }

        switch(status){
            case APPROVED:
                if(!approvalRequest.getApproverId().equals(request.getApproverId())){
                    throw new RuntimeException("You aren't authorized to approve this approval.");
                }
                approvalRequest.setStatus(ApprovalStatus.APPROVED);
                break;

            case REJECTED:
                if(!approvalRequest.getApproverId().equals(request.getApproverId())){
                    throw new RuntimeException("You aren't authorized to approve this approval.");
                }
                approvalRequest.setStatus(ApprovalStatus.REJECTED);
                break;

            case SENT_BACK:
                if(!approvalRequest.getApproverId().equals(request.getApproverId())){
                    throw new RuntimeException("You aren't authorized to approve this approval.");
                }
                approvalRequest.setStatus(ApprovalStatus.SENT_BACK);
                break;

            case FORWARDED:
                return forward(approvalRequestId,
                        request.getApproverId(),
                        request.getRemarks());

            case WITHDRAWN:
                return withdraw(approvalRequestId, request.getEmployeeId());

            default:
                throw new RuntimeException("Invalid action");

        }
        approvalRequest.setRemarks(request.getRemarks());
        approvalRequest.setActionAt(LocalDateTime.now());
        approvalRequest.setCompletedAt(LocalDateTime.now());

        return approvalRepository.save(approvalRequest);
    }
}
