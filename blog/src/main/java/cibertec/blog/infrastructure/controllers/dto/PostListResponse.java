package cibertec.blog.infrastructure.controllers.dto;

import java.time.LocalDateTime;

public record PostListResponse(
        Long id,
        String title,
        String summary,
        String content,
        String status,
        LocalDateTime publishedAt,
        String author,
        Long categoryId,
        String category
) { }
