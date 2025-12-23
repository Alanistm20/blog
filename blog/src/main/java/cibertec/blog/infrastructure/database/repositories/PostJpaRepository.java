package cibertec.blog.infrastructure.database.repositories;

import cibertec.blog.infrastructure.database.entities.PostEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long> {

    @EntityGraph(attributePaths = {"category", "author"})
    List<PostEntity> findAll();

    @EntityGraph(attributePaths = {"category", "author"})
    List<PostEntity> findByCategory_Id(Long categoryId);

    @EntityGraph(attributePaths = {"category", "author"})
    Optional<PostEntity> findWithAllById(Long id);
}
