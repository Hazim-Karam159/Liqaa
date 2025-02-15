package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.Message;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MessageDAO {
    int save(Message message);
    Optional<Message> findById(int id);
    List<Message> findAll();
    List<Message> findByConversationId(int conversationId, int offset, int limit); // New method for pagination
    List<Message> findByConversationId(int conversationId); // New method without pagination
    void update(Message message);
    void delete(int id);
    public Map<String, Integer> getMessagesPerDay();
}