package com.liqaa.shared.network;


import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import com.liqaa.shared.models.Contact;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.network.Server;
import com.liqaa.shared.network.Client;


public interface Client extends Remote {


    void receiveMessage(Message message) throws RemoteException;

    void updateContactStatus(int contactId, CurrentStatus status) throws RemoteException;

    void addNewConversation(ChatInfo chat) throws RemoteException;

    void updateContactsList(Contact contact) throws RemoteException;

    void updateUserStatus(int userId, CurrentStatus status) throws RemoteException;
}