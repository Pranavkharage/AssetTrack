package com.assettrack.controller;

import com.assettrack.entity.Employee;
import com.assettrack.entity.Role;
import com.assettrack.entity.User;
import com.assettrack.dto.Requests;
import com.assettrack.repository.EmployeeRepository;
import com.assettrack.repository.UserRepository;
import com.assettrack.repository.RoleRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeRepository employees;
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;

    public EmployeeController(EmployeeRepository e, UserRepository u, RoleRepository r, PasswordEncoder p) {
        employees=e; users=u; roles=r; encoder=p;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Object all() { return employees.findAll(); }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Employee create(@Valid @RequestBody Requests.EmployeeRequest r) {
        String email=r.email().trim().toLowerCase();
        if (employees.existsByEmail(email) || users.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        if (employees.existsByEmployeeCode(r.employeeCode()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Employee code already exists");
        if (r.accountPassword()==null || r.accountPassword().length()<12)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Employee login password must contain at least 12 characters");
        Employee e=new Employee(); map(e,r); e=employees.save(e);
        Role role=roles.findByName("EMPLOYEE").orElseThrow();
        User u=new User(); u.setFullName(e.getFullName()); u.setEmail(email);
        u.setPassword(encoder.encode(r.accountPassword())); u.setRole(role); u.setEmployee(e); u.setActive(true);
        users.save(u);
        return e;
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public Employee update(@PathVariable Long id,@Valid @RequestBody Requests.EmployeeRequest r) {
        Employee e=employees.findById(id).orElseThrow();
        String email=r.email().trim().toLowerCase();
        if (!e.getEmail().equalsIgnoreCase(email) &&
                (employees.existsByEmail(email) || users.existsByEmail(email)))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already exists");
        if (!e.getEmployeeCode().equals(r.employeeCode()) && employees.existsByEmployeeCode(r.employeeCode()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,"Employee code already exists");
        map(e,r);
        Employee saved=employees.save(e);
        users.findByEmployeeId(id).ifPresent(u->{u.setFullName(saved.getFullName());u.setEmail(saved.getEmail());users.save(u);});
        return saved;
    }

    private void map(Employee e,Requests.EmployeeRequest r) {
        e.setEmployeeCode(r.employeeCode()); e.setFullName(r.fullName()); e.setEmail(r.email().trim().toLowerCase());
        e.setDepartment(r.department()); e.setDesignation(r.designation()); e.setPhone(r.phone()); e.setJoiningDate(r.joiningDate());
    }
}
