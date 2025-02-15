package com.liqaa.client.controllers.services.implementations;

import com.liqaa.client.controllers.services.interfaces.NotificationService;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Announcement;
import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.Notification;
import com.liqaa.shared.models.entities.User;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private static NotificationServiceImpl instance;

    public static synchronized NotificationServiceImpl getInstance() {
        if (instance == null) {
            instance = new NotificationServiceImpl();
        }
        return instance;
    }

    @Override
    public List<Notification> getAllNotifications(int userId) throws SQLException, RemoteException {
        return ServerConnection.getServer().getUserNotifications(userId);
    }

    @Override
    public User getNotificationSenderData(int senderId) throws RemoteException{
        return ServerConnection.getServer().getUserInfoById(senderId);
    }

    @Override
    public AnnouncementNotification getAnnouncementById(int notificationId) throws SQLException, RemoteException{
        return ServerConnection.getServer().getAnnouncementNotificationsById(notificationId);
    }

    @Override
    public List<Notification> getAllAcceptedInvitations(int userId) throws SQLException, RemoteException{
        return ServerConnection.getServer().getAcceptedInvitations(userId);
    }

    @Override
    public List<Notification> getAllDeclinedInvitations(int userId) throws SQLException, RemoteException{
        return ServerConnection.getServer().getDeclinedInvitations(userId);
    }

    @Override
    public List<Notification> getAllFriendRequests(int userId) throws SQLException, RemoteException{
        return ServerConnection.getServer().getUpcomingRequests(userId);
    }

    @Override
    public List<Notification> getAnnouncements(int userId) throws SQLException, RemoteException{
        return ServerConnection.getServer().getAnnouncements(userId);
    }

    @Override
    public Boolean deleteNotification(int notificationId)  throws SQLException, RemoteException{
        return ServerConnection.getServer().deleteNotification(notificationId);
    }

    @Override
    public Boolean addToContactList(int userId, int newContactId) throws RemoteException{
        return ServerConnection.getServer().addContact(userId, newContactId);
    }

    @Override
    public ChatInfo createDirectConversation(int userId, User otherUserId) throws RemoteException{
        return ServerConnection.getServer().createDirectConversation(DataCenter.getInstance().getcurrentUserId(), otherUserId);
    }

    @Override
    public User getUserInfoById(int userId) throws RemoteException{
        return ServerConnection.getServer().getUserInfoById(userId);
    }

    @Override
    public boolean addContact (int userId, int contactId) throws RemoteException{
        return ServerConnection.getServer().addContact(userId, contactId);
    }

    @Override
    public boolean createNotification(Notification notification) throws SQLException, RemoteException{
        return ServerConnection.getServer().createNotification(notification);
    }
}