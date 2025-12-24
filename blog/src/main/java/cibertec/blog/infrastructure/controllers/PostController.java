package cibertec.blog.infrastructure.controllers;

import cibertec.blog.domain.model.PostStatus;
import cibertec.blog.infrastructure.controllers.dto.PostListResponse;
import cibertec.blog.infrastructure.controllers.dto.PostUpsertRequest;
import cibertec.blog.infrastructure.database.entities.CategoryEntity;
import cibertec.blog.infrastructure.database.entities.PostEntity;
import cibertec.blog.infrastructure.database.entities.UserEntity;
import cibertec.blog.infrastructure.database.repositories.CategoryJpaRepository;
import cibertec.blog.infrastructure.database.repositories.PostJpaRepository;
import cibertec.blog.infrastructure.database.repositories.UserJpaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/posts")
@CrossOrigin(origins = "http://localhost:4200")
public class PostController {

    private final PostJpaRepository postRepo;
    private final CategoryJpaRepository categoryRepo;
    private final UserJpaRepository userRepo;

    public PostController(PostJpaRepository postRepo,
                          CategoryJpaRepository categoryRepo,
                          UserJpaRepository userRepo) {
        this.postRepo = postRepo;
        this.categoryRepo = categoryRepo;
        this.userRepo = userRepo;
    }

    // =====================
    // LISTAR
    // =====================
    @GetMapping
    public List<PostListResponse> list(@RequestParam(required = false) Long categoryId) {
        List<PostEntity> posts = (categoryId == null)
                ? postRepo.findAll()
                : postRepo.findByCategory_Id(categoryId);

        return posts.stream().map(this::toListResponse).toList();
    }

    // =====================
    // OBTENER UNO
    // =====================
    @GetMapping("/{id}")
    public PostListResponse getOne(@PathVariable Long id) {
        PostEntity p = postRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no existe"));
        return toListResponse(p);
    }

    // =====================
    // CREAR
    // =====================
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public PostListResponse create(@Valid @RequestBody PostUpsertRequest req) {

        CategoryEntity cat = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria invalida"));

        UserEntity author = getLoggedUser();

        PostEntity p = new PostEntity();
        p.setTitle(req.getTitle());
        p.setSummary(req.getSummary());
        p.setContent(req.getContent());
        p.setCategory(cat);
        p.setAuthor(author);

        PostStatus status = parseStatus(req.getStatus());
        p.setStatus(status);

        if (status == PostStatus.PUBLISHED) {
            p.setPublishedAt(LocalDateTime.now());
        } else {
            p.setPublishedAt(null);
        }

        PostEntity saved = postRepo.save(p);
        return toListResponse(saved);
    }

    // =====================
    // ACTUALIZAR
    // =====================
    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public PostListResponse update(@PathVariable Long id, @Valid @RequestBody PostUpsertRequest req) {

        PostEntity p = postRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no existe"));

        CategoryEntity cat = categoryRepo.findById(req.getCategoryId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria invalida"));

        p.setTitle(req.getTitle());
        p.setSummary(req.getSummary());
        p.setContent(req.getContent());
        p.setCategory(cat);

        PostStatus status = parseStatus(req.getStatus());
        p.setStatus(status);

        if (status == PostStatus.PUBLISHED && p.getPublishedAt() == null) {
            p.setPublishedAt(LocalDateTime.now());
        }

        PostEntity saved = postRepo.save(p);
        return toListResponse(saved);
    }

    // =====================
    // ELIMINAR
    // =====================
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    public void delete(@PathVariable Long id) {
        if (!postRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post no existe");
        }
        postRepo.deleteById(id);
    }

    // =====================
    // HELPERS
    // =====================
    private PostStatus parseStatus(String raw) {
        if (raw == null || raw.isBlank()) return PostStatus.DRAFT;
        return PostStatus.valueOf(raw.trim().toUpperCase());
    }

    private UserEntity getLoggedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getName() == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
        }

        String username = auth.getName();

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no existe"));
    }

    private PostListResponse toListResponse(PostEntity p) {

        String author = (p.getAuthor() != null)
                ? p.getAuthor().getUsername()
                : null;

        Long catId = (p.getCategory() != null)
                ? p.getCategory().getId()
                : null;

        String catName = (p.getCategory() != null)
                ? p.getCategory().getName()
                : null;

        return new PostListResponse(
                p.getId(),
                p.getTitle(),
                p.getSummary(),
                p.getContent(),
                p.getStatus() != null ? p.getStatus().name() : null,
                p.getPublishedAt(),
                author,
                catId,
                catName
        );
    }
}
