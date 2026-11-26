package com.assettrack.entity;
import jakarta.persistence.*;import java.time.LocalDate;
@Entity @Table(name="warranties")
public class Warranty {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @OneToOne(optional=false) @JoinColumn(name="asset_id",unique=true) private Asset asset;
 private LocalDate warrantyStart; private LocalDate warrantyEnd; private String provider;
 public Long getId(){return id;} public Asset getAsset(){return asset;} public void setAsset(Asset v){asset=v;} public LocalDate getWarrantyStart(){return warrantyStart;} public void setWarrantyStart(LocalDate v){warrantyStart=v;}
 public LocalDate getWarrantyEnd(){return warrantyEnd;} public void setWarrantyEnd(LocalDate v){warrantyEnd=v;} public String getProvider(){return provider;} public void setProvider(String v){provider=v;}
}
