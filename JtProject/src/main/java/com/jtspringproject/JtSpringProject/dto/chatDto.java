package com.jtspringproject.JtSpringProject.dto;

import java.time.LocalDateTime;

public class chatDto {
    private Long id;
    private Long userId;
    private Long teacherId;
    private LocalDateTime createdAt;

    // Конструкторы, геттеры и сеттеры
    public chatDto() {}

    public chatDto(Long id, Long userId, Long teacherId, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.teacherId = teacherId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}