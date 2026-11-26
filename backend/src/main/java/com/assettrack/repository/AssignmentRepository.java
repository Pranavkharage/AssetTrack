package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.assettrack.entity.*;
public interface AssignmentRepository extends JpaRepository<Assignment,Long> {
 List<Assignment> findByReturnDateIsNull();
 Optional<Assignment> findByIdAndReturnDateIsNull(Long id);
 boolean existsByAssetIdAndReturnDateIsNull(Long assetId);
 List<Assignment> findByEmployeeIdOrderByAssignedDateDesc(Long employeeId);
}
