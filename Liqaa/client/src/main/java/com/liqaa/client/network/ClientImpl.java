package com.liqaa.client.network;


import com.liqaa.client.controllers.FXMLcontrollers.PrimaryController;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.shared.models.Contact;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.network.Client;
import javafx.application.Platform;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Optional;


public class ClientImpl extends UnicastRemoteObject implements Client
{
    static private Client client;

    public static Client getClient() {
        try {
            if (client == null)
                client = new ClientImpl();
            return client;
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    protected ClientImpl() throws RemoteException
    {

    }

    @Override
    public void receiveMessage(Message message) throws RemoteException {
        Platform.runLater(() -> {
            // Update messages list if viewing the conversation
            if (DataCenter.getInstance().getCurrentConversationId() == message.getConversationId()) {
                DataCenter.getInstance().getMessages().add(message);
//                PrimaryController.getInstance().scrollDown();
            }

            // Update chat list preview
            updateChatListPreview(message);
        });
    }

    @Override
    public void updateContactStatus(int contactId, CurrentStatus status) throws RemoteException {
        Platform.runLater(() -> {
            DataCenter.getInstance().getChats().forEach(chat -> {
                if (chat.getRecipientId() == contactId) {
                    chat.setStatus(status);
                }
            });
        });
    }

    @Override
    public void addNewConversation(ChatInfo chat) throws RemoteException {
        Platform.runLater(() -> {
            if (!DataCenter.getInstance().getChats().contains(chat)) {
                DataCenter.getInstance().getChats().add(0, chat);
            }
        });
    }

    @Override
    public void updateContactsList(Contact contact) throws RemoteException {
        Platform.runLater(() -> {
            if (!DataCenter.getInstance().getOriginalContactsList().contains(contact)) {
                DataCenter.getInstance().getOriginalContactsList().add(contact);
            }
        });
    }

    private void updateChatListPreview(Message message) {
        Optional<ChatInfo> chatOpt = DataCenter.getInstance().getChats().stream()
                .filter(c -> c.getConversationId() == message.getConversationId())
                .findFirst();

        if (chatOpt.isPresent()) {
            ChatInfo chat = chatOpt.get();
            chat.setLastMsgTime(message.getSentAt());
//            chat.setLastMessage(message.getContent());
            chat.setUnreadMsgCount(chat.getUnreadMsgCount() + 1);

            // Move to top of list
            DataCenter.getInstance().getChats().remove(chat);
            DataCenter.getInstance().getChats().add(0, chat);
        }
    }

    @Override
    public void updateUserStatus(int userId, CurrentStatus status) throws RemoteException {
        Platform.runLater(() -> {
            DataCenter.getInstance().getChats().stream()
                    .filter(chat -> chat.getRecipientId() == userId)
                    .forEach(chat -> chat.setStatus(status));
        });
    }

}