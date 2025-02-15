package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Group implements Serializable
{
    private int id;
    private String name;
    private byte[] image;
    private String description;
    private int createdBy;
    private LocalDateTime createdAt;

    public Group() {
    }

    public Group(int id, String name, byte[] image, String description, int createdBy, LocalDateTime createdAt)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Group(int id, String name, byte[] image, String description, int createdBy)
    {
        this.id = id;
        this.name = name;
        this.image = image;
        this.description = description;
        this.createdBy = createdBy;
    }

    public Group(String name, String description, int createdBy)
    {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", image='" + image + '\'' +
                ", description='" + description + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                '}';
    }

}
