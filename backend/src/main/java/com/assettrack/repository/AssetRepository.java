package com.assettrack.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
import com.assettrack.entity.*;
public interface AssetRepository extends JpaRepository<Asset,Long> {
 Optional<Asset> findByAssetCode(String code);
 boolean existsBySerialNumber(String serial);
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @org.springframework.data.jpa.repository.Query("select a from Asset a where a.id = :id")
 Optional<Asset> findByIdForUpdate(@Param("id") Long id);
}
