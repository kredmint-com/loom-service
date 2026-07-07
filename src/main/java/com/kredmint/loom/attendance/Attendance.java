package com.kredmint.loom.attendance;

import com.kredmint.loom.employee.entity.Employee;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {
    private String employeeId;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private Status status;
    private Type type;
    private Date date;

    public enum Status {
        ABSENT, PRESENT, HALF_DAY, WORK_FROM_HOME
    }

    public enum Type {
       WORKING, HOLIDAY, WEEKEND
    }
}
