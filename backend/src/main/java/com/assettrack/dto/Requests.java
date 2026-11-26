package com.assettrack.dto;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.math.BigDecimal;
public class Requests {
 public record Login(@Email @NotBlank String email,@NotBlank String password){}
 public record AssetRequest(@NotBlank String assetCode,@NotBlank String name,@NotNull Long categoryId,String brand,String model,String serialNumber,LocalDate purchaseDate,BigDecimal purchasePrice,String vendor){}
 public record EmployeeRequest(@NotBlank String employeeCode,@NotBlank String fullName,@Email @NotBlank String email,String department,String designation,String phone,LocalDate joiningDate,String accountPassword){}
 public record AssignmentRequest(@NotNull Long assetId,@NotNull Long employeeId,String remarks){}
 public record ReturnRequest(@NotBlank String condition,String remarks){}
 public record WarrantyRequest(@NotNull Long assetId,LocalDate warrantyStart,@NotNull LocalDate warrantyEnd,String provider){}
 public record MaintenanceRequest(@NotNull Long assetId,@NotNull Long employeeId,@NotBlank String issueDescription){}
 public record EmployeeMaintenanceRequest(@NotNull Long assetId,@NotBlank String issueDescription){}
 public record MaintenanceUpdate(String status,BigDecimal repairCost,String resolution){}
}
