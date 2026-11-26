package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.assettrack.entity.*;
public interface MaintenanceRepository extends JpaRepository<MaintenanceRecord,Long> {
 List<MaintenanceRecord> findByStatus(MaintenanceStatus status);
 List<MaintenanceRecord> findByReportedByIdOrderByReportedDateDesc(Long employeeId);
 boolean existsByAssetIdAndStatusNot(Long assetId, MaintenanceStatus status);
}
