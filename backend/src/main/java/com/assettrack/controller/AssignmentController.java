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
import java.time.LocalDate;

@RestController
@RequestMapping("/api/assignments")
public class AssignmentController {
    private final AssignmentRepository assignments;
    private final AssetRepository assets;
    private final EmployeeRepository employees;
    private final MaintenanceRepository maintenance;

    public AssignmentController(AssignmentRepository a, AssetRepository as, EmployeeRepository e, MaintenanceRepository m) {
        assignments=a; assets=as; employees=e; maintenance=m;
    }

    @GetMapping public Object all() { return assignments.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Object assign(@Valid @RequestBody Requests.AssignmentRequest r) {
        Asset asset=assets.findByIdForUpdate(r.assetId())
            .orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));
        if (asset.getStatus()!=AssetStatus.AVAILABLE ||
            assignments.existsByAssetIdAndReturnDateIsNull(asset.getId()) ||
            maintenance.existsByAssetIdAndStatusNot(asset.getId(),MaintenanceStatus.COMPLETED)) {
            throw new ApiException("Only available assets without open maintenance can be assigned",HttpStatus.CONFLICT);
        }
        Employee emp=employees.findById(r.employeeId())
            .orElseThrow(()->new ApiException("Employee not found",HttpStatus.NOT_FOUND));
        if (!emp.isActive()) throw new ApiException("Inactive employees cannot receive assets",HttpStatus.CONFLICT);
        Assignment a=new Assignment(); a.setAsset(asset); a.setEmployee(emp);
        a.setAssignedDate(LocalDate.now()); a.setRemarks(r.remarks());
        asset.setStatus(AssetStatus.ASSIGNED); assets.save(asset);
        return assignments.save(a);
    }

    @PostMapping("/{id}/return")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Object giveBack(@PathVariable Long id,@Valid @RequestBody Requests.ReturnRequest r) {
        Assignment a=assignments.findByIdAndReturnDateIsNull(id)
            .orElseThrow(()->new ApiException("Active assignment not found",HttpStatus.NOT_FOUND));
        Asset asset=assets.findByIdForUpdate(a.getAsset().getId())
            .orElseThrow(()->new ApiException("Asset not found",HttpStatus.NOT_FOUND));
        a.setReturnDate(LocalDate.now()); a.setReturnCondition(r.condition()); a.setRemarks(r.remarks());
        boolean openMaintenance=maintenance.existsByAssetIdAndStatusNot(asset.getId(),MaintenanceStatus.COMPLETED);
        boolean damaged=r.condition().equalsIgnoreCase("DAMAGED");
        asset.setStatus(damaged||openMaintenance?AssetStatus.UNDER_MAINTENANCE:AssetStatus.AVAILABLE);
        assets.save(asset);
        return assignments.save(a);
    }
}
