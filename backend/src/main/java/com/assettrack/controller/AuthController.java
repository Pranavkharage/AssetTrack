package com.assettrack.controller;

import com.assettrack.dto.Requests;
import com.assettrack.repository.UserRepository;
import com.assettrack.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.security.authentication.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager auth;
    private final UserRepository users;
    private final JwtService jwt;

    public AuthController(AuthenticationManager a, UserRepository u, JwtService j) {
        auth=a; users=u; jwt=j;
    }

    @PostMapping("/login")
    public Map<String,Object> login(@Valid @RequestBody Requests.Login r) {
        auth.authenticate(new UsernamePasswordAuthenticationToken(r.email().trim().toLowerCase(), r.password()));
        var u=users.findByEmail(r.email().trim().toLowerCase()).orElseThrow();
        Map<String,Object> user = new LinkedHashMap<>();
        user.put("id",u.getId()); user.put("name",u.getFullName()); user.put("email",u.getEmail());
        user.put("role",u.getRole().getName());
        user.put("employeeId",u.getEmployee()==null?null:u.getEmployee().getId());
        return Map.of("token",jwt.generate(u.getEmail(),u.getRole().getName()),"user",user);
    }

    @GetMapping("/me")
    public Map<String,Object> me(org.springframework.security.core.Authentication a) {
        var u=users.findByEmail(a.getName()).orElseThrow();
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("id",u.getId()); result.put("name",u.getFullName()); result.put("email",u.getEmail());
        result.put("role",u.getRole().getName());
        result.put("employeeId",u.getEmployee()==null?null:u.getEmployee().getId());
        return result;
    }
}
