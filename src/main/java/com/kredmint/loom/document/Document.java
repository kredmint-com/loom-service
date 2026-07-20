package com.kredmint.loom.document;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@org.springframework.data.mongodb.core.mapping.Document(collection = "documents")
public class Document {

    @Id
    private String id;

    private String createdBy;

    private String url;

    private String emplyoeeId;

    private String name;

    private DocumentType documentType;

    private Date urlExpiryDate;

    private String lenderId;

    private String comment;

    private Status status;

    private String docPassword;

    private LocalDateTime uploadedAt;

    public enum Status{
        ACTIVE, INACTIVE, EXPIRED
    }

    public enum DocumentType {
        AADHAAR,
        PAN,
        PASSPORT,
        NATIONAL_ID,
        RESUME,
        OTHER
    }
}


