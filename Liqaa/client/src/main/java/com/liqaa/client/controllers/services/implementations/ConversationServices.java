package com.liqaa.client.controllers.services.implementations;

import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.User;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.util.AlertNotifier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.liqaa.client.controllers.services.implementations.DataCenter;

import java.rmi.RemoteException;
import java.time.LocalDateTime;
import java.util.List;

public class ConversationServices {

    private static ConversationServices instance;

    private ConversationServices() {}

    public static synchronized ConversationServices getInstance() {
        if (instance == null) {
            instance = new ConversationServices();
        }
        return instance;
    }

    public void createDirectConversation(int userId, User otherUser)
    {
        try {
            ChatInfo chat = ServerConnection.getServer().createDirectConversation(userId, otherUser);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().add(chat)
            );
        } catch (RemoteException e) {
            handleError("Error creating conversation: ", e);
        }
    }

    public void loadDirectConversation(int userId, int recipientId) {
        try {
            ChatInfo chat = ServerConnection.getServer().getDirectConeversation(userId, recipientId);
            Platform.runLater(() -> {
                ObservableList<ChatInfo> chats = DataCenter.getInstance().getChats();
                if (!chats.contains(chat)) {
                    chats.add(chat);
                }
            });
        } catch (RemoteException e) {
            handleError("Error fetching direct conversation: ", e);
        }
    }

    public void loadAllConversations(int userId) {
        try {
            List<ChatInfo> conversations = ServerConnection.getServer().getAllConversations(userId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().setAll(conversations)
            );
        } catch (RemoteException e) {
            handleError("Error loading all conversations: ", e);
        }
    }

    public void loadPaginatedConversations(int userId, int limit, int offset) {
        try {
            List<ChatInfo> conversations = ServerConnection.getServer().getAllConversations(userId, limit, offset);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().addAll(conversations)
            );
        } catch (RemoteException e) {
            handleError("Error loading paginated conversations: ", e);
        }
    }

    public void loadGroupConversations(int userId) {
        try {
            List<ChatInfo> groupChats = ServerConnection.getServer().getGroupConversations(userId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().setAll(groupChats)
            );
        } catch (RemoteException e) {
            handleError("Error loading group conversations: ", e);
        }
    }

    public void loadUnreadConversations(int userId) {
        try {
            List<ChatInfo> unreadChats = ServerConnection.getServer().getUnreadConversations(userId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().setAll(unreadChats)
            );
        } catch (RemoteException e) {
            handleError("Error loading unread conversations: ", e);
        }
    }

    public void loadCategoryConversations(int userId, int categoryId) {
        try {
            List<ChatInfo> categoryChats = ServerConnection.getServer().getDirectConversations(userId, categoryId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().setAll(categoryChats)
            );
        } catch (RemoteException e) {
            handleError("Error loading category conversations: ", e);
        }
    }

    public void markConversationAsRead(int userId, int conversationId) {
        try {
            ServerConnection.getServer().markConversationAsRead(userId, conversationId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().stream()
                            .filter(chat -> chat.getConversationId() == conversationId)
                            .findFirst()
                            .ifPresent(chat -> chat.setUnreadMsgCount(0))
            );
        } catch (RemoteException e) {
            handleError("Error marking conversation as read: ", e);
        }
    }

    public void updateMsgCount(int userId, int conversationId, int count)
    {
        try {
            ServerConnection.getServer().updateMsgCount(userId, conversationId, count);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().stream()
                            .filter(chat -> chat.getConversationId() == conversationId)
                            .findFirst()
                            .ifPresent(chat -> chat.setUnreadMsgCount(count))
            );
        } catch (RemoteException e) {
            handleError("Error updating message count: ", e);
        }
    }

    public void updateLastMsgTime(int userId, int conversationId) {
        try {
            long newTime = System.currentTimeMillis();
            ServerConnection.getServer().updateLastMsgTime(userId, conversationId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().stream()
                            .filter(chat -> chat.getConversationId() == conversationId)
                            .findFirst()
                            .ifPresent(chat -> chat.setLastMsgTime(LocalDateTime.now()))
            );
        } catch (RemoteException e) {
            handleError("Error updating last message time: ", e);
        }
    }

    private void handleError(String message, RemoteException e)
    {
        System.err.println(message + e.getMessage());
        e.printStackTrace();
        AlertNotifier.createAlert("Error", message, e.getMessage());
    }
}