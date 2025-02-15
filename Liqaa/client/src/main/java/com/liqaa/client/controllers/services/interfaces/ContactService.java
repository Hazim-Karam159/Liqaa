package com.liqaa.client.controllers.services.interfaces;

import com.liqaa.shared.models.entities.*;
import javafx.collections.ObservableList;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ContactService {
    ObservableList<User> getContacts(int userId);

    ObservableList<Category> getCategoriesForContact(int userId, int contactId);

    boolean deleteContact(int userId, int contactId);

    boolean renameCategory(int categoryId, String newName);

    List<User> getUserFriends(int userId) throws RemoteException;
    List<Category> getUserCategories(int userId) throws RemoteException;
    boolean addContact (int userId, int contactId) throws RemoteException;
    void removeCategory(int categoryId, int userId) throws RemoteException;
    void createGroup(Group group, ArrayList<Integer> groupMembers) throws RemoteException;
    User getUserInfo(String userPhone) throws RemoteException;
    List<Integer> addCategories(List<Category> categories) throws RemoteException;
    List<Category> getCategories(int userId) throws RemoteException;
    boolean blockContact (int userId , int contactId) throws RemoteException;
    boolean unblockContact (int userId , int contactId) throws RemoteException;
    boolean isBlocked(int userId, int contactId) throws RemoteException;
    void removeCategory(String categoryName, int userId) throws RemoteException;
    boolean sendFriendRequest( FriendRequests addRequests) throws RemoteException;
    boolean createNotification(Notification notification) throws SQLException, RemoteException;
}