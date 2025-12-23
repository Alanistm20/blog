package cibertec.blog.infrastructure.database.repositories;

import cibertec.blog.infrastructure.database.entities.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, Long> {
}
