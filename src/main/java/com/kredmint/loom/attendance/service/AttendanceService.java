package com.kredmint.loom.attendance.service;

import com.kredmint.loom.attendance.entity.Attendance;
import com.kredmint.loom.attendance.repository.AttendanceRepository;
import com.kredmint.loom.employee.entity.Employee;
import com.kredmint.loom.employee.service.EmployeeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class AttendanceService {

    @Autowired
    private final MongoTemplate mongoTemplate;

    private final AttendanceRepository attendanceRepository;

    private final EmployeeService employeeService;

    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    public Attendance updateAttendance(String employeeId, LocalDate date) {
        Attendance attendance = attendanceRepository
                .findByEmployeeIdAndDate(employeeId, date);
        if (attendance == null) {
            throw new RuntimeException("Attendance not found.");
        }
        LocalTime currentTime = LocalTime.now();
        if (attendance.getLoginTime() == null) {
            attendance.setLoginTime(currentTime);
            attendance.setStatus(Attendance.Status.PRESENT);
            return attendanceRepository.save(attendance);
        }
        if (attendance.getLogoutTime() == null) {
            long minutes = java.time.Duration
                    .between(attendance.getLoginTime(), currentTime).toMinutes();
            if (minutes < 3) {
                throw new RuntimeException("Attendance Already Marked");
            }
            attendance.setLogoutTime(currentTime);
            return attendanceRepository.save(attendance);
        }
        throw new RuntimeException("Attendance marked for today.");
    }


    public Page<Attendance> getAttendance(String employeeId,
                                          Attendance.Status status,
                                          Attendance.Type type,
                                          Pageable pageable) {
        Query query = new Query();
        query.addCriteria(Criteria.where("employeeId").is(employeeId));
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        long count = mongoTemplate.count(query, Attendance.class);
        query.with(pageable);
        List<Attendance> attendanceList = mongoTemplate.find(query, Attendance.class);
        return new PageImpl<>(attendanceList, pageable, count);
    }

    public byte[] exportAttendanceCsv(LocalDate startDate, LocalDate endDate) {

        List<Employee> employees = employeeService.getAllEmployees();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);
        writer.print("Sr No, Employee Code, Employee Name");
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(endDate)) {
            writer.print("," + currentDate);
            currentDate = currentDate.plusDays(1);
        }
        writer.println(", Leaves");
        int srNo = 1;

        for (Employee employee : employees) {

            writer.print(srNo++);
            writer.print("," + employee.getEmployeeCode());
            writer.print("," + employee.getUsername());
            int leaveCount = 0;
            currentDate = startDate;

            while (!currentDate.isAfter(endDate)) {

                Attendance attendance = attendanceRepository
                        .findByEmployeeIdAndDate(employee.getId(), currentDate);

                System.out.println("--------------------------------");
                System.out.println("Employee : " + employee.getId());
                System.out.println("Date     : " + currentDate);
                System.out.println("Result   : " + attendance);

                if (attendance != null) {

                    System.out.println("Writing -> " + attendance.getStatus());

                    writer.print("," + attendance.getStatus());

                    if (attendance.getStatus() == Attendance.Status.ABSENT) {
                        leaveCount++;
                    }

                } else {

                    System.out.println("Writing -> ABSENT");

                    writer.print(",ABSENT");
                    leaveCount++;
                }
                currentDate = currentDate.plusDays(1);
            }
            writer.println("," + leaveCount);
        }
        writer.flush();
        return outputStream.toByteArray();
    }

    //manual
    public void processAttendanceCsv(String employeeId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded CSV file cannot be empty.");
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MMM-yy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

        List<Attendance> attendanceList = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(
                        new BufferedInputStream(file.getInputStream())))) {

            bufferedReader.readLine();

            String temp;

            while ((temp = bufferedReader.readLine()) != null) {

                if (temp.trim().isEmpty()) {
                    continue;
                }

                String[] array = temp.split(",");

                if (array[0].contains("Total Duration")) {
                    break;
                }

                String date = array.length > 0 ? array[0].trim() : "";
                String inTime = array.length > 1 ? array[1].trim() : "";
                String outTime = array.length > 2 ? array[2].trim() : "";
                String status = array.length > 13 ? array[13].trim() : "";

                LocalDate attendanceDate = LocalDate.parse(date, dateFormatter);

                LocalTime loginTime = null;
                if (!inTime.isBlank()) {
                    loginTime = LocalTime.parse(inTime, timeFormatter);
                }

                LocalTime logoutTime = null;
                if (!outTime.isBlank()) {
                    logoutTime = LocalTime.parse(outTime, timeFormatter);
                }

                Attendance.Status attendanceStatus;
                Attendance.Type attendanceType;

                switch (status.trim().toUpperCase()) {

                    case "PRESENT":
                        attendanceStatus = Attendance.Status.PRESENT;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    case "ABSENT":
                        attendanceStatus = Attendance.Status.ABSENT;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    case "HALF DAY":
                        attendanceStatus = Attendance.Status.HALF_DAY;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    case "WORK FROM HOME":
                        attendanceStatus = Attendance.Status.WORK_FROM_HOME;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    case "WEEKLYOFF":
                        attendanceStatus = Attendance.Status.WEEKEND;
                        attendanceType = Attendance.Type.WEEKEND;
                        break;

                    case "WEEKLYOFF PRESENT":
                        attendanceStatus = Attendance.Status.PRESENT;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    case "ABSENT (NO OUTPUNCH)":
                        attendanceStatus = Attendance.Status.PRESENT;
                        attendanceType = Attendance.Type.WORKING;
                        break;

                    default:
                        attendanceStatus = Attendance.Status.ABSENT;
                        attendanceType = Attendance.Type.WORKING;
                }

                Attendance attendance = Attendance.builder()
                        .employeeId(employeeId)
                        .date(attendanceDate)
                        .loginTime(loginTime)
                        .logoutTime(logoutTime)
                        .status(attendanceStatus)
                        .type(attendanceType)
                        .build();

                attendanceList.add(attendance);
            }

            attendanceRepository.saveAll(attendanceList);

        } catch (IOException e) {
            throw new RuntimeException("Error while processing attendance CSV", e);
        }
    }
    }
