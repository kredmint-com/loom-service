package com.kredmint.loom.document;

import com.kredmint.loom.employee.entity.Employee;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {
    private Long id;
    private String name;
    private String fileUrl;
    private DocumentType documentType;
    
    @Builder.Default
    private String status = "PENDING";
    
    private LocalDateTime uploadedAt;
    private Employee employee;
}
