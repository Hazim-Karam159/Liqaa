package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Conversation;

import java.sql.SQLException;
import java.util.List;

public interface ConversationRepo
{
    public int createDirectConversation();

    public int createGroupConversation(int group_id);
    public Conversation getConversation(int conversation_id);

    public ChatInfo getDirectConversation(int userId, int recipientId);
    public Conversation getConversationByGroup(int groupId);

    public List<ChatInfo> getAllConversations(int userId);
    public List<ChatInfo> getAllConversations(int userId, int limit, int offset);

    public List<ChatInfo> getGroupConversations(int userId);
    public List<ChatInfo> getGroupConversations(int userId, int limit, int offset);

    public List<ChatInfo> getUnreadConversations(int userId);
    public List<ChatInfo> getUnreadConversations(int userId, int limit, int offset);

    public void deleteConversation(int conversation_id);

    void updateLastMsgTime(int userId, int conversationId);

    void markConversationAsRead(int userId, int conversationId);
}
