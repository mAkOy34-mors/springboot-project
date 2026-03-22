package com.homeservices.app.service.impl;

import com.homeservices.app.dto.request.MessageRequest;
import com.homeservices.app.dto.response.MessageResponse;
import com.homeservices.app.entity.Message;
import com.homeservices.app.entity.UserTable;
import com.homeservices.app.repository.MessageRepository;
import com.homeservices.app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponse sendMessage(MessageRequest request, String imagePath) {
        UserTable sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found: " + request.getSenderId()));
        UserTable receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found: " + request.getReceiverId()));

        Message message = Message.builder()
                .messageText(request.getMessageText())
                .imagePath(imagePath)
                .sender(sender)
                .receiver(receiver)
                .isRead(false)
                .isDeleted(false)
                .build();
        messageRepository.save(message);
        return toResponse(message);
    }

    public List<MessageResponse> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<MessageResponse> getAllMessagesByUser(Long userId) {
        return messageRepository.findAllMessagesByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));
        message.setRead(true);
        messageRepository.save(message);
    }

    @Transactional
    public void deleteMessage(Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found: " + messageId));
        message.setDeleted(true);
        message.setDeletedAt(LocalDateTime.now());
        messageRepository.save(message);
    }

    public long countUnread(Long userId) {
        return messageRepository.countByReceiverIdAndIsReadFalse(userId);
    }

    // ── Mapper ─────────────────────────────────────────────────
    private MessageResponse toResponse(Message m) {
        return MessageResponse.builder()
                .id(m.getId())
                .messageText(m.getMessageText())
                .imagePath(m.getImagePath())
                .isRead(m.isRead())
                .isDeleted(m.isDeleted())
                .sentAt(m.getSentAt())
                .senderId(m.getSender() != null ? m.getSender().getId() : null)
                .senderName(m.getSender() != null ? m.getSender().getUsername() : null)
                .receiverId(m.getReceiver() != null ? m.getReceiver().getId() : null)
                .receiverName(m.getReceiver() != null ? m.getReceiver().getUsername() : null)
                .build();
    }
}
