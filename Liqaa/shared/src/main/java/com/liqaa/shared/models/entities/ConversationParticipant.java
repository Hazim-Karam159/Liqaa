package com.liqaa.shared.models.entities;

import com.liqaa.shared.models.enums.ConversationType;
import java.time.LocalDateTime;

public class ConversationParticipant
{
    private int conversationId;
    private int userId;
    private LocalDateTime joinedAt;
    private int unreadMsgCount;
    private LocalDateTime lastMsgTime;

    public ConversationParticipant(int conversationId, int userId, LocalDateTime joinedAt, int unreadMsgCount, LocalDateTime lastMsgTime)
    {
        this.conversationId = conversationId;
        this.userId = userId;
        this.joinedAt = joinedAt;
        this.unreadMsgCount = unreadMsgCount;
        this.lastMsgTime = lastMsgTime;
    }

    public int getConversationId()
    {
        return conversationId;
    }

    public void setConversationId(int conversationId)
    {
        this.conversationId = conversationId;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public LocalDateTime getJoinedAt()
    {
        return joinedAt;
    }

    public void setJoinedAt(LocalDateTime joinedAt)
    {
        this.joinedAt = joinedAt;
    }

    public int getUnreadMsgCount()
    {
        return unreadMsgCount;
    }

    public void setUnreadMsgCount(int unreadMsgCount)
    {
        this.unreadMsgCount = unreadMsgCount;
    }

    public LocalDateTime getLastMsgTime()
    {
        return lastMsgTime;
    }

    public void setLastMsgTime(LocalDateTime lastMsgTime)
    {
        this.lastMsgTime = lastMsgTime;
    }

    @Override
    public String toString()
    {
        return "ConversationParticipant{" + "conversationId=" + conversationId + ", userId=" + userId + ", joinedAt=" + joinedAt + ", unreadMsgCount=" + unreadMsgCount + ", lastMsgTime=" + lastMsgTime + '}';
    }
}