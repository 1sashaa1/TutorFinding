package com.jtspringproject.JtSpringProject.services;

import com.jtspringproject.JtSpringProject.dao.chatDao;
import com.jtspringproject.JtSpringProject.dao.messageDao;
import com.jtspringproject.JtSpringProject.dto.chatDto;
import com.jtspringproject.JtSpringProject.dto.messageDto;
import com.jtspringproject.JtSpringProject.models.Chat;
import com.jtspringproject.JtSpringProject.models.Message;
import com.jtspringproject.JtSpringProject.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class chatService {

    @Autowired
    private chatDao chatDao;

    @Autowired
    private final messageDao messageDao;

    private final userService userService;

    @Autowired
    public chatService(chatDao chatDao, messageDao messageDao, com.jtspringproject.JtSpringProject.services.userService userService) {
        this.chatDao = chatDao;
        this.messageDao = messageDao;
        this.userService = userService;
    }

    @Transactional
    public chatDto getOrCreateChat(Long userId, Long teacherId) {
        Optional<Chat> existingChat = chatDao.findChatBetweenUsers(userId, teacherId);

        if (existingChat.isPresent()) {
            return convertToDto(existingChat.get());
        } else {
            Chat newChat = new Chat();
            newChat.setUserId(userId);
            newChat.setTeacherId(teacherId);
            newChat.setCreatedAt(LocalDateTime.now());
            Long chatId = chatDao.save(newChat);
            newChat.setId(chatId);
            return convertToDto(newChat);
        }
    }

    @Transactional
    public List<messageDto> getMessages(Long chatId) {
        List<Message> messages = messageDao.findByChatId(chatId);
        return messages.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public messageDto sendMessage(Long chatId, Long senderId, String messageText) {
        Chat chat = chatDao.findById(chatId);
        if (chat == null) {
            throw new RuntimeException("Chat not found");
        }

        Message message = new Message();
        message.setChat(chat);
        message.setSenderId(senderId);
        message.setMessage(messageText);
        message.setSentAt(LocalDateTime.now());

        Long messageId = messageDao.save(message);
        message.setId(messageId);

        return convertToDto(message);
    }

    private chatDto convertToDto(Chat chat) {
        return new chatDto(
                chat.getId(),
                chat.getUserId(),
                chat.getTeacherId(),
                chat.getCreatedAt()
        );
    }

    private messageDto convertToDto(Message message) {
        User user = userService.getUserById(message.getSenderId().intValue());
        String senderName = user.getName();
        return new messageDto(
                message.getId(),
                message.getChat().getId(),
                message.getSenderId(),
                senderName,
                message.getMessage(),
                message.getSentAt()
        );
    }
}
