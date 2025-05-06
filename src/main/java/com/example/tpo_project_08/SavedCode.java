package com.example.tpo_project_08;

import java.io.Serializable;
import java.time.LocalDateTime;

public class SavedCode implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String originalCode;
    private String formattedCode;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    public SavedCode(String id, String originalCode, String formattedCode, LocalDateTime expiresAt) {
        this.id = id;
        this.originalCode = originalCode;
        this.formattedCode = formattedCode;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }

    public String getId() {
        return id;
    }

    public String getOriginalCode() {
        return originalCode;
    }

    public String getFormattedCode() {
        return formattedCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
}
