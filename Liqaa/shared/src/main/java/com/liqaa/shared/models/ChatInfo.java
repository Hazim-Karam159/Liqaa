package com.liqaa.shared.models;

import com.liqaa.shared.models.enums.ConversationType;
import com.liqaa.shared.models.enums.CurrentStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatInfo implements Serializable
{
    private int conversationId;
    private ConversationType type;
    private int recipientId;     // may be group id or user id.
    private String name;
    private CurrentStatus status;
    private byte[] image;
    private int unreadMsgCount;
    private LocalDateTime lastMsgTime;
    private LocalDateTime createdAt;

    public ChatInfo(int conversationId, ConversationType type, int recipientId, String name, CurrentStatus status, byte[] image, int unreadMsgCount, LocalDateTime lastMsgTime, LocalDateTime createdAt) {
        this.conversationId = conversationId;
        this.type = type;
        this.recipientId = recipientId;
        this.name = name;
        this.status = status;
        this.image = image;
        this.unreadMsgCount = unreadMsgCount;
        this.lastMsgTime = lastMsgTime;
        this.createdAt = createdAt;
    }

    public int getConversationId() {
        return conversationId;
    }

    public void setConversationId(int conversationId) {
        this.conversationId = conversationId;
    }

    public ConversationType getType() {
        return type;
    }

    public void setType(ConversationType type) {
        this.type = type;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CurrentStatus getStatus() {
        return status;
    }

    public void setStatus(CurrentStatus status) {
        this.status = status;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getUnreadMsgCount() {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount) {
        this.unreadMsgCount = unreadMsgCount;
    }

    public LocalDateTime getLastMsgTime() {
        return lastMsgTime;
    }

    public void setLastMsgTime(LocalDateTime lastMsgTime) {
        this.lastMsgTime = lastMsgTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ChatInfo{" +
                "conversationId=" + conversationId +
                ", type=" + type +
                ", recipientId=" + recipientId +
                ", name='" + name + '\'' +
                ", status=" + status +
                ", image=" + image +
                ", unreadMsgCount=" + unreadMsgCount +
                ", lastMsgTime=" + lastMsgTime +
                ", createdAt=" + createdAt +
                '}';
    }
}