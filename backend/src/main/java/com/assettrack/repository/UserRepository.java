package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.assettrack.entity.*;
public interface UserRepository extends JpaRepository<User,Long> {
 Optional<User> findByEmail(String email);
 boolean existsByEmail(String email);
 Optional<User> findByEmployeeId(Long employeeId);
}
