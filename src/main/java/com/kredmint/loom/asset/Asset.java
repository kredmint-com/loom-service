package com.kredmint.loom.asset;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Asset {
    private Long id;
    private String name;
    private String serialNumber;
    private String model;
    private String type;
    private Double value;
    
    @Builder.Default
    private AssetStatus status = AssetStatus.AVAILABLE;
}
