package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.ConversationParticipantsRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.ConversationRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.UserContactCategoryRepoImpl;
import com.liqaa.server.controllers.reposotories.interfaces.ConversationParticipantsRepo;
import com.liqaa.server.controllers.services.interfaces.ConversationServices;
import com.liqaa.server.controllers.reposotories.interfaces.ConversationRepo;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.ConversationType;
import com.liqaa.shared.models.enums.CurrentStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ConversationServicesImpl implements ConversationServices
{
    private static ConversationServicesImpl instance;
    private ConversationServicesImpl() {}

    public static synchronized ConversationServicesImpl getInstance()
    {
        if (instance == null)
        {
            instance = new ConversationServicesImpl();
        }
        return instance;
    }

    @Override
    public ChatInfo createDirectConversation(int userId, User otherUser) {
        int conversationId = 0;
        conversationId = ConversationRepoImpl.getInstance().createDirectConversation();
        ConversationParticipantsRepoImpl.getInstance().addParticipant(userId, conversationId);
        ConversationParticipantsRepoImpl.getInstance().addParticipant(otherUser.getId(), conversationId);

        ChatInfo chatInfo = new ChatInfo(conversationId, ConversationType.DIRECT, otherUser.getId(), otherUser.getDisplayName(),
                otherUser.isActive() ? otherUser.getCurrentstatus() : CurrentStatus.OFFLINE,
                otherUser.getProfilepicture(), 0, LocalDateTime.now(), LocalDateTime.now());

        return chatInfo;
    }

    @Override
    public ChatInfo getDirectConeversation(int userId, int recipientId)
    {
        if (userId == recipientId || userId < 1 || recipientId < 1)
        {
            System.err.println("Invalid recipient");
            throw new IllegalArgumentException("Invalid recipient");
        }

        return ConversationRepoImpl.getInstance().getDirectConversation(userId, recipientId);
    }

    @Override
    public List<ChatInfo> getAllConversations(int userId)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getAllConversations(userId);
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getAllConversations(int userId, int limit, int offset)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getAllConversations(userId, limit, offset);
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getGroupConversations(int userId)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getGroupConversations(userId);
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getGroupConversations(int userId, int limit, int offset)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getGroupConversations(userId, limit, offset);
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getUnreadConversations(int userId)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getUnreadConversations(userId);
        }
        return List.of();
    }

    @Override
    @Deprecated
    public List<ChatInfo> getUnreadConversations(int userId, int limit, int offset)
    {
        if (userId > 0)
        {
            return ConversationRepoImpl.getInstance().getUnreadConversations(userId, limit, offset);
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getDirectConversations(int userId, int categoryId) {
        if (userId <= 0 || categoryId <= 0) {
            System.err.println("Error in getDirectConversations: userId or categoryId is invalid");
            return List.of();
        }

        List<ChatInfo> chatInfos = new ArrayList<>();

        try {
            // Step 1: Get all users in the specified category
            List<Integer> categoryUsers = UserContactCategoryRepoImpl.getInstance().getCategoryUsers(categoryId);

            for (Integer contactId : categoryUsers) {
                // Step 2: Fetch the conversation details with this contact
                ChatInfo chatInfo = ConversationServicesImpl.getInstance().getDirectConeversation(userId, contactId);

                if (chatInfo != null) {
                    chatInfos.add(chatInfo);
                }
            }

            // Step 3: Sort by last message time in descending order
            chatInfos.sort(Comparator.comparing(ChatInfo::getLastMsgTime, Comparator.nullsLast(Comparator.reverseOrder())));

        } catch (SQLException e)
        {
            System.err.println("Error in getDirectConversations: " + e.getMessage());
            e.printStackTrace();
        }

        return chatInfos;
    }


    @Override
    public List<ChatInfo> getDirectConversations(int userId, int categoryId, int limit, int offset)
    {
        if (userId <= 0 || categoryId <= 0 || limit <= 0 || offset < 0)
        {
            System.err.println("Error in getDirectConversations: Invalid input parameters");
            return List.of();
        }

        List<ChatInfo> chatInfos = new ArrayList<>();

        try {
            // Step 1: Get all users in the specified category
            List<Integer> categoryUsers = UserContactCategoryRepoImpl.getInstance().getCategoryUsers(categoryId);

            for (Integer contactId : categoryUsers) {
                // Step 2: Fetch the conversation details with this contact
                ChatInfo chatInfo = ConversationServicesImpl.getInstance().getDirectConeversation(userId, contactId);

                if (chatInfo != null) {
                    chatInfos.add(chatInfo);
                }
            }

            // Step 3: Sort by last message time in descending order
            chatInfos.sort(Comparator.comparing(ChatInfo::getLastMsgTime, Comparator.nullsLast(Comparator.reverseOrder())));

            // Step 4: Apply pagination (subList ensures we return only the required portion)
            int fromIndex = Math.min(offset, chatInfos.size());
            int toIndex = Math.min(fromIndex + limit, chatInfos.size());

            return chatInfos.subList(fromIndex, toIndex);

        } catch (SQLException e) {
            System.err.println("Error in getDirectConversations: " + e.getMessage());
            e.printStackTrace();
        }

        return List.of();
    }

    @Override
    public void markConversationAsRead(int userId, int conversationId) {
        if (userId <= 0 || conversationId <= 0)
        {
            System.err.println("Error in markConversationAsRead: Invalid input parameters");
            return;
        }

        ConversationRepoImpl.getInstance().markConversationAsRead(userId, conversationId);
    }

    @Override
    public void updateMsgCount(int userId, int conversationId, int count)
    {
        if (userId <= 0 || conversationId <= 0 || count < 0)
            System.err.println("Error in updateMsgCount: Invalid input parameters");

        ConversationParticipantsRepoImpl.getInstance().updateUnreadMsgCount(userId, conversationId, count);
    }

    @Override
    public void updateLastMsgTime(int userId, int conversationId)
    {
        if (userId <= 0 || conversationId <= 0)
        {
            System.err.println("Error in updateLastMsgTime: Invalid input parameters");
            return;
        }

        ConversationRepoImpl.getInstance().updateLastMsgTime(userId, conversationId);
    }


    List<Integer> getCategoryUsers(int categoryId) throws SQLException
    {
        String query = "SELECT user_id FROM user_categories WHERE category_id = ?";
        PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query);
        statement.setInt(1, categoryId);
        ResultSet resultSet = statement.executeQuery();

        List<Integer> users = List.of();
        while (resultSet.next())
        {
            users.add(resultSet.getInt("user_id"));
        }

        return users;
    }
}
