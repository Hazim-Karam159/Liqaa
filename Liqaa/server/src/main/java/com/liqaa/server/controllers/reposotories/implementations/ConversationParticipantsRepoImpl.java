package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.ConversationParticipantsRepo;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.ConversationParticipant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ConversationParticipantsRepoImpl implements ConversationParticipantsRepo
{
    private static ConversationParticipantsRepo instance;
    private ConversationParticipantsRepoImpl() {}

    public static synchronized ConversationParticipantsRepo getInstance() {
        if (instance == null) {
            instance = new ConversationParticipantsRepoImpl();
        }
        return instance;
    }

    @Override
    public boolean addParticipant(int userId, int conversationId) {
        if (userId == 0 || conversationId == 0) {
            System.err.println("Invalid user id or conversation id");
            return false;
        }
        String query = "INSERT INTO conversationparticipants (user_id, conversation_id, last_msg_time, unread_msg_count) VALUES (?, ?, NOW(), 0)";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, conversationId);
            if (stmt.executeUpdate() == 0) {
                System.err.println("Failed to add participant");
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean addParticipants(List<Integer> userIds, int conversationId) {
        if (userIds == null || userIds.isEmpty() || conversationId == 0) {
            System.err.println("Invalid user ids or conversation id");
            return false;
        }
        String query = "INSERT INTO conversationparticipants (user_id, conversation_id) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            for (Integer userId : userIds) {
                if (userId == 0) {
                    System.err.println("Invalid user id");
                    continue;
                }
                stmt.setInt(1, userId);
                stmt.setInt(2, conversationId);
                stmt.addBatch();
            }
            int[] results = stmt.executeBatch();
            for (int result : results) {
                if (result == 0) {
                    System.err.println("Failed to add some participants");
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean removeParticipants(List<Integer> userIds, int conversationId) {
        if (userIds == null || userIds.isEmpty() || conversationId == 0) {
            System.err.println("Invalid user ids or conversation id");
            return false;
        }

        StringBuilder sb = new StringBuilder("DELETE FROM conversationparticipants WHERE conversation_id = ? AND user_id IN (");
        for (int i = 0; i < userIds.size(); i++) {
            sb.append("?");
            if (i < userIds.size() - 1) {
                sb.append(",");
            }
        }
        sb.append(")");
        String query = sb.toString();
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, conversationId);
            for (int i = 0; i < userIds.size(); i++) {
                stmt.setInt(i + 2, userIds.get(i));
            }

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected == 0) {
                System.err.println("Failed to remove participants");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to remove participants: " + e.getMessage());
            return false;
        }
    }



    @Override
    public ConversationParticipant getInfo(int userId, int conversationId)
    {
        if(userId == 0 || conversationId == 0)
        {
            System.err.println("Invalid user id or conversation id");
            return null;
        }
        String query = "SELECT * FROM conversationparticipants WHERE user_id = ? AND conversation_id = ?";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, conversationId);
            ResultSet resultSet = statement.executeQuery();
            if(resultSet.next())
            {
                return new ConversationParticipant(resultSet.getInt("conversation_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("joined_at").toLocalDateTime(),
                        resultSet.getInt("unread_msg_count"),
                        resultSet.getTimestamp("last_msg_time")!=null?
                        resultSet.getTimestamp("last_msg_time").toLocalDateTime():null);
            }
            else
            {
                System.err.println("Failed to get participant info");
                return null;
            }

        } catch (SQLException e)
        {
           System.err.println("Failed to get participant info" + e.getMessage());
            return null;
        }
    }

    @Override
    public List<ConversationParticipant> getParticipants(int conversationId)
    {
        if (conversationId == 0)
        {
            System.err.println("Invalid conversation id");
            return List.of();
        }
        String query = "SELECT * FROM conversationparticipants WHERE conversation_id = ?";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
            statement.setInt(1, conversationId);
            ResultSet resultSet = statement.executeQuery();
            List<ConversationParticipant> participants = new ArrayList<>();
            while (resultSet.next())
            {
                participants.add(new ConversationParticipant(
                        resultSet.getInt("conversation_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("joined_at").toLocalDateTime(),
                        resultSet.getInt("unread_msg_count"),
                        resultSet.getTimestamp("last_msg_time") != null ?
                        resultSet.getTimestamp("last_msg_time").toLocalDateTime() : null
                ));
            }
            return participants;
        } catch (SQLException e)
        {
            System.err.println("Failed to get participants: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public List<ConversationParticipant> getConversations(int userId)
    {
        if (userId <= 0)
        {
            System.err.println("Invalid user id");
            return List.of();
        }

        String query = "SELECT * FROM conversationparticipants WHERE user_id = ?";

        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<ConversationParticipant> conversations = new ArrayList<>();
            while (resultSet.next())
            {
                conversations.add(new ConversationParticipant(
                        resultSet.getInt("conversation_id"),
                        resultSet.getInt("user_id"),
                        resultSet.getTimestamp("joined_at").toLocalDateTime(),
                        resultSet.getInt("unread_msg_count"),
                        resultSet.getTimestamp("last_msg_time") != null ?
                        resultSet.getTimestamp("last_msg_time").toLocalDateTime() : null
                ));
            }
            return conversations;
        } catch (SQLException e)
        {
            System.err.println("Failed to get conversations: " + e.getMessage());
            return List.of();
        }
    }

    @Override
    public boolean updateConversationInfo(int userId, int conversationId, int unreadMsgCount, String lastMsgTime)
    {
        if(userId<=0 || conversationId<=0)
        {
            System.err.println("Invalid user id or conversation id");
            return false;
        }
        String query = "UPDATE conversationparticipants SET unread_msg_count = ?, last_msg_time = ? WHERE user_id = ? AND conversation_id = ?";
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query))
        {
            stmt.setInt(1, unreadMsgCount);
            stmt.setString(2, lastMsgTime);
            stmt.setInt(3, userId);
            stmt.setInt(4, conversationId);
            if(stmt.executeUpdate()==0)
            {
                System.err.println("Failed to update conversation info");
                return false;
            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("Failed to update conversation info");
            return false;
        }
    }

    @Override
    public boolean updateUnreadMsgCount(int userId, int conversationId, int unreadMsgCount) {
        if (userId <= 0 || conversationId <= 0) {
            System.err.println("Invalid user id or conversation id");
            return false;
        }
        String query = "UPDATE conversationparticipants SET unread_msg_count = ? WHERE user_id = ? AND conversation_id = ?";
        try (PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query)) {
            stmt.setInt(1, unreadMsgCount);
            stmt.setInt(2, userId);
            stmt.setInt(3, conversationId);
            if (stmt.executeUpdate() == 0) {
                System.err.println("Failed to update unread message count");
                return false;
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to update unread message count");
            return false;
        }
    }

    @Override
    public boolean updateLastMsgTime(int userId, int conversationId, String lastMsgTime)
    {
        if(userId <= 0 || conversationId <= 0)
        {
            System.err.println("Invalid user id or conversation id");
            return false;
        }
        String query = "UPDATE conversationparticipants SET last_msg_time = ? WHERE user_id = ? AND conversation_id = ?";
        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(query))
        {
            stmt.setString(1, lastMsgTime);
            stmt.setInt(2, userId);
            stmt.setInt(3, conversationId);
            if(stmt.executeUpdate()==0)
            {
                System.err.println("Failed to update last message time");
                return false;
            }
            return true;
        }
        catch(SQLException e)
        {
            System.err.println("Failed to update last message time");
            return false;
        }
    }
}