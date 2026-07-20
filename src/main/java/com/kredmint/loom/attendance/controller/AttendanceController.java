
package com.kredmint.loom.attendance.controller;

import com.kredmint.loom.attendance.service.AttendanceService;
import com.kredmint.loom.attendance.entity.Attendance;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDate;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    public Attendance save(@RequestBody Attendance attendance){
        return attendanceService.save(attendance);
    }

    @PutMapping
    public Attendance updateAttendance(@RequestParam String employeeId,@RequestParam LocalDate date){
        return attendanceService.updateAttendance(employeeId, date);
    }

    @GetMapping
    public Page<Attendance> getAttendance(@RequestParam String employeeId,
                                          @RequestParam(required = false) Attendance.Status status,
                                          @RequestParam(required = false) Attendance.Type type,
                                          @PageableDefault Pageable pageable) {

        return attendanceService.getAttendance(employeeId, status, type, pageable);
    }
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportAttendanceCsv(@RequestParam LocalDate startDate,
                                                      @RequestParam LocalDate endDate){
        byte[] csvData = attendanceService.exportAttendanceCsv(startDate, endDate);

        return  ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename= attendance.csv")
                .header(HttpHeaders.CONTENT_TYPE, "text/csv")
                .body(csvData);
    }

    //manual
    @PostMapping("/upload/{employeeId}")
    public ResponseEntity<String> uploadAttendanceCsv(
            @PathVariable String employeeId,
            @RequestParam("file") MultipartFile file) {

        attendanceService.processAttendanceCsv(employeeId, file);

        return ResponseEntity.ok("Attendance uploaded successfully for Employee : " + employeeId);
    }


}