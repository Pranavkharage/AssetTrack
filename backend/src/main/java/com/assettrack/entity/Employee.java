package com.assettrack.entity;
import jakarta.persistence.*;import java.time.LocalDate;
@Entity @Table(name="employees")
public class Employee {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=30) private String employeeCode;
 @Column(nullable=false,length=100) private String fullName;
 @Column(nullable=false,unique=true,length=150) private String email;
 private String department; private String designation; private String phone; private LocalDate joiningDate; private boolean active=true;
 public Long getId(){return id;} public String getEmployeeCode(){return employeeCode;} public void setEmployeeCode(String v){employeeCode=v;}
 public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;} public String getEmail(){return email;} public void setEmail(String v){email=v;}
 public String getDepartment(){return department;} public void setDepartment(String v){department=v;} public String getDesignation(){return designation;} public void setDesignation(String v){designation=v;}
 public String getPhone(){return phone;} public void setPhone(String v){phone=v;} public LocalDate getJoiningDate(){return joiningDate;} public void setJoiningDate(LocalDate v){joiningDate=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
