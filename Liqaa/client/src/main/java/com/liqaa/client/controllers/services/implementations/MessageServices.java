package com.liqaa.client.controllers.services.implementations;

import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.models.entities.FileMessage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.util.AlertNotifier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.util.List;

public class MessageServices {

    private static MessageServices instance;
    private MessageServices() {}

    public static synchronized MessageServices getInstance()
    {
        if (instance == null) {
            instance = new MessageServices();
        }
        return instance;
    }

    public void loadMessages(int conversationId)
    {
        try {
            List<Message> messageList = ServerConnection.getServer().getMessagesByConversationId(conversationId);
            System.out.println("Messages: " + messageList);
            Platform.runLater(() ->
                    DataCenter.getInstance().getMessages().setAll(messageList)
            );
        } catch (RemoteException | SQLException e) {
            handleError("Error loading messages: ", (RemoteException) e);
        }
    }

    public void loadPaginatedMessages(int conversationId, int limit, int offset) {
        try {
            List<Message> messageList = ServerConnection.getServer().getMessagesByConversationId(conversationId, offset, limit);
            Platform.runLater(() ->
                    DataCenter.getInstance().getMessages().addAll(messageList)
            );
        } catch (RemoteException | SQLException e) {
            handleError("Error loading paginated messages: ", (RemoteException) e);
        }
    }

    public int sendMessage(Message message)
    {
        int id =0;
        try {
           id =ServerConnection.getServer().sendMessage(message);
            Platform.runLater(() ->
                    DataCenter.getInstance().getMessages().add(message)
            );
        } catch (RemoteException | SQLException e) {
            handleError("Error sending message: ", (RemoteException) e);
        }
        return id;
    }
    public int sendFile(FileMessage fileInfo, Message message)
    {
        int fileId =0, messageId=0;
        try {
            messageId =ServerConnection.getServer().sendMessage(message);
            if (messageId != 0)
            {
                message.setId(messageId);
                System.out.println("Message ID: " + messageId);
                fileInfo.setMessageId(messageId);
                fileId = ServerConnection.getServer().sendFile(fileInfo);
                if(fileId == 0)
                    ServerConnection.getServer().deleteMessage(messageId);
                else
                {
                    Platform.runLater(() ->
                            DataCenter.getInstance().getMessages().add(message)
                    );
                }
            }
            AlertNotifier.createAlert("Error", "Error sending File",null );

        } catch (RemoteException | SQLException e)
        {
            handleError("Error sending File: ", (RemoteException) e);
        }
        return fileId;
    }

    public void getUnreadMessageCount(int conversationId, int userId, UnreadCountCallback callback)
    {
        try {
            int count = ServerConnection.getServer().getUnreadMessageCount(conversationId, userId);
            Platform.runLater(() ->
                    callback.onUnreadCountReceived(count)
            );
        } catch (RemoteException | SQLException e) {
            handleError("Error getting unread message count: ", (RemoteException) e);
        }
    }

    public void markMessagesAsSeen(int conversationId)
    {
        try {
            ServerConnection.getServer().markMessagesAsSeen(conversationId, DataCenter.getInstance().getcurrentUserId());
            ServerConnection.getServer().markConversationAsRead(DataCenter.getInstance().getcurrentUserId(), conversationId);
            Platform.runLater(() ->
                    DataCenter.getInstance().getChats().stream()
                            .filter(chatInfo -> chatInfo.getConversationId() == DataCenter.getInstance().getCurrentConversationId())
                            .forEach(chatInfo -> chatInfo.setUnreadMsgCount(0)));
        } catch (RemoteException | SQLException e) {
            handleError("Error marking messages as seen: ", (RemoteException) e);
        }
    }

    public void getFileInfo(int messageId, FileInfoCallback callback)
    {
        try {
            FileMessage fileInfo = ServerConnection.getServer().getFileInfo(messageId);
            Platform.runLater(() ->
                    callback.onFileInfoReceived(fileInfo)
            );
        } catch (RemoteException | SQLException e) {
            handleError("Error getting file info: ", (RemoteException) e);
        }
    }

    private void handleError(String message, RemoteException e)
    {
        System.err.println(message + e.getMessage());
        e.printStackTrace();
        AlertNotifier.createAlert("Error", message, e.getMessage());
    }

    // Callback interfaces for asynchronous operations
    public interface UnreadCountCallback {
        void onUnreadCountReceived(int count);
    }

    public interface FileInfoCallback {
        void onFileInfoReceived(FileMessage fileInfo);
    }

    public FileMessage getFileInfo(int messageId) {
        try {
            return ServerConnection.getServer().getFileInfo(messageId);
        } catch (SQLException e) {
            System.err.println("Error in getFileInfo: " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Error in getFileInfo: " + e.getMessage());
        }
        return null;
    }

    public boolean uploadFile(FileMessage fileMessage, Message message, byte[] fileData, int conversationId)
    {
        try {
            return ServerConnection.getServer().uploadFile(
                    fileData,
                    fileMessage.getFileName(),
                    conversationId,
                    message.getId()
            );
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

        public boolean downloadFile(int messageId, int conversationId, String savePath) {
            try {
                byte[] fileData = ServerConnection.getServer().downloadFile(
                        messageId,
                        conversationId
                );

                FileMessage metadata = ServerConnection.getServer().getFileInfo(messageId);
                Path path = Paths.get(savePath, metadata.getFileName());
                Files.write(path, fileData);
                return true;
            } catch (IOException | SQLException e) {
                handleError("Download failed", (RemoteException) e);
                return false;
            }
        }
}

