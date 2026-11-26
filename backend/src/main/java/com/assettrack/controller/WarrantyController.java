package com.assettrack.controller;
import com.assettrack.dto.Requests;import com.assettrack.entity.*;import com.assettrack.repository.*;import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import org.springframework.security.access.prepost.PreAuthorize;import java.time.LocalDate;
@RestController @RequestMapping("/api/warranties")
public class WarrantyController {
 private final WarrantyRepository repo;private final AssetRepository assets;
 public WarrantyController(WarrantyRepository r,AssetRepository a){repo=r;assets=a;}
 @GetMapping public Object all(){return repo.findAll();}
 @GetMapping("/expiring") public Object expiring(){return repo.findByWarrantyEndBetween(LocalDate.now(),LocalDate.now().plusDays(30));}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public Warranty save(@Valid @RequestBody Requests.WarrantyRequest r){Warranty w=repo.findAll().stream().filter(x->x.getAsset().getId().equals(r.assetId())).findFirst().orElseGet(Warranty::new);w.setAsset(assets.findById(r.assetId()).orElseThrow());w.setWarrantyStart(r.warrantyStart());w.setWarrantyEnd(r.warrantyEnd());w.setProvider(r.provider());return repo.save(w);}
}
