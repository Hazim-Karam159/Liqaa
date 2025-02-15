package com.liqaa.shared.models.entities;

import com.liqaa.shared.models.enums.NotificationType;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Notification implements Serializable {
    private int id, recipientId, senderId, AnnouncementId;
    private NotificationType type;
    private boolean isRead;
    private LocalDateTime sentAt;

    public Notification() {
    }

    public Notification(int recipientId, int senderId, NotificationType type, boolean isRead) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        this.type = type;
        this.isRead = isRead;
    }

    public Notification(int recipientId, int senderId, int announcementId, NotificationType type, boolean isRead) {
        this.recipientId = recipientId;
        this.senderId = senderId;
        AnnouncementId = announcementId;
        this.type = type;
        this.isRead = isRead;
    }

    public Notification(int id, int recipientId, int senderId, int announcementId, NotificationType type, boolean isRead, LocalDateTime sentAt) {
        this.id = id;
        this.recipientId = recipientId;
        this.senderId = senderId;
        AnnouncementId = announcementId;
        this.type = type;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getAnnouncementId() {
        return AnnouncementId;
    }

    public void setAnnouncementId(int announcementId) {
        AnnouncementId = announcementId;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    @Override
    public String toString() {
        return "Notification{" +
                "id=" + id +
                ", recipientId=" + recipientId +
                ", senderId=" + senderId +
                ", AnnouncementId=" + AnnouncementId +
                ", type=" + type +
                ", isRead=" + isRead +
                ", sentAt=" + sentAt +
                '}';
    }
}