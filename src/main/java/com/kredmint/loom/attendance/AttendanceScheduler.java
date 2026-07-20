package com.kredmint.loom.attendance;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AttendanceScheduler {

    @Scheduled(cron = "* * * * * *")
    public void syncAttendance() {
        // todo : add new attendance entry each day.
        // todo : check if holiday or leave or wfh or weekend
        // todo : update attendance status
    }
}
