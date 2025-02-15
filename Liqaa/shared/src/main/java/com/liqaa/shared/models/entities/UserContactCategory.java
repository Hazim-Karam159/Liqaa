package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class UserContactCategory implements Serializable {
    private int userId, categoryId;
    private LocalDateTime createdAt;

    public UserContactCategory() {
    }

    public UserContactCategory(int userId, int categoryId) {
        this.userId = userId;
        this.categoryId = categoryId;
    }

    public UserContactCategory(int userId, int categoryId, LocalDateTime createdAt) {
        this.userId = userId;
        this.categoryId = categoryId;
        this.createdAt = createdAt;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getUserId() {
        return userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "UserContactCategory{" +
                "userId=" + userId +
                ", categoryId=" + categoryId +
                ", createdAt=" + createdAt +
                '}';
    }
}