package com.assettrack.controller;

import com.assettrack.dto.Requests;
import com.assettrack.entity.*;
import com.assettrack.repository.*;
import com.assettrack.exception.ApiException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceController {
    private final MaintenanceRepository repo;
    private final AssetRepository assets;
    private final EmployeeRepository employees;
    private final AssignmentRepository assignments;

    public MaintenanceController(MaintenanceRepository r,AssetRepository a,EmployeeRepository e,AssignmentRepository as) {
        repo=r; assets=a; employees=e; assignments=as;
    }

    @GetMapping public Object all() { return repo.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Object create(@Valid @RequestBody Requests.MaintenanceRequest r) {
        Asset asset=assets.findByIdForUpdate(r.assetId())
            .orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));
        Employee emp=employees.findById(r.employeeId())
            .orElseThrow(()->new ApiException("Employee not found",HttpStatus.NOT_FOUND));
        if (asset.getStatus()==AssetStatus.RETIRED)
            throw new ApiException("Retired assets cannot enter maintenance",HttpStatus.CONFLICT);
        if (repo.existsByAssetIdAndStatusNot(asset.getId(),MaintenanceStatus.COMPLETED))
            throw new ApiException("An open maintenance record already exists for this asset",HttpStatus.CONFLICT);
        MaintenanceRecord record=new MaintenanceRecord();
        record.setAsset(asset); record.setReportedBy(emp); record.setIssueDescription(r.issueDescription());
        if (asset.getStatus()==AssetStatus.AVAILABLE) asset.setStatus(AssetStatus.UNDER_MAINTENANCE);
        assets.save(asset);
        return repo.save(record);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Object update(@PathVariable Long id,@RequestBody Requests.MaintenanceUpdate r) {
        MaintenanceRecord m=repo.findById(id)
            .orElseThrow(()->new ApiException("Maintenance record not found",HttpStatus.NOT_FOUND));
        Asset asset=assets.findByIdForUpdate(m.getAsset().getId())
            .orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));
        if (r.status()!=null) {
            MaintenanceStatus next;
            try { next=MaintenanceStatus.valueOf(r.status().toUpperCase()); }
            catch (IllegalArgumentException ex) { throw new ApiException("Invalid maintenance status",HttpStatus.BAD_REQUEST); }
            if (m.getStatus()==MaintenanceStatus.COMPLETED && next!=MaintenanceStatus.COMPLETED)
                throw new ApiException("Completed maintenance records cannot be reopened",HttpStatus.CONFLICT);
            m.setStatus(next);
        }
        if (r.repairCost()!=null && r.repairCost().signum()<0)
            throw new ApiException("Repair cost cannot be negative",HttpStatus.BAD_REQUEST);
        m.setRepairCost(r.repairCost()); m.setResolution(r.resolution());
        if (m.getStatus()==MaintenanceStatus.COMPLETED) {
            m.setCompletedDate(LocalDateTime.now());
            boolean activeAssignment=assignments.existsByAssetIdAndReturnDateIsNull(asset.getId());
            boolean otherOpen=repo.findAll().stream().anyMatch(x ->
                !x.getId().equals(m.getId()) && x.getAsset().getId().equals(asset.getId()) &&
                x.getStatus()!=MaintenanceStatus.COMPLETED);
            if (!activeAssignment && !otherOpen && asset.getStatus()!=AssetStatus.RETIRED)
                asset.setStatus(AssetStatus.AVAILABLE);
        } else if (!assignments.existsByAssetIdAndReturnDateIsNull(asset.getId()) &&
                asset.getStatus()!=AssetStatus.RETIRED) {
            asset.setStatus(AssetStatus.UNDER_MAINTENANCE);
        }
        assets.save(asset);
        return repo.save(m);
    }
}
