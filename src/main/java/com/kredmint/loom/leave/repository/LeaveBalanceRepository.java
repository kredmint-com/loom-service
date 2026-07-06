package com.kredmint.loom.leave.repository;

import com.kredmint.loom.leave.entity.LeaveBalance;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceRepository extends MongoRepository<LeaveBalance, String> {
    Optional<LeaveBalance> findByEmployeeIdAndLeaveTypeAndYear(
            String employeeId,
            LeaveBalance.LeaveType leaveType,
            Integer year
    );
    List<LeaveBalance> findByEmployeeId(String employeeId);

}
