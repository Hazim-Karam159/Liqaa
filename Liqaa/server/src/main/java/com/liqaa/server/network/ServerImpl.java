package com.liqaa.server.network;

import com.liqaa.server.controllers.services.implementations.*;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.*;
import com.liqaa.shared.network.Server;
import com.liqaa.server.controllers.services.interfaces.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.liqaa.shared.network.Client;

public class ServerImpl extends UnicastRemoteObject implements Server
{
    private static ConcurrentHashMap<Integer, Client> onlineClients = new ConcurrentHashMap<>();
    private static ServerImpl server;
    private final UserServicesInt userServices = UserServicesImpl.getInstance();

    private ServerImpl() throws RemoteException
    {
        super();
    }

    @Override
    public synchronized void unregisterClient(int userId) throws RemoteException {
        onlineClients.remove(userId);
    }

    @Override
    public synchronized void registerClient(Client client, int id) throws RemoteException {
        onlineClients.put(id, client);
    }

    @Override
    public synchronized User signIn(Client client, String phone, String password) throws RemoteException
    {
        User user= userServices.signIn(phone, password);
        System.out.println("User in sign in: "+user);
        if(user!=null) {
//            System.out.println("test");
            onlineClients.put(user.getId(), client);

            return user;
        }
        return null;
    }



    private void notifyClientsAboutMessage(Message message) {
        onlineClients.forEach((userId, client) -> {
            try {
                client.receiveMessage(message);
            } catch (RemoteException e) {
                System.out.println("Client " + userId + " disconnected");
                onlineClients.remove(userId);
            }
        });
    }

    // Similar methods for other notifications (status changes, new contacts, etc.)

    // Example usage for message notification
//    private void notifyAboutMessage(int userId, Message message) {
//        notifyUser(userId, client -> client.receiveMessage(message));
//    }

    public static Server getServer() {
        try {
            if (server == null)
                server = new ServerImpl();
            return server;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void logout(int userId) throws RemoteException {
        onlineClients.remove(userId);
    }

    @Override
    public User signUp(User user) throws RemoteException {
        return userServices.signUp(user);
    }

    @Override
    public User getUserInfo(String userPhone) throws RemoteException {
        return userServices.getUserInfo(userPhone);
    }
    @Override
    public User getUserInfoById(int userId) throws RemoteException {
        return userServices.getUserInfoById(userId);
    }
    @Override
    public boolean updateUserInfo(User user) throws RemoteException {
        return userServices.updateUserInfo(user);
    }

    @Override
    public boolean updateUserImage(String phone, byte[] img) throws RemoteException {
        return userServices.updateUserImage(phone, img);
    }

    @Override
    public boolean deleteUser(int userId) throws RemoteException {
        return userServices.deleteUser(userId);
    }
    @Override
    public int getNumberAllUsers() throws RemoteException {
        return userServices.getNumberAllUsers();
    }

    @Override
    public int getNumberAllMaleUsers() throws RemoteException {
        return userServices.getNumberAllMaleUsers();
    }

    @Override
    public int getNumberAllFemaleUsers() throws RemoteException {
        return userServices.getNumberAllFemaleUsers();
    }

    @Override
    public int getNumberAllOnlineUsers() throws RemoteException {
        return userServices.getNumberAllOnlineUsers();
    }

    @Override
    public int getNumberAllOfflineUsers() throws RemoteException {
        return userServices.getNumberAllOfflineUsers();
    }

    @Override
    public int getNumberAllCountryOfUsers() throws RemoteException {
        return userServices.getNumberAllCountryOfUsers();
    }
    @Override
    public Map<String, Integer> getTopCountriesOfUsers() throws RemoteException {
        return userServices.getTopCountriesOfUsers();
    }
    @Override
    public List<User> getAllUsers() throws RemoteException {
        return userServices.getAllUsers();
    }

    @Override
    public List<Notification> getUserNotifications(int userId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getUserNotifications(userId);
    }

    @Override
    public List<Notification> getUpcomingRequests(int recipientId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getUpcomingRequests(recipientId);
    }

    @Override
    public List<Notification> getAcceptedInvitations(int recipientId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getAcceptedInvitations(recipientId);
    }

    @Override
    public List<Notification> getDeclinedInvitations(int recipientId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getDeclinedInvitations(recipientId);
    }

    @Override
    public List<Notification> getAnnouncements(int recipientId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getAnnouncements(recipientId);
    }

    @Override
    public List<Notification> getUnreadNotifications(int recipientId) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getUnreadNotifications(recipientId);
    }
    @Override
    public boolean sendNotification(Notification notification) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().sendNotification(notification);
    }

