package com.assettrack.entity;
import jakarta.persistence.*;
@Entity @Table(name="users")
public class User {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=100) private String fullName;
 @Column(nullable=false,unique=true,length=150) private String email;
 @Column(nullable=false) private String password;
 @ManyToOne(optional=false) @JoinColumn(name="role_id") private Role role;
 @OneToOne @JoinColumn(name="employee_id",unique=true) private Employee employee;
 private boolean active=true;
 public Long getId(){return id;} public String getFullName(){return fullName;} public void setFullName(String v){fullName=v;}
 public String getEmail(){return email;} public void setEmail(String v){email=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;}
 public Role getRole(){return role;} public void setRole(Role v){role=v;} public Employee getEmployee(){return employee;} public void setEmployee(Employee v){employee=v;}
 public boolean isActive(){return active;} public void setActive(boolean v){active=v;}
}
