package com.kredmint.loom.approval;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends MongoRepository<ApprovalRequest, String> {
}
