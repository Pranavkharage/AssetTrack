package com.assettrack.entity;
import jakarta.persistence.*;import java.time.LocalDate;
@Entity @Table(name="assignments")
public class Assignment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="asset_id") private Asset asset;
 @ManyToOne(optional=false) @JoinColumn(name="employee_id") private Employee employee;
 private LocalDate assignedDate; private LocalDate returnDate; private String returnCondition; @Column(columnDefinition="TEXT") private String remarks;
 public Long getId(){return id;} public Asset getAsset(){return asset;} public void setAsset(Asset v){asset=v;} public Employee getEmployee(){return employee;} public void setEmployee(Employee v){employee=v;}
 public LocalDate getAssignedDate(){return assignedDate;} public void setAssignedDate(LocalDate v){assignedDate=v;} public LocalDate getReturnDate(){return returnDate;} public void setReturnDate(LocalDate v){returnDate=v;}
 public String getReturnCondition(){return returnCondition;} public void setReturnCondition(String v){returnCondition=v;} public String getRemarks(){return remarks;} public void setRemarks(String v){remarks=v;}
}
