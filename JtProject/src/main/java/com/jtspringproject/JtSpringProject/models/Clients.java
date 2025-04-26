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

    private String subjects; // JSON или строка с предметами

    @Enumerated(EnumType.STRING)
    private PreferredFormat preferredFormat; // Перечисление для формата

    private BigDecimal budget; // Бюджет клиента

    private String availableTimes; // JSON или строка с расписанием

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

    public String getSubjects() {
        return subjects;
    }

    public void setSubjects(String subjects) {
        this.subjects = subjects;
    }

    public PreferredFormat getPreferredFormat() {
        return preferredFormat;
    }

    public void setPreferredFormat(PreferredFormat preferredFormat) {
        this.preferredFormat = preferredFormat;
    }

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }

    public String getAvailableTimes() {
        return availableTimes;
    }

    public void setAvailableTimes(String availableTimes) {
        this.availableTimes = availableTimes;
    }

    public enum Level {
        BEGINNER,
        INTERMEDIATE,
        ADVANCED
    }

    public enum PreferredFormat {
        ONLINE,
        OFFLINE,
        HYBRID
    }
}
