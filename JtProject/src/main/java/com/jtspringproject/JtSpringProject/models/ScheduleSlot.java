package com.jtspringproject.JtSpringProject.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "schedule_slot")
public class ScheduleSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private LocalTime start_time;
    private LocalTime end_time;

    private boolean available = true; // true – свободен, false – занят

    @ManyToOne
    @JoinColumn(name = "tutor_id")
    private Tutors tutor;

    // при занятии можно связать с учеником
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Clients client;

    @OneToMany(mappedBy = "scheduleSlot", cascade = CascadeType.ALL)
    private List<Lesson> lessons;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return start_time;
    }

    public void setStartTime(LocalTime start_time) {
        this.start_time = start_time;
    }

    public LocalTime getEndTime() {
        return end_time;
    }

    public void setEndTime(LocalTime end_time) {
        this.end_time = end_time;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Tutors getTutor() {
        return tutor;
    }

    public void setTutor(Tutors tutor) {
        this.tutor = tutor;
    }

    public Clients getClient() {
        return client;
    }

    public void setClient(Clients client) {
        this.client = client;
    }
    @Override
    public String toString() {
        return "ScheduleSlot{" +
                "date='" + date + '\'' +
                ", startTime=" + start_time +
                ", endTime=" + end_time +
                '}';
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
