package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;import com.assettrack.entity.*;
public interface RoleRepository extends JpaRepository<Role,Long> { Optional<Role> findByName(String name); }
