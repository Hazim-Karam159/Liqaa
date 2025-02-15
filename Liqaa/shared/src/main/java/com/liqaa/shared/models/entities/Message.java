package com.liqaa.shared.models.entities;

import com.liqaa.shared.models.enums.MessageType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private int id;
    private int senderId;
    private int conversationId;
    private String content;
    private MessageType type;
    private LocalDateTime sentAt;
    private boolean isSent;
    private LocalDateTime receivedAt;
    private LocalDateTime seenAt;

    public Message() {}

    public Message(int id, int senderId, int conversationId, String content, MessageType type, LocalDateTime sentAt, boolean isSent, LocalDateTime receivedAt, LocalDateTime seenAt) {
        this.id = id;
        this.senderId = senderId;
        this.conversationId = conversationId;
        this.content = content;
        this.type = type;
        this.sentAt = sentAt;
        this.isSent = isSent;
        this.receivedAt = receivedAt;
        this.seenAt = seenAt;
    }

    public Message(int senderId, int conversationId, String content, MessageType type, LocalDateTime sentAt, boolean isSent, LocalDateTime receivedAt, LocalDateTime seenAt) {
        this(0, senderId, conversationId, content, type, sentAt, isSent, receivedAt, seenAt); // Assume id is auto-generated
    }

    public Message(int currentUserId, int currentConversationId, String content, MessageType messageType) {
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public LocalDateTime getSeenAt() {
        return seenAt;
    }

    public void setSeenAt(LocalDateTime seenAt) {
        this.seenAt = seenAt;
    }

    // Override toString()
    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", senderId=" + senderId +
                ", conversationId=" + conversationId +
                ", content='" + content + '\'' +
                ", type=" + type +
                ", sentAt=" + sentAt +
                ", isSent=" + isSent +
                ", receivedAt=" + receivedAt +
                ", seenAt=" + seenAt +
                '}';
    }
}