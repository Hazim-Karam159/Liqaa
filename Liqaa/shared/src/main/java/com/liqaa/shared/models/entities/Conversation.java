package com.liqaa.shared.models.entities;

import com.liqaa.shared.models.enums.ConversationType;

import java.time.LocalDateTime;

public class Conversation
{
    private int id;
    private ConversationType type;
    private int groupId;
    private LocalDateTime createdAt;

    public Conversation(int id, ConversationType type, int groupId, LocalDateTime createdAt)
    {
        this.id = id;
        this.type = type;
        this.groupId = groupId;
        this.createdAt = createdAt;
    }

    public int getId()
    {
        return id;
    }

    public ConversationType getType()
    {
        return type;
    }

    public int getGroupId()
    {
        return groupId;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public void setType(ConversationType type)
    {
        this.type = type;
    }

    public void setGroupId(int groupId)
    {
        this.groupId = groupId;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    @Override
    public String toString()
    {
        return "Conversation{" +
                "id=" + id +
                ", type=" + type +
                ", groupId=" + groupId +
                ", createdAt=" + createdAt +
                '}';
    }
}

