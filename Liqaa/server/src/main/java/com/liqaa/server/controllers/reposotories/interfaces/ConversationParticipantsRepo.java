package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.ConversationParticipant;

import java.util.List;

public interface ConversationParticipantsRepo
{
  public boolean addParticipant(int userId, int conversationId);
  public boolean addParticipants(List<Integer> userIds, int conversationId);

//  public boolean removeParticipant(int userId, int conversationId);
  public boolean removeParticipants(List<Integer> userIds, int conversationId);

  public ConversationParticipant getInfo(int userId, int conversationId);

  public List<ConversationParticipant> getParticipants(int conversationId);
  public List<ConversationParticipant> getConversations(int userId);

  public boolean updateConversationInfo(int userId, int conversationId, int unreadMsgCount, String lastMsgTime);
  public boolean updateUnreadMsgCount(int userId, int conversationId, int unreadMsgCount);
  public boolean updateLastMsgTime(int userId, int conversationId, String lastMsgTime);
}
