package com.jtspringproject.JtSpringProject.controller;
import com.jtspringproject.JtSpringProject.dto.chatDto;
import com.jtspringproject.JtSpringProject.dto.messageDto;
import com.jtspringproject.JtSpringProject.models.Chat;
import com.jtspringproject.JtSpringProject.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jtspringproject.JtSpringProject.services.chatService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final chatService chatService;

    @Autowired
    public ChatController(chatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/getChatId/{userId}/{teacherId}")
    public ResponseEntity<Long> getChatId(
            @PathVariable Long userId,
            @PathVariable Long teacherId) {

        chatDto chat = chatService.getOrCreateChat(userId, teacherId);
        return ResponseEntity.ok(chat.getId());
    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<List<messageDto>> getMessages(
            @PathVariable Long chatId) {

        return ResponseEntity.ok(chatService.getMessages(chatId));
    }

    @PostMapping("/send")
    public ResponseEntity<messageDto> sendMessage(
            @RequestBody SendMessageRequest request) {

        messageDto message = chatService.sendMessage(
                request.getChatId(),
                request.getSenderId(),
                request.getMessage()
        );
        return ResponseEntity.ok(message);
    }

    public static class SendMessageRequest {
        private Long chatId;
        private Long senderId;
        private String message;

        // Геттеры и сеттеры
        public Long getChatId() { return chatId; }
        public void setChatId(Long chatId) { this.chatId = chatId; }

        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}