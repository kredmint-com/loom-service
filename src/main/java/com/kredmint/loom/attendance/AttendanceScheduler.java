package com.kredmint.loom.attendance;

import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.repository.EmployeeRepository;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@EnableScheduling
public class AttendanceScheduler {
    private AttendanceRepository attendanceRepository;
    private EmployeeRepository employeeRepository;



    @Scheduled(cron = "0 0 0 * * *")
    public void syncAttendance() {
        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {
            Attendance attendance = Attendance.builder()
                    .employeeId(employee.getId())
                    .date(LocalDate.now())
                    .status(Attendance.Status.ABSENT)
                    .type(Attendance.Type.WORKING)
                    .build();

            attendanceRepository.save(attendance);
    }
}
}
