package com.kredmint.loom.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    public Attendance save(@RequestBody Attendance attendance){
        return attendanceService.save(attendance);
    }

    @PutMapping("/login")
    public Attendance updateLoginTime(@RequestParam LocalTime loginTime, @RequestParam String employeeId, @RequestParam LocalDate date){
        return attendanceService.updateLoginTime(loginTime, employeeId, date);
    }

    @PutMapping("/logout")
    public Attendance updateLogoutTime(@RequestParam LocalTime logoutTime, @RequestParam String employeeId, @RequestParam LocalDate date){
        return attendanceService.updateLogoutTime(logoutTime, employeeId, date);
    }

    @GetMapping
    public Page<Attendance> getAttendance(@RequestParam String employeeId,
                                          @RequestParam(required = false) Attendance.Status status,
                                          @RequestParam(required = false) Attendance.Type type,
                                          @PageableDefault Pageable pageable) {

        return attendanceService.getAttendance(employeeId, status, type, pageable);
    }

    @GetMapping("/export")
    public Object getAttendance(@RequestParam LocalDate startDate,
                                @RequestParam LocalDate endDate) {

        return attendanceService.getAttendance(startDate, endDate);
    }

    @GetMapping("/export/csv")
    public ResponseEntity<byte[]> exportAttendanceCsv(@RequestParam LocalDate date){
        byte[] csvData = attendanceService.exportAttendanceCsv(date);

        return  ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename= attendance.csv").header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }
    }



