package com.jtspringproject.JtSpringProject.models;

import com.jtspringproject.JtSpringProject.dto.VideoDto;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tutors")
public class Tutors implements Serializable {

    @Id
    @Column(name = "tutor_id")
    private int id; // Первичный ключ

    @OneToOne
    @MapsId // Позволяет использовать id из User
    @JoinColumn(name = "tutor_id", referencedColumnName = "id", nullable = true)
    private User user;

    @ManyToOne
    @JoinColumn(name = "subject_id") // имя столбца в таблице tutors
    private Subject subject;

    @Column(name = "youtube_channel")
    private String youtubeChannelId;

    public String getYoutubeChannelId() {
        return youtubeChannelId;
    }

    public void setYoutubeChannelId(String youtubeChannelId) {
        this.youtubeChannelId = youtubeChannelId;
    }

    @Column(name = "hourly_rate")
    private BigDecimal rate; // Почасовая ставка
    private String experience; // Опыт работы

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    private String education; // Образование

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private PreferredFormat preferredFormat;

    @OneToMany(mappedBy = "tutor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScheduleSlot> schedule = new ArrayList<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();

    public double getAverageRating() {
        if (reviews.isEmpty()) {
            return 0;  // Или можешь вернуть какое-то значение по умолчанию, например, 0
        }
        double totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();  // Суммируем все рейтинги
        }
        return totalRating / reviews.size();  // Делим на количество отзывов, чтобы получить среднее
    }
    public int getReviewCount() {
        return reviews.size();  // Возвращаем количество отзывов
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    @Lob
    @Column(name = "photo", columnDefinition = "LONGBLOB")
    private byte[] photo;

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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public PreferredFormat getPreferredFormat() {
        return preferredFormat;
    }

    public void setPreferredFormat(PreferredFormat preferredFormat) {
        this.preferredFormat = preferredFormat;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public List<ScheduleSlot> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleSlot> schedule) {
        this.schedule = schedule;
    }

    @Transient
    private List<VideoDto> youtubeVideos;

    public List<VideoDto> getYoutubeVideos() {
        return youtubeVideos;
    }

    public void setYoutubeVideos(List<VideoDto> youtubeVideos) {
        this.youtubeVideos = youtubeVideos;
    }
}
