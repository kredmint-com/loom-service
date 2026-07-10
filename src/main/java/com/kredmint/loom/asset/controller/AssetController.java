package com.kredmint.loom.asset.controller;

import com.kredmint.loom.asset.entity.Asset;
import com.kredmint.loom.asset.entity.AssetAssignmentRequest;
import com.kredmint.loom.asset.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @PostMapping
    public Asset registerAsset(@RequestBody Asset asset) {
        return assetService.registerAsset(asset);
    }

    @GetMapping("/{assetId}")
    public Asset getAssetById(@PathVariable String assetId) {
        return assetService.getAssetById(assetId);
    }

    @GetMapping
    public Page<Asset> getAssets(Pageable pageable) {
        return assetService.getAssets(pageable);
    }

    @GetMapping("/available")
    public Page<Asset> getAvailable(Pageable pageable) {
        return assetService.getAvailableAssets(pageable);
    }

    @GetMapping("/assigned/{employeeId}")
    public List<Asset> getAssignedAssets(@PathVariable String employeeId) {
        return assetService.getAssignedAssets(employeeId);
    }

    @PutMapping("/{assetId}")
    public Asset updateAsset(@PathVariable String assetId, @RequestBody Asset asset){
        return assetService.updateAsset(assetId, asset);
    }

    @PutMapping("/assign")
    public Asset assignAsset(@RequestBody AssetAssignmentRequest request){
        return assetService.assignAsset(request);
    }

    @PutMapping("/return")
    public Asset returnAsset(@RequestBody AssetAssignmentRequest request) {
        return assetService.returnAsset(request);
    }

    @PutMapping("/status/{assetId}")
    public Asset updateAssetStatus(
            @PathVariable String assetId,
            @RequestParam Asset.AssetStatus status,
            @RequestParam String updatedBy,
            @RequestParam String remarks) {

        return assetService.updateAssetStatus(
                assetId,
                status,
                updatedBy,
                remarks);
    }

    @DeleteMapping("/{assetId}")
    public void deleteAsset(@PathVariable String assetId) {
        assetService.deleteAsset(assetId);
    }
}

