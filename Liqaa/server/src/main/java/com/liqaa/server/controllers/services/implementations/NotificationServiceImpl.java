package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.NotificationRepoImpl;
import com.liqaa.server.controllers.services.interfaces.NotificationService;
import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.Notification;

import java.sql.SQLException;
import java.util.List;

public class NotificationServiceImpl implements NotificationService {
    private static NotificationServiceImpl instance;
    private NotificationRepoImpl notificationRepoImpl = new NotificationRepoImpl();

    private NotificationServiceImpl() {
        // private constructor to prevent instantiation
    }

    public static synchronized NotificationServiceImpl getInstance() {
        if (instance == null) {
            instance = new NotificationServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean createNotification(Notification notification) throws SQLException{
        return notificationRepoImpl.createNotification(notification);
    }

    @Override
    public List<Notification> getUserNotifications(int userId) throws SQLException {
        return notificationRepoImpl.getUserNotifications(userId);
    }

    @Override
    public List<Notification> getUpcomingRequests(int recipientId) throws SQLException {
        return notificationRepoImpl.getUserFriendRequests(recipientId);
    }

    @Override
    public List<Notification> getAcceptedInvitations(int recipientId) throws SQLException {
        return notificationRepoImpl.getAcceptedInvitations(recipientId);
    }

    @Override
    public List<Notification> getDeclinedInvitations(int recipientId) throws SQLException {
        return notificationRepoImpl.getDeclinedInvitations(recipientId);
    }

    @Override
    public List<Notification> getAnnouncements(int recipientId) throws SQLException{
        return notificationRepoImpl.getAnnouncements(recipientId);
    }

    @Override
    public List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException{
        return notificationRepoImpl.getAnnouncementNotifications();
    }

    @Override
    public AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException{
        return notificationRepoImpl.getAnnouncementNotificationsById(notificationId);
    }

    @Override
    public List<Notification> getUnreadNotifications(int recipientId) throws SQLException {
        return notificationRepoImpl.getUnreadNotifications(recipientId);
    }

    @Override
    public boolean sendNotification(Notification notification) throws SQLException{
        return notificationRepoImpl.createNotification(notification);
    }

    @Override
    public boolean deleteNotification(int id) throws SQLException{
        return notificationRepoImpl.deleteNotification(id);
    }

    @Override
    public void markAllAsRead(int recipientId) throws SQLException{
        List<Notification> notifications = notificationRepoImpl.getAllNotifications();
        for (Notification notification : notifications){
            notification.setRead(true);
        }
    }
}