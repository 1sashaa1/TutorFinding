package com.jtspringproject.JtSpringProject.models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "clients")
public class Clients implements Serializable {
    @Id
    @Column(name = "client_id")
    private int id; // Первичный ключ

    @OneToOne
    @MapsId // Позволяет использовать id из User
    @JoinColumn(name = "client_id", referencedColumnName = "id", nullable = false)
    private User user;

    private Integer age;

    @Enumerated(EnumType.STRING)
    private Level level; // Перечисление для уровней

    @ManyToOne
    @JoinColumn(name = "subjects") // имя столбца в таблице tutors
    private Subject subject;

    @Enumerated(EnumType.STRING)
    private PreferredFormat preferred_format; // Перечисление для формата

    private BigDecimal budget; // Бюджет клиента

    private String available_times; // JSON или строка с расписанием

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public void setPreferred_format(PreferredFormat preferred_format) {
        this.preferred_format = preferred_format;
    }

    public String getAvailable_times() {
        return available_times;
    }

    public void setAvailable_times(String available_times) {
        this.available_times = available_times;
    }

    public PreferredFormat getPreferred_format() {
        return preferred_format;
    }

    public void setPreferredFormat(PreferredFormat preferredFormat) {
        this.preferred_format = preferredFormat;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getAvailableTimes() {
        return available_times;
    }

    public void setAvailableTimes(String availableTimes) {
        this.available_times = availableTimes;
    }

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public enum PreferredFormat {
        ONLINE,
        OFFLINE,
        BOTH
    }
}
