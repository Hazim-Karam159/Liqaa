package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.ConversationRepo;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Conversation;
import com.liqaa.shared.models.entities.UserContactCategory;
import com.liqaa.shared.models.enums.ConversationType;
import com.liqaa.shared.models.enums.CurrentStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class ConversationRepoImpl implements ConversationRepo
{
    private static ConversationRepoImpl instance;

    private ConversationRepoImpl(){}

    public static synchronized ConversationRepoImpl getInstance()
    {
        if(instance == null)
            instance = new ConversationRepoImpl();

        return instance;
    }

    @Override
    public int createDirectConversation() {
        String query = "INSERT INTO conversations (type) VALUES ('Direct')";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            int affectedRows = statement.executeUpdate();

            if (affectedRows == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }

            return 0;
        } catch (SQLException e) {
            System.err.println("Error in createDirectConversation: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public int createGroupConversation(int group_id)
    {
        if (group_id <= 0) {
            System.err.println("Error in createGroupConversation: group_id is invalid");
            return 0;
        }
        String query = "INSERT INTO conversations (type, group_id) VALUES ('Group', ?)";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, group_id);
            if (statement.executeUpdate() == 1) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys())
                {
                    if (generatedKeys.next())
                        return generatedKeys.getInt(1);
                }
            } else {
                System.err.println("Error in createGroupConversation: No rows affected");
                return 0;
            }
        } catch (SQLException e) {
            System.err.println("Error in createGroupConversation: " + e.getMessage());
            return 0;
        }
        return 0;
    }

    @Override
    public Conversation getConversation(int conversation_id)
    {
        if(conversation_id<=0)
        {
            System.err.println("Error in getConversation: conversation_id is invalid");
            return null;
        }
        String query = "SELECT * FROM conversations WHERE id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, conversation_id);
            var result = statement.executeQuery();
            if(result.next())
            {
                return new Conversation(result.getInt("id"),
                                        ConversationType.valueOf(result.getString("type").toUpperCase()),
                                        result.getInt("group_id"),
                                        result.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Error in getConversation: " + e.getMessage());
        }
        return null;
    }

    @Override
    public ChatInfo getDirectConversation(int userId, int recipientId)
    {
        if (userId <= 0 || recipientId <= 0) return null;

        String query = "SELECT c.id, c.type, c.created_at, " +
                "u.name, u.profile_picture, u.current_status, " +
                "cp.unread_msg_count, cp.last_msg_time " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp_user " +
                "  ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                "INNER JOIN conversationparticipants cp_recipient " +
                "  ON c.id = cp_recipient.conversation_id AND cp_recipient.user_id = ? " +
                "INNER JOIN users u ON u.id = cp_recipient.user_id " +
                "INNER JOIN conversationparticipants cp " +
                "  ON cp.conversation_id = c.id AND cp.user_id = ? " +
                "WHERE c.type = 'DIRECT'";


        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, recipientId);
            stmt.setInt(3, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ChatInfo(
                            rs.getInt("id"),
                            ConversationType.valueOf(rs.getString("type").toUpperCase()),
                            recipientId,
                            rs.getString("name"),
                            CurrentStatus.valueOf(rs.getString("current_status")),
                            rs.getBytes("profile_picture"),
                            rs.getInt("unread_msg_count"),
                            rs.getTimestamp("last_msg_time") != null ?
                                    rs.getTimestamp("last_msg_time").toLocalDateTime() : null,
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error in getDirectConversation: " + e.getMessage());
        }
        return null;
    }

    public ChatInfo getDirectConeversation(int userId, int recipientId)
    {
        if(userId<=0 || recipientId<=0)
        {
            System.err.println("Error in getDirectConeversation: userId or recipientId is invalid");
            return null;
        }
        String query = "SELECT c.id, c.type, c.created_at, u.profile_picture, u.current_status, cp.unread_msg_count, cp.last_msg_time " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp_user ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                "INNER JOIN conversationparticipants cp_recipient ON c.id = cp_recipient.conversation_id AND cp_recipient.user_id = ? " +
                "INNER JOIN users u ON u.id = cp_recipient.user_id " +
                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id AND cp.user_id = ? " +
                "WHERE c.type = 'DIRECT'";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, recipientId);
            statement.setInt(3, userId);
            var result = statement.executeQuery();
            if(result.next())
            {
                return new ChatInfo(result.getInt("id"),
                        ConversationType.valueOf(result.getString("type").toUpperCase()),
                        recipientId,
                        result.getString("name"),
                        CurrentStatus.valueOf(result.getString("current_status")),
                        result.getBytes("profile_picture"),
                        result.getInt("unread_msg_count"),
                        result.getTimestamp("last_msg_time") != null ?
                                result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                        result.getTimestamp("created_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            System.err.println("Error in getDirectConeversation: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Conversation getConversationByGroup(int groupId)
    {
        if(groupId <= 0)
        {
            System.err.println("Error in getConversationByGroup: groupId is invalid");
            return null;
        }

        String query = "SELECT c.id, c.type, c.group_id, c.created_at, MAX(cp.last_msg_time) AS last_msg_time " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id " +
                "WHERE c.group_id = ? " +
                "GROUP BY c.id, c.type, c.group_id, c.created_at ";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, groupId);
            var result = statement.executeQuery();

            if(result.next())
            {
                return new Conversation(
                        result.getInt("id"),
                        ConversationType.valueOf(result.getString("type").toUpperCase()),
                        result.getInt("group_id"),
                        result.getTimestamp("created_at").toLocalDateTime()
                );
            }
        } catch (SQLException e) {
            System.err.println("Error in getConversationByGroup: " + e.getMessage());
        }
        return null;
    }


//
//    @Override
//    public List<Conversation> getConversations(int userId, List<Integer> recipientsIds)
//    {
//        if (recipientsIds == null || recipientsIds.isEmpty()) {
//            System.err.println("Error in getConversations: recipientsIds is invalid");
//            return List.of();
//        }
//
//        String placeholders = String.join(", ", Collections.nCopies(recipientsIds.size(), "?"));
//
//        String query = "SELECT c.id, c.type, c.group_id, c.created_at, cp.last_msg_time " +
//                "FROM conversations c " +
//                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id " +
//                "WHERE c.id IN (" + placeholders + ") " +
//                "ORDER BY cp.last_msg_time DESC";
//
//        try (Connection connection = DatabaseManager.getConnection();
//             PreparedStatement statement = connection.prepareStatement(query)) {
//
//            for (int i = 0; i < recipientsIds.size(); i++) {
//                statement.setInt(i + 1, recipientsIds.get(i));
//            }
//
//            // Execute the query
//            ResultSet result = statement.executeQuery();
//            List<Conversation> conversations = new ArrayList<>();
//
//            while (result.next()) {
//                conversations.add(new Conversation(
//                        result.getInt("id"),
//                        ConversationType.valueOf(result.getString("type").toUpperCase()),
//                        result.getInt("group_id"),
//                        result.getTimestamp("created_at").toLocalDateTime(),
//                        result.getTimestamp("last_msg_time") != null ? result.getTimestamp("last_msg_time").toLocalDateTime() : null
//                ));
//            }
//
//            return conversations;
//        } catch (SQLException e) {
//            System.err.println("Error in getConversations: " + e.getMessage());
//        }
//
//        return List.of();
//    }


    @Override
    public List<ChatInfo> getAllConversations(int userId) {
        if (userId <= 0) {
            System.err.println("Error in getAllConversations: userId is invalid");
            return List.of();
        }
        String query = "SELECT " +
                "c.id, " +
                "c.type, " +
                "c.created_at, " +
                "cp_user.unread_msg_count, " + // Changed to cp_user
                "cp_user.last_msg_time, " +    // Changed to cp_user
                "CASE WHEN c.type = 'GROUP' THEN g.id ELSE u.id END AS recipient_id, " +
                "CASE WHEN c.type = 'GROUP' THEN g.name ELSE u.name END AS recipient_name, " +
                "CASE WHEN c.type = 'GROUP' THEN g.image ELSE u.profile_picture END AS recipient_image, " +
                "CASE WHEN c.type = 'GROUP' THEN NULL ELSE u.current_status END AS current_status " + // Handle NULL for groups
                "FROM Conversations c " +
                "INNER JOIN ConversationParticipants cp_user " +
                "ON c.id = cp_user.conversation_id " +
                "AND cp_user.user_id = ? " +
                "LEFT JOIN ConversationParticipants cp " + // Changed to LEFT JOIN
                "ON c.id = cp.conversation_id " +
                "AND cp.user_id <> ? " +
                "AND c.type = 'DIRECT' " + // Only join for direct conversations
                "LEFT JOIN Users u " +
                "ON u.id = cp.user_id " +
                "LEFT JOIN Groups_ g " +
                "ON c.group_id = g.id " +
                "ORDER BY cp_user.last_msg_time DESC"; // Order by user's last_msg_time

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);

            var result = statement.executeQuery();
            List<ChatInfo> chatInfos = new ArrayList<>();

            while (result.next()) {
                ConversationType type = ConversationType.valueOf(result.getString("type").toUpperCase());

                int recipientId = result.getInt("recipient_id");
                String recipientName = result.getString("recipient_name");
                byte[] image = result.getBytes("recipient_image");

                // Handle NULL current_status for groups
                String currentStatusStr = result.getString("current_status");
                CurrentStatus currentStatus = currentStatusStr != null ?
                        CurrentStatus.valueOf(currentStatusStr) : null;

                chatInfos.add(new ChatInfo(
                        result.getInt("id"),
                        type,
                        recipientId,
                        recipientName,
                        currentStatus,
                        image,
                        result.getInt("unread_msg_count"),
                        result.getTimestamp("last_msg_time") != null ?
                                result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                        result.getTimestamp("created_at").toLocalDateTime()
                ));
            }
            return chatInfos;
        } catch (SQLException e) {
            System.err.println("Error in getAllConversations: " + e.getMessage());
        }
        return List.of();
    }

    @Override
        public List<ChatInfo> getAllConversations(int userId, int limit, int offset) {
            if (userId <= 0) {
                System.err.println("Error in getAllConversations: userId is invalid");
                return List.of();
            }

            String query = "SELECT " +
                    "c.id, " +
                    "c.type, " +
                    "c.created_at, " +
                    "cp.unread_msg_count, " +
                    "cp.last_msg_time, " +
                    "CASE WHEN c.type = 'GROUP' THEN g.id ELSE u.id END AS recipient_id, " +
                    "CASE WHEN c.type = 'GROUP' THEN g.name ELSE u.name END AS recipient_name, " +
                    "CASE WHEN c.type = 'GROUP' THEN g.image ELSE u.profile_picture END AS recipient_image, " +
                    "u.current_status " +
                    "FROM conversations c " +
                    "INNER JOIN conversationparticipants cp_user ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                    "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id AND cp.user_id <> ? " +
                    "LEFT JOIN users u ON u.id = cp.user_id " +
                    "LEFT JOIN groups_ g ON c.group_id = g.id " +
                    "ORDER BY cp.last_msg_time DESC " +
                    "LIMIT ? OFFSET ?";

            try (Connection connection = DatabaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, userId);
                statement.setInt(2, userId);
                statement.setInt(3, limit);
                statement.setInt(4, offset);

                var result = statement.executeQuery();
                List<ChatInfo> chatInfos = new ArrayList<>();

                while (result.next()) {
                    ConversationType type = ConversationType.valueOf(result.getString("type").toUpperCase());

                    int recipientId = result.getInt("recipient_id");
                    String recipientName = result.getString("recipient_name");
                    byte[] recipientImage = result.getBytes("recipient_image");

                    chatInfos.add(new ChatInfo(
                            result.getInt("id"),
                            type,
                            recipientId,
                            recipientName,
                            CurrentStatus.valueOf(result.getString("current_status")),
                            recipientImage,
                            result.getInt("unread_msg_count"),
                            result.getTimestamp("last_msg_time") != null ?
                                    result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                            result.getTimestamp("created_at").toLocalDateTime()
                    ));
                }
                return chatInfos;
            } catch (SQLException e) {
                System.err.println("Error in getAllConversations: " + e.getMessage());
            }
            return List.of();
        }

    @Override
    public List<ChatInfo> getGroupConversations(int userId) {
        if (userId <= 0) {
            System.err.println("Error in getGroupConversations: userId is invalid");
            return List.of();
        }

        String query = """
        SELECT 
            c.id, 
            c.created_at, 
            g.id AS group_id,
            g.name AS group_name, 
            g.description AS group_description, 
            g.image AS group_image, 
            cp_user.unread_msg_count,  -- Use current user's unread count
            cp_user.last_msg_time      -- Use current user's last message time
        FROM Conversations c
        INNER JOIN ConversationParticipants cp_user 
            ON c.id = cp_user.conversation_id 
            AND cp_user.user_id = ?
        INNER JOIN Groups_ g 
            ON c.group_id = g.id
        WHERE c.type = 'GROUP'  -- Ensure we only get group conversations
        ORDER BY cp_user.last_msg_time DESC  -- Order by user's last message time
        """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);

            try (ResultSet result = statement.executeQuery()) {
                List<ChatInfo> chatInfos = new ArrayList<>();
                while (result.next()) {
                    ChatInfo chatInfo = new ChatInfo(
                            result.getInt("id"),
                            ConversationType.GROUP,
                            result.getInt("group_id"),
                            result.getString("group_name"),
                            null,
                            result.getBytes("group_image"),
                            result.getInt("unread_msg_count"),
                            result.getTimestamp("last_msg_time") != null ?
                                    result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                            result.getTimestamp("created_at").toLocalDateTime()
                    );
                    chatInfos.add(chatInfo);
                }
                return chatInfos;
            }
        } catch (SQLException e) {
            System.err.println("Error in getGroupConversations: " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();
    }


    @Override
    public List<ChatInfo> getGroupConversations(int userId, int limit, int offset)
    {
        if(userId <= 0)
        {
            System.err.println("Error in getGroupConversations: userId is invalid");
            return List.of();
        }

        String query = "SELECT c.id, c.type, c.created_at, g.name AS group_name, g.description AS group_description, g.image AS group_image, cp.unread_msg_count, cp.last_msg_time " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp_user ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id AND cp.user_id = ? " +
                "INNER JOIN groups_ g ON c.group_id = g.id " +
                "WHERE c.type = 'GROUP' " +
                "ORDER BY cp.last_msg_time DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, limit);
            statement.setInt(4, offset);
            var result = statement.executeQuery();
            List<ChatInfo> chatInfos = new ArrayList<>();
            while(result.next())
            {
                ChatInfo chatInfo = new ChatInfo(
                        result.getInt("id"),
                        ConversationType.GROUP,
                        result.getInt("group_id"),
                        result.getString("group_name"),
                        null,
                        result.getBytes("group_image"),
                        result.getInt("unread_msg_count"),
                        result.getTimestamp("last_msg_time") != null ?
                                result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                        result.getTimestamp("created_at").toLocalDateTime()
                );
                chatInfos.add(chatInfo);
            }
            return chatInfos;
        } catch (SQLException e) {
            System.err.println("Error in getGroupConversations: " + e.getMessage());
        }
        return List.of();
    }

    @Override
    public List<ChatInfo> getUnreadConversations(int userId) {
        if (userId <= 0) {
            System.err.println("Error in getUnreadConversations: userId is invalid");
            return List.of();
        }

        String query = """
        SELECT 
            c.id, 
            c.type, 
            c.created_at, 
            cp_user.unread_msg_count, 
            cp_user.last_msg_time,
            CASE 
                WHEN c.type = 'GROUP' THEN g.id 
                ELSE u.id 
            END AS entity_id,
            CASE 
                WHEN c.type = 'GROUP' THEN g.name 
                ELSE u.name 
            END AS entity_name,
            CASE 
                WHEN c.type = 'GROUP' THEN g.image 
                ELSE u.profile_picture 
            END AS entity_image,
            u.current_status
        FROM conversations c
        INNER JOIN conversationparticipants cp_user 
            ON c.id = cp_user.conversation_id 
            AND cp_user.user_id = ?
        LEFT JOIN conversationparticipants cp_other
            ON c.id = cp_other.conversation_id 
            AND cp_other.user_id <> ?
            AND c.type = 'DIRECT'
        LEFT JOIN users u 
            ON u.id = cp_other.user_id
        LEFT JOIN groups_ g 
            ON c.group_id = g.id
        WHERE cp_user.unread_msg_count > 0 
        ORDER BY cp_user.last_msg_time DESC
        """;

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, userId);
            statement.setInt(2, userId);

            try (ResultSet result = statement.executeQuery()) {
                List<ChatInfo> chatInfos = new ArrayList<>();

                while (result.next()) {
                    ConversationType type = ConversationType.valueOf(
                            result.getString("type").toUpperCase()
                    );

                    ChatInfo chatInfo = new ChatInfo(
                            result.getInt("id"),
                            type,
                            result.getInt("entity_id"),
                            result.getString("entity_name"),
                            type == ConversationType.DIRECT
                                    ? CurrentStatus.valueOf(result.getString("current_status"))
                                    : null,
                            result.getBytes("entity_image"),
                            result.getInt("unread_msg_count"),
                            result.getTimestamp("last_msg_time") != null
                                    ? result.getTimestamp("last_msg_time").toLocalDateTime()
                                    : null,
                            result.getTimestamp("created_at").toLocalDateTime()
                    );

                    chatInfos.add(chatInfo);
                }
                return chatInfos;
            }
        } catch (SQLException e) {
            System.err.println("Error in getUnreadConversations: " + e.getMessage());
        }
        return List.of();
    }


    @Override
    public List<ChatInfo> getUnreadConversations(int userId, int limit, int offset) {
        if (userId <= 0) {
            System.err.println("Error in getUnreadConversations: userId is invalid");
            return List.of();
        }
        String query = "SELECT c.id, c.type, c.created_at, u.profile_picture, u.current_status, " +
                "cp_user.unread_msg_count, cp_user.last_msg_time, " +
                "g.id AS group_id, g.name AS group_name, g.image AS group_image, " +
                "u.id AS user_id, u.name AS user_name, u.profile_picture AS user_image " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp_user ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id AND cp.user_id != ? " +
                "INNER JOIN users u ON u.id = cp.user_id " +
                "LEFT JOIN groups_ g ON c.group_id = g.id " +
                "WHERE cp_user.unread_msg_count > 0 " +
                "ORDER BY cp.last_msg_time DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, userId);
            statement.setInt(3, limit);
            statement.setInt(4, offset);
            ResultSet result = statement.executeQuery();

            List<ChatInfo> chatInfos = new ArrayList<>();
            while (result.next()) {
                ConversationType type = ConversationType.valueOf(result.getString("type").toUpperCase());
                int id;
                String name;
                byte[] image;

                if (type == ConversationType.GROUP) {
                    id = result.getInt("group_id");
                    name = result.getString("group_name");
                    image = result.getBytes("group_image");
                } else if (type == ConversationType.DIRECT) {
                    id = result.getInt("user_id");
                    name = result.getString("user_name");
                    image = result.getBytes("user_image");
                } else {
                    System.err.println("Error in getUnreadConversations: unknown conversation type");
                    continue;
                }

                chatInfos.add(new ChatInfo(
                        result.getInt("id"),
                        type,
                        id,
                        name,
                        CurrentStatus.valueOf(result.getString("current_status")),
                        result.getBytes("profile_picture"),
                        result.getInt("unread_msg_count"),
                        result.getTimestamp("last_msg_time") != null ?
                                result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                        result.getTimestamp("created_at").toLocalDateTime()
                ));
            }
            return chatInfos;
        } catch (SQLException e) {
            System.err.println("Error in getUnreadConversations: " + e.getMessage());
        }
        return List.of();
    }


    List<ChatInfo> getDirectConversations(int userId, List<Integer> recipients)
    {
        if(userId <= 0 || recipients == null || recipients.isEmpty())
        {
            System.err.println("Error in getDirectConversations: userId or recipients list is invalid");
            return List.of();
        }

        String query = "SELECT c.id, c.type, c.created_at, u.id AS user_id, u.name AS user_name, u.profile_picture, " +
                "u.current_status, cp.unread_msg_count, cp.last_msg_time " +
                "FROM conversations c " +
                "INNER JOIN conversationparticipants cp_user ON c.id = cp_user.conversation_id AND cp_user.user_id = ? " +
                "INNER JOIN conversationparticipants cp ON cp.conversation_id = c.id AND cp.user_id IN (?) " +
                "INNER JOIN users u ON u.id = cp.user_id " +
                "WHERE c.type = 'DIRECT'" +
                "ORDER BY cp.last_msg_time DESC ";

        String recipientsPlaceholder = String.join(",", Collections.nCopies(recipients.size(), "?"));

        query = query.replace("IN (?)", "IN (" + recipientsPlaceholder + ")");

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            int paramIndex = 2;
            for (Integer recipient : recipients) {
                statement.setInt(paramIndex++, recipient);
            }

            ResultSet result = statement.executeQuery();
            List<ChatInfo> chatInfos = new ArrayList<>();

            while(result.next())
            {
                chatInfos.add(new ChatInfo(
                        result.getInt("id"),
                        ConversationType.DIRECT,
                        result.getInt("user_id"),
                        result.getString("user_name"),
                        CurrentStatus.valueOf(result.getString("current_status")),
                        result.getBytes("profile_picture"),
                        result.getInt("unread_msg_count"),
                        result.getTimestamp("last_msg_time") != null ?
                                result.getTimestamp("last_msg_time").toLocalDateTime() : null,
                        result.getTimestamp("created_at").toLocalDateTime()
                ));
            }
            return chatInfos;
        }
        catch (SQLException e)
        {
            System.err.println("Error in getDirectConversations: " + e.getMessage());
            e.printStackTrace();
        }
        return List.of();
    }


    @Override
    public void deleteConversation(int conversationId)
    {
        if(conversationId <= 0)
        {
            System.err.println("Error in deleteConversation: conversationId is invalid");
            return;
        }

        String deleteParticipantsQuery = "DELETE FROM conversation_participants WHERE conversation_id = ?";

        String deleteConversationQuery = "DELETE FROM conversations WHERE id = ?";

        try (Connection connection = DatabaseManager.getConnection())
        {
            try (PreparedStatement statement = connection.prepareStatement(deleteParticipantsQuery))
            {
                statement.setInt(1, conversationId);
                statement.executeUpdate();
            }

            try (PreparedStatement statement = connection.prepareStatement(deleteConversationQuery))
            {
                statement.setInt(1, conversationId);
                statement.executeUpdate();
            }
        }
        catch (SQLException e)
        {
            System.err.println("Error in deleteConversation: " + e.getMessage());
            e.printStackTrace();
        }

    }

    @Override
    public void updateLastMsgTime(int userId, int conversationId)
    {
        if (userId <= 0 || conversationId <= 0) {
            System.err.println("Error in updateLastMsgTime: userId or conversationId is invalid");
            return;
        }

        String query = "UPDATE conversationparticipants SET last_msg_time = NOW() WHERE user_id = ? AND conversation_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, conversationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in updateLastMsgTime: " + e.getMessage());
        }
    }

    @Override
    public void markConversationAsRead(int userId, int conversationId) {
        if (userId <= 0 || conversationId <= 0)
        {
            System.err.println("Error in markConversationAsRead: Invalid input parameters");
            return;
        }

        String query = "UPDATE conversationparticipants SET unread_msg_count = 0 WHERE user_id = ? AND conversation_id = ?";

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query))
        {
            statement.setInt(1, userId);
            statement.setInt(2, conversationId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error in markConversationAsRead: " + e.getMessage());
        }
    }

}