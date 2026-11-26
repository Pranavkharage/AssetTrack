package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.assettrack.entity.*;
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
 Optional<Employee> findByEmail(String email);
 boolean existsByEmail(String email);
 boolean existsByEmployeeCode(String employeeCode);
}
