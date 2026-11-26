package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;import java.util.*;import com.assettrack.entity.*;
public interface CategoryRepository extends JpaRepository<Category,Long> { Optional<Category> findByName(String name); }
