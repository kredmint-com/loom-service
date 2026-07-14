package com.kredmint.loom.attendance.repository;

import com.kredmint.loom.attendance.entity.Holiday;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;

public interface HolidayRepository extends MongoRepository<Holiday, String > {
    boolean existsByDate(LocalDate date);
}
