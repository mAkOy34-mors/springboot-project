package com.homeservices.app.controller;

import com.homeservices.app.dto.request.MessageRequest;
import com.homeservices.app.dto.response.ApiResponse;
import com.homeservices.app.dto.response.MessageResponse;
import com.homeservices.app.service.impl.FileStorageService;
import com.homeservices.app.service.impl.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final FileStorageService fileStorageService;

    // POST /api/v1/messages/send  (text only)
    @PostMapping("/send")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessage(
            @Valid @RequestBody MessageRequest request) {
        return ResponseEntity.ok(ApiResponse.ok("Message sent",
                messageService.sendMessage(request, null)));
    }

    // POST /api/v1/messages/send-with-image  (multipart)
    @PostMapping("/send-with-image")
    public ResponseEntity<ApiResponse<MessageResponse>> sendMessageWithImage(
            @RequestParam("senderId") Long senderId,
            @RequestParam("receiverId") Long receiverId,
            @RequestParam(value = "messageText", required = false) String messageText,
            @RequestParam("image") MultipartFile image) {

        String fileName = fileStorageService.storeFile(image);
        String imageUrl = "http://localhost:8080/api/files/download/" + fileName;

        MessageRequest request = new MessageRequest();
        request.setSenderId(senderId);
        request.setReceiverId(receiverId);
        request.setMessageText(messageText);

        return ResponseEntity.ok(ApiResponse.ok("Message sent",
                messageService.sendMessage(request, imageUrl)));
    }

    // GET /api/v1/messages/conversation?user1=1&user2=2
    @GetMapping("/conversation")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getConversation(
            @RequestParam Long user1,
            @RequestParam Long user2) {
        return ResponseEntity.ok(ApiResponse.ok("Conversation fetched",
                messageService.getConversation(user1, user2)));
    }

    // GET /api/v1/messages/user/{userId}
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<MessageResponse>>> getAllMessages(
            @PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Messages fetched",
                messageService.getAllMessagesByUser(userId)));
    }

    // PATCH /api/v1/messages/{id}/read
    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable Long id) {
        messageService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.ok("Message marked as read"));
    }

    // DELETE /api/v1/messages/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok(ApiResponse.ok("Message deleted"));
    }

    // GET /api/v1/messages/unread/{userId}
    @GetMapping("/unread/{userId}")
    public ResponseEntity<ApiResponse<Long>> countUnread(@PathVariable Long userId) {
        return ResponseEntity.ok(ApiResponse.ok("Unread count",
                messageService.countUnread(userId)));
    }
}
