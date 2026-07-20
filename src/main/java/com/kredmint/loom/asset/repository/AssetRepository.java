package com.kredmint.loom.asset.repository;

import com.kredmint.loom.asset.entity.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AssetRepository extends MongoRepository<Asset, String> {

    Optional<Asset> findByAssetCodeAndDeletedFalse(String assetCode);

    Optional<Asset> findBySerialNumberAndDeletedFalse(String serialNumber);

    Page<Asset> findByDeletedFalse(Pageable pageable);

    Page<Asset> findByStatusAndDeletedFalse(
            Asset.AssetStatus status,
            Pageable pageable);

    List<Asset> findByAssignedEmployeeIdAndDeletedFalse(String employeeId);
}
