package com.liqaa.server.controllers.services.interfaces;

import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.User;

import java.sql.SQLException;
import java.util.List;

public interface ConversationServices
{
    public ChatInfo createDirectConversation(int userId, User otherUserId);

    public ChatInfo getDirectConeversation(int userId, int recipientId);

    public List<ChatInfo> getAllConversations(int userId);
    public List<ChatInfo> getAllConversations(int userId, int limit, int offset);

    public List<ChatInfo> getGroupConversations(int userId);
    public List<ChatInfo> getGroupConversations(int userId, int limit, int offset);

    public List<ChatInfo> getUnreadConversations(int userId);
    public List<ChatInfo> getUnreadConversations(int userId, int limit, int offset);


    public List<ChatInfo> getDirectConversations(int userId, int categoryId);
    public List<ChatInfo> getDirectConversations(int userId, int categoryId, int limit, int offset);

    public void markConversationAsRead(int userId, int conversationId);
    public void updateMsgCount(int userId, int conversationId, int count);
    public void updateLastMsgTime(int userId, int conversationId);

}


