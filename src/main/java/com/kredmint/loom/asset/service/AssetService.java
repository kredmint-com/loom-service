package com.kredmint.loom.asset.service;

import com.kredmint.loom.asset.entity.Asset;
import com.kredmint.loom.asset.entity.AssetAssignmentRequest;
import com.kredmint.loom.asset.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.kredmint.loom.asset.entity.AssetAssignmentRequest;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    public Asset registerAsset(Asset asset) {
        if (assetRepository.findByAssetCodeAndDeletedFalse(asset.getAssetCode()).isPresent()) {
            throw new RuntimeException("Asset code already exists!");
        }
        if (assetRepository.findBySerialNumberAndDeletedFalse(asset.getSerialNumber()).isPresent()) {
            throw new RuntimeException("Serial number already exists!");
        }
        asset.setStatus(Asset.AssetStatus.AVAILABLE);
        asset.setDeleted(false);
        return assetRepository.save(asset);
    }

    public Asset getAssetById(String assetId) {
        Asset asset = assetRepository.findById(assetId).orElse(null);
        if (asset == null || asset.isDeleted()) {
            throw new RuntimeException("Asset not found!");
        }
        return asset;
    }

    public Page<Asset> getAssets(Pageable pageable) {
       return assetRepository.findByDeletedFalse(pageable);
    }

    public Page<Asset> getAvailableAssets(Pageable pageable) {
        return assetRepository.findByStatusAndDeletedFalse(Asset.AssetStatus.AVAILABLE, pageable);
    }

    public List<Asset> getAssignedAssets(String employeeId) {
        return assetRepository.findByAssignedEmployeeIdAndDeletedFalse(employeeId);
    }

    public Asset updateAsset(String assetId, Asset asset) {
        Asset existingAsset = getAssetById(assetId);

        existingAsset.setBrand(asset.getBrand());
        existingAsset.setModel(asset.getModel());
        existingAsset.setVendorName(asset.getVendorName());
        existingAsset.setVendorId(asset.getVendorId());
        existingAsset.setWarrantyExpiryDate(asset.getWarrantyExpiryDate());
        existingAsset.setCondition(asset.getCondition());
        existingAsset.setDescription(asset.getDescription());
        existingAsset.setRemarks(asset.getRemarks());

        return assetRepository.save(existingAsset);
    }

    public Asset assignAsset(AssetAssignmentRequest request) {
        Asset asset = getAssetById(request.getAssetId());

        if (asset.getStatus() != Asset.AssetStatus.AVAILABLE) {
            throw new RuntimeException("Asset is not available!");
        }
        asset.setAssignedEmployeeId(request.getEmployeeId());
        asset.setAssignedEmployeeName(request.getEmployeeName());
        asset.setAssignedDepartment(request.getDepartment());
        asset.setAssignedDate(LocalDate.now());
        asset.setStatus(Asset.AssetStatus.ASSIGNED);

        return assetRepository.save(asset);
    }

    public Asset returnAsset(AssetAssignmentRequest request) {
        Asset asset = getAssetById(request.getAssetId());

        if(asset.getStatus() != Asset.AssetStatus.ASSIGNED){
            throw new RuntimeException("Asset is not assigned!");
        }
        if (!request.getEmployeeId().equals(asset.getAssignedEmployeeId())) {
            throw new RuntimeException("Asset is not assigned to this employee!");
        }
        asset.setAssignedEmployeeId(null);
        asset.setAssignedEmployeeName(null);
        asset.setAssignedDepartment(null);
        asset.setAssignedDate(null);

        asset.setStatus(Asset.AssetStatus.AVAILABLE);
        asset.setRemarks(request.getRemarks());

        return assetRepository.save(asset);
    }



    public Asset updateAssetStatus(
            String assetId,
            Asset.AssetStatus status,
            String updatedBy,
            String remarks) {

        Asset asset = getAssetById(assetId);
        asset.setStatus(status);
        asset.setRemarks(remarks);

        return assetRepository.save(asset);
    }

    public void deleteAsset(String assetId) {
        Asset asset = getAssetById(assetId);

        if (asset.getStatus() == Asset.AssetStatus.ASSIGNED) {
            throw new RuntimeException("Assigned asset cannot be deleted!");
        }
        asset.setDeleted(true);
        assetRepository.save(asset);
    }
}
