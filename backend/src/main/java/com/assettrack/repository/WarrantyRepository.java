package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;import com.assettrack.entity.*;
public interface WarrantyRepository extends JpaRepository<Warranty,Long> { List<Warranty> findByWarrantyEndBetween(java.time.LocalDate from, java.time.LocalDate to); }
