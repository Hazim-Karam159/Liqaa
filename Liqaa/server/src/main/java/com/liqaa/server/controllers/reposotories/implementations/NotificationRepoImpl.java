package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.NotificationRepo;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.AnnouncementNotification;
import com.liqaa.shared.models.entities.Notification;
import com.liqaa.shared.models.enums.NotificationType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationRepoImpl implements NotificationRepo {

    @Override
    public boolean createNotification(Notification notification) throws SQLException {
        if (notification == null) {
            System.err.println("Error creating notification: Notification is null");
            return false;
        }
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "INSERT INTO notifications (recipient_id, sender_id, announcement_id, type, is_read) VALUES (?, ?, ?, ?, ?)";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, notification.getRecipientId());
                statement.setObject(2, (notification.getSenderId() == 0 ? null : notification.getSenderId()));
                statement.setObject(3, (notification.getAnnouncementId() == 0 ? null : notification.getAnnouncementId()));
                statement.setString(4, notification.getType().toString());
                statement.setBoolean(5, notification.isRead());
                int rowsAffected = statement.executeUpdate();
                if(rowsAffected <= 0){
                    System.err.println("Failed to create notification");
                    return false;
                }
                else{
                    System.out.println("notification is created successfully");
                    return true;
                }
            }
        }
    }

    @Override
    public Notification getNotificationById(int id) throws SQLException {
        if(id <= 0) {
            System.err.println("Error getting notification: Invalid ID");
            return null;
        }
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery();) {
                    if (resultSet.next()) {
                        return new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                    }
                    return null;
                }
            }
        }
    }

    @Override
    public List<Notification> getAllNotifications() throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public boolean updateNotification(Notification notification) throws SQLException {
        if (notification == null) {
            System.err.println("Error updating notification: Notification is null");
            return false;
        }
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "UPDATE notifications SET recipient_id = ?, sender_id = ?, announcement_id = ?, type = ?, is_read = ? WHERE id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, notification.getRecipientId());
                statement.setInt(2, notification.getSenderId());
                statement.setInt(3, notification.getAnnouncementId());
                statement.setString(4, String.valueOf(notification.getType()));
                statement.setBoolean(5, notification.isRead());
                statement.setInt(6, notification.getId());
                if (statement.executeUpdate() <= 0) { /** executeUpdate() returns the number of rows be updated successfully */
                    System.err.println("Failed to update notification");
                } else {
                    System.out.println("notification is updated successfully");
                }
                return (statement.executeUpdate() > 0);
            }
        }
    }

    @Override
    public boolean deleteNotification(int id) throws SQLException {
        if (id <= 0) {
            System.err.println("Error deleting notification: Invalid ID");
            return false;
        }
        try (Connection connection = DatabaseManager.getConnection();) {
            String query = "DELETE FROM notifications WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                int rowAffected = statement.executeUpdate();
                if (rowAffected <= 0) {
                    System.err.println("Failed to delete notification");
                    return false;
                } else {
                    System.out.println("notification is deleted successfully");
                }
                return true;
            }
        }
    }

    @Override
    public List<Notification> getAllAcceptedRequests() throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'REQUEST_ACCEPTED'";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getAllDeclinedRequests() throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'REQUEST_DECLINED'";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getUserNotifications(int userId) throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, userId);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getAllFriendRequests() throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'FRIEND_REQUEST'";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getAllAnnouncements() throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'ANNOUNCEMENT'";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    // for specific user:
    @Override
    public List<Notification> getUserFriendRequests(int recipientId) throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'FRIEND_REQUEST' AND recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, recipientId);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getAcceptedInvitations(int id) throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'REQUEST_ACCEPTED' AND recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getDeclinedInvitations(int id) throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'REQUEST_DECLINED' AND recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<Notification> getAnnouncements(int id) throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE type = 'ANNOUNCEMENT' AND recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public List<AnnouncementNotification> getAnnouncementNotifications() throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT n.id, a.title, a.content, n.is_read AS isRead, n.sent_at AS sentAt " +
                    "FROM Notifications n " +
                    "JOIN Announcements a ON n.announcement_id = a.id " +
                    "WHERE n.type = 'Announcement'";
            try(PreparedStatement statement = connection.prepareStatement(query);){
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<AnnouncementNotification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        AnnouncementNotification notification = new AnnouncementNotification(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("content"), resultSet.getBoolean("isRead"), resultSet.getTimestamp("sentAt").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }

    @Override
    public AnnouncementNotification getAnnouncementNotificationsById(int notificationId) throws SQLException{
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT n.id, a.title, a.content, n.is_read AS isRead, n.sent_at AS sentAt " +
                    "FROM Notifications n " +
                    "JOIN Announcements a ON n.announcement_id = a.id " +
                    "WHERE n.type = 'Announcement' AND n.id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);){
                statement.setInt(1, notificationId);
                try(ResultSet resultSet = statement.executeQuery();) {
                    if (resultSet.next()) {
                        return new AnnouncementNotification(resultSet.getInt("id"), resultSet.getString("title"), resultSet.getString("content"), resultSet.getBoolean("isRead"), resultSet.getTimestamp("sentAt").toLocalDateTime());
                    }
                    return null;
                }
            }
        }
    }

    @Override
    public List<Notification> getUnreadNotifications(int id) throws SQLException {
        try(Connection connection = DatabaseManager.getConnection();) {
            String query = "SELECT id, recipient_id, sender_id, announcement_id, type, is_read, sent_at FROM notifications WHERE is_read = FALSE AND recipient_id = ?";
            try(PreparedStatement statement = connection.prepareStatement(query);) {
                statement.setInt(1, id);
                try(ResultSet resultSet = statement.executeQuery();) {
                    List<Notification> notifications = new ArrayList<>();
                    while (resultSet.next()) {
                        Notification notification = new Notification(resultSet.getInt("id"), resultSet.getInt("recipient_id"), resultSet.getInt("sender_id"), resultSet.getInt("announcement_id"), NotificationType.valueOf(resultSet.getString("type")), resultSet.getBoolean("is_read"), resultSet.getTimestamp("sent_at").toLocalDateTime());
                        notifications.add(notification);
                    }
                    return notifications;
                }
            }
        }
    }
}