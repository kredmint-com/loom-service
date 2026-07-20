package com.kredmint.loom.attendance;

import com.kredmint.loom.attendance.entity.Attendance;
import com.kredmint.loom.attendance.repository.AttendanceRepository;
import com.kredmint.loom.attendance.repository.HolidayRepository;
import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.repository.EmployeeRepository;
import com.kredmint.loom.employee.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class AttendanceScheduler {
    private AttendanceRepository attendanceRepository;
    private EmployeeService employeeService;
    private HolidayRepository holidayRepository;


   @Scheduled(cron = "0 0 0 * * *")
    public void syncAttendance() {

        LocalDate today = LocalDate.now();
        Attendance.Type type;

        if(holidayRepository.existsByDate(today)){
            type = Attendance.Type.HOLIDAY;
        }

        else if (today.getDayOfWeek()== DayOfWeek.SATURDAY || today.getDayOfWeek() == DayOfWeek.SUNDAY){
            type = Attendance.Type.WEEKEND;
        }

        else{
            type = Attendance.Type.WORKING;
        }

        List<Employee> employees = employeeService.getAllEmployees();

        List<Attendance> attendanceList = new ArrayList<>();

        for (Employee employee : employees) {

            Attendance attendance = Attendance.builder()
                    .employeeId(employee.getId())
                    .date(LocalDate.now())
                    .status(Attendance.Status.ABSENT)
                    .type(Attendance.Type.WORKING)
                    .build();

            attendanceRepository.saveAll(attendanceList);
        }
    }
}