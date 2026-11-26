package com.assettrack.controller;
import com.assettrack.entity.Category;import com.assettrack.repository.CategoryRepository;import org.springframework.web.bind.annotation.*;import org.springframework.security.access.prepost.PreAuthorize;
@RestController @RequestMapping("/api/categories")
public class CategoryController {
 private final CategoryRepository repo;public CategoryController(CategoryRepository r){repo=r;}
 @GetMapping public Object all(){return repo.findAll();}
 @PostMapping @PreAuthorize("hasRole('ADMIN')") public Category create(@RequestBody Category c){c.setName(c.getName().trim());return repo.save(c);}
}
