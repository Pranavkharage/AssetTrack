package com.assettrack.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDate;import java.time.LocalDateTime;
@Entity @Table(name="assets")
public class Asset {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=30) private String assetCode;
 @Column(nullable=false,length=120) private String name;
 private String brand; private String model;
 @Column(unique=true,length=100) private String serialNumber;
 @ManyToOne(optional=false) @JoinColumn(name="category_id") private Category category;
 private LocalDate purchaseDate; private BigDecimal purchasePrice; private String vendor;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=30) private AssetStatus status=AssetStatus.AVAILABLE;
 private LocalDateTime createdAt=LocalDateTime.now();
 public Long getId(){return id;} public String getAssetCode(){return assetCode;} public void setAssetCode(String v){assetCode=v;} public String getName(){return name;} public void setName(String v){name=v;}
 public String getBrand(){return brand;} public void setBrand(String v){brand=v;} public String getModel(){return model;} public void setModel(String v){model=v;}
 public String getSerialNumber(){return serialNumber;} public void setSerialNumber(String v){serialNumber=v;} public Category getCategory(){return category;} public void setCategory(Category v){category=v;}
 public LocalDate getPurchaseDate(){return purchaseDate;} public void setPurchaseDate(LocalDate v){purchaseDate=v;} public BigDecimal getPurchasePrice(){return purchasePrice;} public void setPurchasePrice(BigDecimal v){purchasePrice=v;}
 public String getVendor(){return vendor;} public void setVendor(String v){vendor=v;} public AssetStatus getStatus(){return status;} public void setStatus(AssetStatus v){status=v;}
 public LocalDateTime getCreatedAt(){return createdAt;}
}
