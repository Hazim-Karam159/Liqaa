package com.liqaa.client.controllers.services.interfaces;

import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Announcement;
import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.Notification;
import com.liqaa.shared.models.entities.User;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface NotificationService {
    List<Notification> getAllNotifications(int userId) throws SQLException, RemoteException;
    User getNotificationSenderData(int senderId) throws RemoteException;
    AnnouncementNotification getAnnouncementById(int notificationId) throws SQLException, RemoteException;
    List<Notification> getAllAcceptedInvitations(int userId) throws SQLException, RemoteException;
    List<Notification> getAllDeclinedInvitations(int userId) throws SQLException, RemoteException;
    List<Notification> getAllFriendRequests(int userId) throws SQLException, RemoteException;
    List<Notification> getAnnouncements(int userId) throws SQLException, RemoteException;
    Boolean deleteNotification(int notificationId)  throws SQLException, RemoteException;
    Boolean addToContactList(int userId, int newContactId) throws RemoteException;
    ChatInfo createDirectConversation(int userId, User otherUserId) throws RemoteException;
    User getUserInfoById(int userId) throws RemoteException;
    boolean addContact (int userId, int contactId) throws RemoteException;
    boolean createNotification(Notification notification) throws SQLException, RemoteException;
}
