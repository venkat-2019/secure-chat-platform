package com.chatapp.api.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private boolean delivered;
    private boolean read;
    private boolean toxic;
    private LocalDateTime createdAt;

    // Constructors
    public MessageDTO() {}

    public MessageDTO(Long id, Long senderId, Long receiverId, String content,
                     boolean delivered, boolean read, boolean toxic, LocalDateTime createdAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.delivered = delivered;
        this.read = read;
        this.toxic = toxic;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getSenderId() { return senderId; }
    public void setSenderId(Long senderId) { this.senderId = senderId; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public boolean isDelivered() { return delivered; }
    public void setDelivered(boolean delivered) { this.delivered = delivered; }

    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }

    public boolean isToxic() { return toxic; }
    public void setToxic(boolean toxic) { this.toxic = toxic; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

