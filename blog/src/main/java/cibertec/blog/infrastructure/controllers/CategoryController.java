package cibertec.blog.infrastructure.controllers;

import cibertec.blog.infrastructure.database.entities.CategoryEntity;
import cibertec.blog.infrastructure.database.repositories.CategoryJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {

    private final CategoryJpaRepository categoryRepo;

    public CategoryController(CategoryJpaRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @GetMapping
    public List<CategoryEntity> list() {
        return categoryRepo.findAll();
    }
}
