package com.kredmint.loom.attendance;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.time.LocalTime;

@Document("attendance")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    private String id;
    private String employeeId;
    private LocalTime loginTime;
    private LocalTime logoutTime;
    private Status status;
    private Type type;
    private LocalDate date;

    public enum Status {
        ABSENT, PRESENT, HALF_DAY, WORK_FROM_HOME
    }

    public enum Type {
       WORKING, HOLIDAY, WEEKEND
    }
}
