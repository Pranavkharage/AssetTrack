package com.assettrack.entity;
import jakarta.persistence.*;import java.math.BigDecimal;import java.time.LocalDateTime;
@Entity @Table(name="maintenance_records")
public class MaintenanceRecord {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="asset_id") private Asset asset;
 @ManyToOne(optional=false) @JoinColumn(name="reported_by") private Employee reportedBy;
 @Column(nullable=false,columnDefinition="TEXT") private String issueDescription;
 private LocalDateTime reportedDate=LocalDateTime.now();
 @Enumerated(EnumType.STRING) private MaintenanceStatus status=MaintenanceStatus.PENDING;
 private BigDecimal repairCost; @Column(columnDefinition="TEXT") private String resolution; private LocalDateTime completedDate;
 public Long getId(){return id;} public Asset getAsset(){return asset;} public void setAsset(Asset v){asset=v;} public Employee getReportedBy(){return reportedBy;} public void setReportedBy(Employee v){reportedBy=v;}
 public String getIssueDescription(){return issueDescription;} public void setIssueDescription(String v){issueDescription=v;} public LocalDateTime getReportedDate(){return reportedDate;}
 public MaintenanceStatus getStatus(){return status;} public void setStatus(MaintenanceStatus v){status=v;} public BigDecimal getRepairCost(){return repairCost;} public void setRepairCost(BigDecimal v){repairCost=v;}
 public String getResolution(){return resolution;} public void setResolution(String v){resolution=v;} public LocalDateTime getCompletedDate(){return completedDate;} public void setCompletedDate(LocalDateTime v){completedDate=v;}
}
