package com.homeservices.app.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class MessageResponse {
    private Long id;
    private String messageText;
    private String imagePath;
    private boolean isRead;
    private boolean isDeleted;
    private LocalDateTime sentAt;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
}
