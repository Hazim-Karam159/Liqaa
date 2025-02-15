package com.liqaa.server.controllers.services.interfaces;

import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.FriendRequests;
import com.liqaa.shared.models.entities.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationService {
    boolean createNotification(Notification notification) throws SQLException;
    List<Notification> getUserNotifications(int userId) throws SQLException;
    List<Notification> getUpcomingRequests(int recipientId) throws SQLException;
    List<Notification> getAcceptedInvitations(int recipientId) throws SQLException;
    List<Notification> getDeclinedInvitations(int recipientId) throws SQLException;
    List<Notification> getAnnouncements(int recipientId) throws SQLException;
    List<Notification> getUnreadNotifications(int recipientId) throws SQLException;
    boolean sendNotification(Notification notification) throws SQLException;
    List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException;
    AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException;
    boolean deleteNotification(int id) throws SQLException;
    void markAllAsRead(int recipientId) throws SQLException;
}