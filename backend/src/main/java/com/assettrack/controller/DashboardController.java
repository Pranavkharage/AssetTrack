package com.assettrack.controller;
import com.assettrack.repository.*;import com.assettrack.entity.*;import org.springframework.web.bind.annotation.*;import java.time.LocalDate;import java.util.*;
@RestController @RequestMapping("/api/dashboard")
public class DashboardController {
 private final AssetRepository assets;private final EmployeeRepository employees;private final AssignmentRepository assignments;private final MaintenanceRepository maintenance;private final WarrantyRepository warranties;
 public DashboardController(AssetRepository a,EmployeeRepository e,AssignmentRepository as,MaintenanceRepository m,WarrantyRepository w){assets=a;employees=e;assignments=as;maintenance=m;warranties=w;}
 @GetMapping public Map<String,Object> stats(){var all=assets.findAll();Map<String,Long> status=new LinkedHashMap<>();for(AssetStatus s:AssetStatus.values())status.put(s.name(),all.stream().filter(a->a.getStatus()==s).count());return Map.of("totalAssets",all.size(),"totalEmployees",employees.count(),"activeAssignments",assignments.findByReturnDateIsNull().size(),"openMaintenance",maintenance.findAll().stream().filter(m->m.getStatus()!=MaintenanceStatus.COMPLETED).count(),"expiringWarranties",warranties.findByWarrantyEndBetween(LocalDate.now(),LocalDate.now().plusDays(30)).size(),"assetStatus",status);}
}
