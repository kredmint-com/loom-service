package com.kredmint.loom.approval;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ApprovalRepository extends MongoRepository<ApprovalRequest, String> {

    Optional<ApprovalRequest> findByEntityIdAndEntityType(String id, ApprovalRequest.ApprovalEntityType entityType);

    Page<ApprovalRequest> findByApproverIdAndStatus(String approverId, ApprovalStatus status, Pageable pageable);

    List<ApprovalRequest> findByApproverIdAndStatus(String approverId, ApprovalStatus status);

}
