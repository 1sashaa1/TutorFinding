package com.jtspringproject.JtSpringProject.dto;

public class VideoDto {
    private String videoId;
    private String title;
    private String description;

    public VideoDto(String videoId, String title, String description) {
        this.videoId = videoId;
        this.title = title;
        this.description = description;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}