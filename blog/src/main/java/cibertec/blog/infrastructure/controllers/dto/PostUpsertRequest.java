package cibertec.blog.infrastructure.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class PostUpsertRequest {

    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 160, message = "El titulo no puede superar 160 caracteres")
    private String title;

    @NotBlank(message = "El resumen es obligatorio")
    @Size(max = 280, message = "El resumen no puede superar 280 caracteres")
    private String summary;

    @NotBlank(message = "El contenido es obligatorio")
    private String content;

    @NotNull(message = "La categoria es obligatoria")
    private Long categoryId;

    /**
     * Valores permitidos:
     * DRAFT | PUBLISHED
     * Opcional: si no se envia, el backend asigna DRAFT
     */
    private String status;

    // ======================
    // GETTERS & SETTERS
    // ======================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
