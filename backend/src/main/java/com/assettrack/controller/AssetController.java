package com.assettrack.controller;
import com.assettrack.service.AssetService;import com.assettrack.dto.Requests;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import org.springframework.security.access.prepost.PreAuthorize;
@RestController @RequestMapping("/api/assets")
public class AssetController {
 private final AssetService service;public AssetController(AssetService s){service=s;}
 @GetMapping public Object all(){return service.all();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public Object create(@Valid @RequestBody Requests.AssetRequest r){return service.save(r,null);}
 @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Object update(@PathVariable Long id,@Valid @RequestBody Requests.AssetRequest r){return service.save(r,id);}
 @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public Object retire(@PathVariable Long id){return service.retire(id);}
}