    @Override
    public List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().getAnnouncementNotifications();
    }

    @Override
    public AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException, RemoteException{
        return NotificationServiceImpl.getInstance().getAnnouncementNotificationsById(notificationId);
    }

    @Override
    public boolean deleteNotification(int id) throws SQLException, RemoteException {
        return NotificationServiceImpl.getInstance().deleteNotification(id);
    }

    @Override
    public void markAllAsRead(int recipientId) throws SQLException, RemoteException {
        NotificationServiceImpl.getInstance().markAllAsRead(recipientId);
    }

    @Override
    public List<Message> getMessagesByConversationId(int conversationId) throws SQLException, RemoteException {
        return MessageServiceImpl.getInstance().getMessagesByConversationId(conversationId);
    }

    @Override
    public List<Message> getMessagesByConversationId(int conversationId, int offset, int limit) throws SQLException, RemoteException {
        return MessageServiceImpl.getInstance().getMessagesByConversationId(conversationId, offset, limit);
    }

    @Override
    public int sendMessage(Message message) throws  RemoteException {
        int id = MessageServiceImpl.getInstance().sendMessage(message);
        onlineClients.forEach((userId, client) -> {
            if(userId != message.getSenderId()) {
                try {
                    client.receiveMessage(message);
                } catch (RemoteException e) {
                    System.out.println("Client " + userId + " disconnected");
                    onlineClients.remove(userId);
                }
            }
        });
        return id;
    }

    @Override
    public int getUnreadMessageCount(int conversationId, int userId) throws SQLException, RemoteException {
        return MessageServiceImpl.getInstance().getUnreadMessageCount(conversationId, userId);
    }

    @Override
    public void markMessagesAsSeen(int conversationId, int userId) throws SQLException, RemoteException {
        MessageServiceImpl.getInstance().markMessagesAsSeen(conversationId, userId);
    }

    public FileMessage getFileInfo(int messageId) throws SQLException, RemoteException {
        return MessageServiceImpl.getInstance().getFileInfo(messageId);
    }
    public int sendFile(FileMessage fileInfo) throws SQLException, RemoteException
    {
        return MessageServiceImpl.getInstance().sendFile(fileInfo);
    }

    @Override
    public boolean uploadFile(byte[] fileData, String fileName, int conversationId, int messageId) throws RemoteException {
        try
        {
            // Create conversation directory if needed
            String basePath = "./files/" + conversationId + "/";
            new File(basePath).mkdirs();

            // Save file
            Path filePath = Paths.get(basePath + messageId + "_" + fileName);
            Files.write(filePath, fileData);

            MessageServiceImpl.getInstance().updateFileInfo(messageId, filePath);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteMessage(int messageId) throws SQLException, RemoteException {
        MessageServiceImpl.getInstance().deleteMessage(messageId);
    }

    @Override
    public byte[] downloadFile(int messageId, int conversationId) throws RemoteException {
            try
            {
                FileMessage metadata = MessageServiceImpl.getInstance().getFileInfo(messageId);
                Path filePath = Paths.get(metadata.getFilePath());
                return Files.readAllBytes(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
    }


    @Override
    public void createGroup(Group group, ArrayList<Integer> groupMembers) throws RemoteException {
        GroupServicesImpl.getInstance().createGroup(group, groupMembers);
    }

    @Override
    public void updateGroup(int groupId, int ownerId, String name, String description) throws RemoteException {
        GroupServicesImpl.getInstance().updateGroup(groupId, ownerId, name, description);
    }

    @Override
    public void updateGroupImage(int groupId, int ownerId, byte[] image) throws RemoteException {
        GroupServicesImpl.getInstance().updateGroupImage(groupId, ownerId, image);
    }

    @Override
    public void deleteGroup(int groupId, int ownerId) throws RemoteException {
        GroupServicesImpl.getInstance().deleteGroup(groupId, ownerId);
    }

    @Override
    public void addMembers(int groupId, int ownerId, List<Integer> groupMembers) throws RemoteException {
        GroupServicesImpl.getInstance().addMembers(groupId, ownerId, groupMembers);
    }

    @Override
    public void removeMembers(int groupId, int ownerId, List<Integer> groupMembers) throws RemoteException {
        GroupServicesImpl.getInstance().removeMembers(groupId, ownerId, groupMembers);
    }
    @Override
    public ChatInfo createDirectConversation(int userId, User otherUserId) throws RemoteException {
        return ConversationServicesImpl.getInstance().createDirectConversation(userId, otherUserId);
    }

    @Override
    public ChatInfo getDirectConeversation(int userId, int recipientId) throws RemoteException {
        return ConversationServicesImpl.getInstance().getDirectConeversation(userId, recipientId);
    }

    @Override
    public List<ChatInfo> getAllConversations(int userId) throws RemoteException {
        return ConversationServicesImpl.getInstance().getAllConversations(userId);
    }

    @Override
    public List<ChatInfo> getAllConversations(int userId, int limit, int offset) throws RemoteException {
        return ConversationServicesImpl.getInstance().getAllConversations(userId, limit, offset);
    }

    @Override
    public List<ChatInfo> getGroupConversations(int userId) throws RemoteException {
        return ConversationServicesImpl.getInstance().getGroupConversations(userId);
    }

    @Override
    public List<ChatInfo> getGroupConversations(int userId, int limit, int offset) throws RemoteException {
        return ConversationServicesImpl.getInstance().getGroupConversations(userId, limit, offset);
    }

    @Override
    public List<ChatInfo> getUnreadConversations(int userId) throws RemoteException {
        return ConversationServicesImpl.getInstance().getUnreadConversations(userId);
    }

    @Override
    public List<ChatInfo> getUnreadConversations(int userId, int limit, int offset) throws RemoteException {
        return ConversationServicesImpl.getInstance().getUnreadConversations(userId, limit, offset);
    }

    @Override
    public List<ChatInfo> getDirectConversations(int userId, int categoryId) throws RemoteException {
        return ConversationServicesImpl.getInstance().getDirectConversations(userId, categoryId);
    }

    @Override
    public List<ChatInfo> getDirectConversations(int userId, int categoryId, int limit, int offset) throws RemoteException {
        return ConversationServicesImpl.getInstance().getDirectConversations(userId, categoryId, limit, offset);
    }

    @Override
    public void markConversationAsRead(int userId, int conversationId) throws RemoteException {
        ConversationServicesImpl.getInstance().markConversationAsRead(userId, conversationId);
    }

    @Override
    public void updateMsgCount(int userId, int conversationId, int count) throws RemoteException {
        ConversationServicesImpl.getInstance().updateMsgCount(userId, conversationId, count);
    }

    @Override
    public void updateLastMsgTime(int userId, int conversationId) throws RemoteException {
        ConversationServicesImpl.getInstance().updateLastMsgTime(userId, conversationId);
    }

    @Override
    public User getContact(String name) throws RemoteException {
        return UserServicesImpl.getInstance().getUserInfo(name);
    }

    @Override
    public List<User> getUserFriends(int userID) throws RemoteException {
        return ContactServicesImpl.getInstance().getUserFriends(userID);
    }

    @Override
    public List<FriendRequests> getUserFriendRequests(int userID) throws RemoteException {
        return ContactServicesImpl.getInstance().getUserFriendRequests(userID);
    }

    @Override
    public boolean updateFriendsRequestStatus(FriendRequests updatedRequest) throws RemoteException {
        return ContactServicesImpl.getInstance().updateFriendsRequestStatus(updatedRequest);
    }

    @Override
    public boolean sendFriendRequest(FriendRequests addRequests) throws RemoteException {
        return ContactServicesImpl.getInstance().sendFriendRequest(addRequests);
    }

    @Override
    public boolean createNotification(Notification notification) throws SQLException, RemoteException{
        return NotificationServiceImpl.getInstance().createNotification(notification);
    }

    @Override
    public boolean addContact(int userId, int contactId) throws RemoteException {
        return ContactServicesImpl.getInstance().addContact(userId,contactId);
    }

    @Override
    public boolean deleteContact(int userId, int contactId) throws RemoteException {
        return ContactServicesImpl.getInstance().deleteContact(userId,contactId);
    }

    @Override
    public boolean blockContact(int userId, int contactId) throws RemoteException {
        return ContactServicesImpl.getInstance().blockContact(userId,contactId);
    }

    @Override
    public boolean unblockContact(int userId, int contactId) throws RemoteException {
        return ContactServicesImpl.getInstance().unblockContact(userId,contactId);
    }

    @Override
    public boolean isBlocked(int userId, int contactId) throws RemoteException {
        return ContactServicesImpl.getInstance().isBlocked(userId,contactId);
    }

    @Override
    public List<Category> getCategories(int userId) throws RemoteException {
        return CategoryServicesImpl.getInstance().getCategories(userId);
    }

    @Override
    public List<Integer> addCategories(List<Category> categories) throws RemoteException {
        return CategoryServicesImpl.getInstance().addCategories(categories);
    }

    @Override
    public void addContactsToCategory(int categoryId, List<Integer> contactIds) throws RemoteException {
        CategoryServicesImpl.getInstance().addContactsToCategory(categoryId, contactIds);
    }

    @Override
    public void removeContactsFromCateogry(int categoryId, List<Integer> contactIds) throws RemoteException {
        CategoryServicesImpl.getInstance().removeContactsFromCateogry(categoryId, contactIds);
    }

    @Override
    public List<Integer> getCategoryContacts(int categoryId) throws RemoteException {
        return CategoryServicesImpl.getInstance().getCategoryContacts(categoryId);
    }

    @Override
    public void renameCategory(int categoryId, String newName) throws RemoteException {
        CategoryServicesImpl.getInstance().renameCategory(categoryId, newName);
    }

    @Override
    public void removeCategory(int categoryId, int userId) throws RemoteException {
        CategoryServicesImpl.getInstance().removeCategory(categoryId, userId);
    }

    @Override
    public List<Category> getCategoriesForContact(int userId, int contactId) throws RemoteException {
        return CategoryServicesImpl.getInstance().getCategoriesForContact(userId, contactId);
    }

    @Override
    public Map<Category, List<Integer>> getCategoriesWithContacts(int userId) throws RemoteException {
        return CategoryServicesImpl.getInstance().getCategoriesWithContacts(userId);
    }

    @Override
    public void removeCategory(String categoryName, int userId) throws RemoteException
    {
        CategoryServicesImpl.getInstance().removeCategory(categoryName, userId);
    }

    @Override
    public String ping() throws RemoteException {
        return "Connection established";
    }

    @Override
    public String getResponse(String userMessage) throws RemoteException {
//        return GeminiAPI.getBotResponse(userMessage);
        return null;
    }
}