package com.assettrack.controller;

import com.assettrack.dto.Requests;
import com.assettrack.entity.*;
import com.assettrack.repository.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api/employee")
public class EmployeePortalController {
    private final UserRepository users;
    private final AssignmentRepository assignments;
    private final MaintenanceRepository maintenance;
    private final AssetRepository assets;

    public EmployeePortalController(UserRepository u, AssignmentRepository a, MaintenanceRepository m, AssetRepository as) {
        users=u; assignments=a; maintenance=m; assets=as;
    }

    private Employee employee(Authentication auth) {
        User user=users.findByEmail(auth.getName()).orElseThrow();
        if (user.getEmployee()==null) throw new ResponseStatusException(HttpStatus.FORBIDDEN,"Employee account is not linked to an employee record");
        return user.getEmployee();
    }

    @GetMapping("/profile")
    public Object profile(Authentication auth) { return employee(auth); }

    @GetMapping("/assignments")
    public Object myAssignments(Authentication auth) {
        return assignments.findByEmployeeIdOrderByAssignedDateDesc(employee(auth).getId());
    }

    @GetMapping("/maintenance")
    public Object myMaintenance(Authentication auth) {
        return maintenance.findByReportedByIdOrderByReportedDateDesc(employee(auth).getId());
    }

    @PostMapping("/maintenance")
    @Transactional
    public Object report(@Valid @RequestBody Requests.EmployeeMaintenanceRequest request, Authentication auth) {
        Employee emp=employee(auth);
        Asset asset=assets.findByIdForUpdate(request.assetId())
            .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Asset not found"));
        if (asset.getStatus()==AssetStatus.RETIRED)
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Retired assets cannot receive maintenance requests");
        if (maintenance.existsByAssetIdAndStatusNot(asset.getId(), MaintenanceStatus.COMPLETED))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"An open maintenance request already exists for this asset");
        boolean assignedToThisEmployee = assignments
            .findByEmployeeIdOrderByAssignedDateDesc(emp.getId()).stream()
            .anyMatch(a -> a.getAsset().getId().equals(asset.getId()) && a.getReturnDate()==null);
        if (!assignedToThisEmployee)
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"You can report issues only for assets currently assigned to you");
        // Keep ASSIGNED status while the employee still holds the asset.

        MaintenanceRecord m=new MaintenanceRecord();
        m.setAsset(asset); m.setReportedBy(emp); m.setIssueDescription(request.issueDescription());
        assets.save(asset);
        return maintenance.save(m);
    }
}
