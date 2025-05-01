package com.jtspringproject.JtSpringProject.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class userpnDto {
    @NotBlank(message = "Имя обязательно")
    private String name;

    @Email(message = "Некорректный email")
    private String email;

    public @NotBlank(message = "Имя обязательно") String getName() {
        return name;
    }

    public void setName(@NotBlank(message = "Имя обязательно") String name) {
        this.name = name;
    }

    public @Email(message = "Некорректный email") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Некорректный email") String email) {
        this.email = email;
    }
}
