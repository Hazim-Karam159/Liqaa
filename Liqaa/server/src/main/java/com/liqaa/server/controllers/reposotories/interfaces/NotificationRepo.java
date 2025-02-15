package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.Notification;

import java.sql.SQLException;
import java.util.List;

public interface NotificationRepo {
    boolean createNotification(Notification notification) throws SQLException;
    Notification getNotificationById(int id) throws SQLException;
    List<Notification> getAllNotifications() throws SQLException;
    boolean updateNotification(Notification notification) throws SQLException;
    boolean deleteNotification(int id) throws SQLException;
    List<Notification> getAllFriendRequests() throws SQLException;
    List<Notification> getAllAcceptedRequests() throws SQLException;
    List<Notification> getAllDeclinedRequests() throws SQLException;
    List<Notification> getAllAnnouncements() throws SQLException;
    // for specific user
    List<Notification> getUserNotifications(int userId) throws SQLException;
    List<Notification> getUserFriendRequests(int recipientId) throws SQLException;
    List<Notification> getAcceptedInvitations(int id) throws SQLException; // receiverId = id
    List<Notification> getDeclinedInvitations(int id) throws SQLException; // receiverId = id
    List<Notification> getAnnouncements(int id) throws SQLException;
    List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException;
    AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException;
    List<Notification> getUnreadNotifications(int id) throws SQLException; // receiverId = id
}