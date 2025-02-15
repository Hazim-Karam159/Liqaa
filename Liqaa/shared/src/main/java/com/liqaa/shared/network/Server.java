
package com.liqaa.shared.network;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface Server extends Remote
{
    void unregisterClient(int userId) throws RemoteException;

    void registerClient(Client client, int id) throws RemoteException;

    public User signIn (Client client, String userPhone, String userPassword) throws RemoteException;
    void logout(int userId) throws RemoteException;

    public User signUp(User user) throws RemoteException;

    public User getUserInfo(String userPhone) throws RemoteException;
    public User getUserInfoById(int userId) throws RemoteException;
    public boolean updateUserInfo (User user) throws RemoteException;
    public boolean updateUserImage ( String phone, byte[] img) throws RemoteException;
    public boolean deleteUser ( int userId) throws RemoteException;

    public  int getNumberAllUsers() throws RemoteException;
    public  int getNumberAllMaleUsers() throws RemoteException;
    public  int getNumberAllFemaleUsers() throws RemoteException;
    public  int getNumberAllOnlineUsers() throws RemoteException;
    public  int getNumberAllOfflineUsers() throws RemoteException;
    public  int getNumberAllCountryOfUsers() throws RemoteException;
    public Map<String,Integer> getTopCountriesOfUsers() throws RemoteException;
    public List<User> getAllUsers() throws RemoteException;




    List<Notification> getUserNotifications(int userId) throws SQLException, RemoteException;
    List<Notification> getUpcomingRequests(int recipientId) throws SQLException, RemoteException;
    List<Notification> getAcceptedInvitations(int recipientId) throws SQLException, RemoteException;
    List<Notification> getDeclinedInvitations(int recipientId) throws SQLException, RemoteException;
    List<Notification> getAnnouncements(int recipientId) throws SQLException, RemoteException;
    List<Notification> getUnreadNotifications(int recipientId) throws SQLException, RemoteException;
    boolean sendNotification(Notification notification) throws SQLException, RemoteException;
    List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException, RemoteException;
    AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException, RemoteException;
    boolean deleteNotification(int id) throws SQLException, RemoteException;
    void markAllAsRead(int recipientId) throws SQLException, RemoteException;




    List<Message> getMessagesByConversationId(int conversationId) throws SQLException, RemoteException; // Without pagination
    List<Message> getMessagesByConversationId(int conversationId, int offset, int limit) throws SQLException, RemoteException; // With pagination
    int sendMessage(Message message) throws SQLException, RemoteException; // Send a message
    int getUnreadMessageCount(int conversationId, int userId) throws SQLException, RemoteException; // Get unread message count
    void markMessagesAsSeen(int conversationId, int userId) throws SQLException, RemoteException; // Mark messages as seen
    public FileMessage getFileInfo(int messageId) throws SQLException, RemoteException;
    public int sendFile(FileMessage fileInfo) throws SQLException, RemoteException;
    public boolean uploadFile(byte[] fileData, String fileName, int conversationId, int messageId) throws RemoteException;
    public void deleteMessage(int messageId) throws SQLException, RemoteException;
    public byte[] downloadFile(int messageId, int conversationId) throws RemoteException;


    public void createGroup(Group group, ArrayList<Integer> groupMembers) throws RemoteException;
    public void updateGroup(int groupId, int ownerId, String name, String description) throws RemoteException;
    public void updateGroupImage(int groupId, int ownerId, byte[] image) throws RemoteException;
    public void deleteGroup(int groupId, int ownerId) throws RemoteException;
    public void addMembers(int groupId, int ownerId, List<Integer> groupMembers) throws RemoteException;
    public void removeMembers(int groupId, int ownerId, List<Integer> groupMembers) throws RemoteException;




    ChatInfo createDirectConversation(int userId, User otherUserId) throws RemoteException;

    ChatInfo getDirectConeversation(int userId, int recipientId) throws RemoteException;

    List<ChatInfo> getAllConversations(int userId) throws RemoteException;
    List<ChatInfo> getAllConversations(int userId, int limit, int offset) throws RemoteException;

    List<ChatInfo> getGroupConversations(int userId) throws RemoteException;
    List<ChatInfo> getGroupConversations(int userId, int limit, int offset) throws RemoteException;

    List<ChatInfo> getUnreadConversations(int userId) throws RemoteException;
    List<ChatInfo> getUnreadConversations(int userId, int limit, int offset) throws RemoteException;

    List<ChatInfo> getDirectConversations(int userId, int categoryId) throws RemoteException;
    List<ChatInfo> getDirectConversations(int userId, int categoryId, int limit, int offset) throws RemoteException;

    void markConversationAsRead(int userId, int conversationId) throws RemoteException;
    void updateMsgCount(int userId, int conversationId, int count) throws RemoteException;
    void updateLastMsgTime(int userId, int conversationId) throws RemoteException;




    public User getContact(String name) throws RemoteException; ///////
    public List<User> getUserFriends(int userID) throws RemoteException; //get Contacts
    public List<FriendRequests> getUserFriendRequests(int userID) throws RemoteException; // when calling , check if empty list or not
    public boolean updateFriendsRequestStatus( FriendRequests updatedRequest) throws RemoteException;//rejected>>remove request , Accepted>>add contact
    public boolean sendFriendRequest( FriendRequests addRequests) throws RemoteException;

    boolean createNotification(Notification notification) throws SQLException, RemoteException;

    public  boolean addContact (int userId , int contactId) throws RemoteException;
    public boolean deleteContact (int userId , int contactId) throws RemoteException;
    public  boolean blockContact (int userId , int contactId) throws RemoteException;
    public  boolean unblockContact (int userId , int contactId) throws RemoteException;
    public boolean isBlocked(int userId, int contactId) throws RemoteException;





    List<Category> getCategories(int userId) throws RemoteException;
    public List<Integer> addCategories(List<Category> categories) throws RemoteException;
    void addContactsToCategory(int categoryId, List<Integer> contactIds) throws RemoteException;
    void removeContactsFromCateogry(int categoryId, List<Integer> contactIds) throws RemoteException;
    List<Integer> getCategoryContacts(int categoryId) throws RemoteException;
    public void renameCategory(int categoryId, String newName) throws RemoteException;
    void removeCategory(int categoryId, int userId) throws RemoteException;
    List<Category> getCategoriesForContact(int userId, int contactId) throws RemoteException;
    Map<Category, List<Integer>> getCategoriesWithContacts(int userId) throws RemoteException;
    void removeCategory(String categoryName, int userId) throws RemoteException;
    String ping() throws RemoteException;

    String getResponse(String userMessage) throws RemoteException;

}
