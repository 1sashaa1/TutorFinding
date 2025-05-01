package com.jtspringproject.JtSpringProject.dto;

import java.time.LocalDateTime;

public class userDto {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime created_at;

    public userDto(Long id, String name, String email, LocalDateTime created_at) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    // Геттеры и сеттеры
}
