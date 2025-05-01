package com.jtspringproject.JtSpringProject.dto;

import java.time.LocalDateTime;

public class messageDto {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String senderName;
    private String message;
    private LocalDateTime sentAt;

    // Конструкторы
    public messageDto() {}

    public messageDto(Long id, Long chatId, Long senderId, String senderName,
                      String message, LocalDateTime sentAt) {
        this.id = id;
        this.chatId = chatId;
        this.senderId = senderId;
        this.senderName = senderName;
        this.message = message;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}