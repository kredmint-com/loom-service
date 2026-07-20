package com.kredmint.loom.attendance;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public Attendance save(Attendance attendance) {
        // todo: add create new attendance
        return null;
    }

    public Attendance updateLoginTime(LocalTime loginTime, String employeeId, Date date) {
        // todo: updateLoginTime
        return null;
    }

    public Attendance updateLogoutTime(LocalTime logoutTime, String employeeId, Date date) {
        // todo: updateLogoutTime
        return null;
    }

    public Object getAttendance(Date startDate, Date endDate) {
        // todo: csv return attendance
        return null;
    }

    public Page<Attendance> getAttendance(String query, String employeeId, Attendance.Status status, Attendance.Type type) {
        // todo: return attendance
        return null;
    }
}
