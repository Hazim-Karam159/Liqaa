package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AnnouncementNotification implements Serializable {
    private int id; // notification id
    private String title, content;
    private boolean isRead;
    private LocalDateTime sentAt;

    public AnnouncementNotification() {
    }

    public AnnouncementNotification(int id, String title, String content, boolean isRead, LocalDateTime sentAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.isRead = isRead;
        this.sentAt = sentAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
        return "AnnouncementNotification{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isRead=" + isRead +
                ", sentAt=" + sentAt +
                '}';
    }
}
