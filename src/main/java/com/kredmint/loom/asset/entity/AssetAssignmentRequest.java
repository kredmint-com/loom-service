package com.kredmint.loom.asset.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssetAssignmentRequest {
    private String assetId;
    private String employeeId;
    private String employeeName;
    private String department;
    private String remarks;
}
