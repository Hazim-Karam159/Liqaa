package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.AnnouncementRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.FileMessageDAOImpl;
import com.liqaa.server.controllers.reposotories.interfaces.FileMessageDAO;
import com.liqaa.server.controllers.reposotories.interfaces.MessageDAO;
import com.liqaa.server.controllers.reposotories.implementations.MessageDAOImpl;
import com.liqaa.server.controllers.services.interfaces.MessageService;
import com.liqaa.shared.models.entities.Announcement;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.MessageType;

import java.nio.file.Path;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class MessageServiceImpl implements MessageService
{

    private static final MessageServiceImpl INSTANCE = new MessageServiceImpl();
    private final MessageDAO messageDAO;

    private MessageServiceImpl() {
        this.messageDAO = new MessageDAOImpl();
    }

    public static MessageServiceImpl getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Message> getMessagesByConversationId(int conversationId) {
        return messageDAO.findByConversationId(conversationId);
    }
    @Override
    public void  createAnnouncement(Announcement announcement)
    {
        AnnouncementRepoImpl AnnouncementRepoImplobj=new AnnouncementRepoImpl();
        try{
            if (AnnouncementRepoImplobj.createAnnouncement(announcement))
            {
                System.out.println("Announcement created");
            }
            else
            {
                System.out.println("Announcement is not created");
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Message> getMessagesByConversationId(int conversationId, int offset, int limit) {
        return messageDAO.findByConversationId(conversationId, offset, limit);
    }

    @Override
    public int sendMessage(Message message) {
        if (message.getContent() == null || message.getContent().isEmpty()) {
            throw new IllegalArgumentException("Message content cannot be empty");
        }
        return messageDAO.save(message);
    }

    @Override
    public int getUnreadMessageCount(int conversationId, int userId) {
        List<Message> messages = messageDAO.findByConversationId(conversationId);
        return (int) messages.stream()
                .filter(msg -> msg.getSenderId() != userId && msg.getSeenAt() == null)
                .count();
    }

    @Override
    public void markMessagesAsSeen(int conversationId, int userId) {
        List<Message> messages = messageDAO.findByConversationId(conversationId);
        for (Message message : messages) {
            if (message.getSenderId() != userId && message.getSeenAt() == null) {
                message.setSeenAt(LocalDateTime.now());
                messageDAO.update(message);
            }
        }
    }

    @Override
    public FileMessage getFileInfo(int messageId) {
        FileMessageDAOImpl fileMessageDAO = new FileMessageDAOImpl();
        return fileMessageDAO.findById( messageId);
    }

    @Override
    public int sendFile(FileMessage fileInfo) {
        FileMessageDAOImpl fileMessageDAO = new FileMessageDAOImpl();
        return fileMessageDAO.save(fileInfo);
    }

    @Override
    public void deleteMessage(int messageId) {

    }

    @Override
    public Map<String, Integer> getMessagesPerDay()
    {
        Map<String, Integer> MsgNumberPerDay=messageDAO.getMessagesPerDay();
        return MsgNumberPerDay;

    }

    @Override
    public void updateFileInfo(int messageId, Path filePath) {
        FileMessageDAOImpl fileMessageDAO = new FileMessageDAOImpl();
        fileMessageDAO.update(messageId, filePath);
    }

}