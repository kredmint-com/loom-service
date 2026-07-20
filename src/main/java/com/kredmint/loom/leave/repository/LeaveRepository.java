package com.kredmint.loom.leave.repository;

import com.kredmint.loom.leave.entity.LeaveRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeaveRepository extends MongoRepository<LeaveRequest, String> {

    Page<LeaveRequest> findByEmployeeId(String employeeId, Pageable pageable);

    Page<LeaveRequest> findByManagerIdAndStatus(
            String managerId,
            LeaveRequest.LeaveStatus status,
            Pageable pageable
    );
}