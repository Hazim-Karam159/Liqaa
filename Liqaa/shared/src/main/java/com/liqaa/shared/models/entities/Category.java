package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Category implements Serializable {
    private int id, userId;
    private String categoryName;
    private LocalDateTime createdAt;

    public Category(){}
    public Category(int userId, String categoryName) {
        this.userId = userId;
        this.categoryName = categoryName;
    }

    public Category(int id, int userId, String categoryName) {
        this.id = id;
        this.userId = userId;
        this.categoryName = categoryName;
    }

    public Category(int id, int userId, String categoryName, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.categoryName = categoryName;
        this.createdAt = createdAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", userId=" + userId +
                ", categoryName='" + categoryName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
