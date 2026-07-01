package com.kredmint.loom.asset;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Document("asset")
public class Asset {

    @Id
    private String id;
    private String assetCode;
    private String assetName;
    private AssetType assetType;
    private String brand;
    private String model;
    private String serialNumber;

    private LocalDate purchaseDate;
    private BigDecimal purchasePrice;
    private String vendorName;
    private String vendorId;
    private LocalDate warrantyExpiryDate;

    private AssetStatus status;
    private AssetCondition condition;

    private String assignedEmployeeId;
    private String assignedEmployeeName;
    private String assignedDepartment;
    private LocalDate assignedDate;

    private String description;
    private String remarks;

//    private boolean deleted;

    public enum AssetType {
        LAPTOP,
        DESKTOP,
        MONITOR,
        MOBILE_PHONE,
        TABLET,
        KEYBOARD,
        MOUSE,
        HEADSET,
        PRINTER,
        ID_CARD,
        ACCESS_CARD,
        SIM_CARD,
        VEHICLE,
        FURNITURE,
        SOFTWARE_LICENSE,
        OTHER
    }

    public enum AssetStatus {
        AVAILABLE,
        ASSIGNED,
        RETURNED,
        UNDER_MAINTENANCE,
        LOST,
        DAMAGED,
        RETIRED,
        DISPOSED
    }

    public enum AssetCondition {
        NEW,
        GOOD,
        FAIR,
        DAMAGED,
        NEEDS_REPAIR
    }
}
