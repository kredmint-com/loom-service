package com.kredmint.loom.attendance;

import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    Page<Attendance> findByEmployeeIdAndStatusAndType(String employeeId, Attendance.Status status, Attendance.Type type, Pageable paga
    );
}
