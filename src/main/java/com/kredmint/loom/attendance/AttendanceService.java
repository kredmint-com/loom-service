package com.kredmint.loom.attendance;

import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    @Autowired
    private final MongoTemplate mongoTemplate;

    private final AttendanceRepository attendanceRepository;

    private final EmployeeRepository employeeRepository;

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance updateLoginTime(LocalTime loginTime, String employeeId, LocalDate date) {
        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, date);
        attendance.setLoginTime(loginTime);
        attendance.setStatus(Attendance.Status.PRESENT);
        return attendanceRepository.save(attendance);
    }

    public Attendance updateLogoutTime(LocalTime logoutTime, String employeeId, LocalDate date) {
        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, date);
        attendance.setLogoutTime(logoutTime);
        return attendanceRepository.save(attendance);
    }

    public Page<Attendance> getAttendance(String employeeId,
                                          Attendance.Status status,
                                          Attendance.Type type,
                                          Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("employeeId").is(employeeId));
        if (status!=null){
            query.addCriteria(Criteria.where("status").is(status));
        }
        if(type!=null){
            query.addCriteria(Criteria.where("type").is(type));
        }
        long count = mongoTemplate.count(query, Attendance.class);
        query.with(pageable);
        List<Attendance> attendanceList= mongoTemplate.find(query, Attendance.class);
        return new PageImpl<>(attendanceList, pageable, count);
    }

    public List<Attendance> getAttendance(LocalDate startDate, LocalDate endDate){
        Query query = new Query();
        query.addCriteria(Criteria.where("date")
                .gte(startDate.atStartOfDay())
                .lte(endDate.plusDays(1).atStartOfDay()));
        return mongoTemplate.find(query, Attendance.class);
    }

    public byte[] exportAttendanceCsv(LocalDate date){
        List<Employee> employees = employeeRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.println("Employee Name,"+ date);
        for(Employee employee :employees){
            Attendance attendance = attendanceRepository.findByEmployeeIdAndDate(employee.getId(), date );

            String status = "ABSENT";
            if(attendance !=null){
                status = attendance.getStatus().name();
            }
            writer.println(employee.getUsername()+","+ status);
        }
        writer.flush();
        return outputStream.toByteArray();
    }
}
