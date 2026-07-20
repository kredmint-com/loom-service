package com.kredmint.loom.attendance.repository;

import com.kredmint.loom.attendance.entity.Attendance;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    Attendance findByEmployeeIdAndDate(String employeeId, LocalDate date);
}