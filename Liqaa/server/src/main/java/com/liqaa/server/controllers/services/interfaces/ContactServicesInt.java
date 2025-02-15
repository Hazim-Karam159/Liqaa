package com.liqaa.server.controllers.services.interfaces;

import com.liqaa.shared.models.entities.FriendRequests;
import com.liqaa.shared.models.entities.User;

import java.util.List;

public interface ContactServicesInt {

    public User getContact(String name); ///////
    public List<User> getUserFriends(int userID); //get Contacts
    public List<FriendRequests> getUserFriendRequests(int userID); // when calling , check if empty list or not
    public boolean updateFriendsRequestStatus( FriendRequests updatedRequest);//rejected>>remove request , Accepted>>add contact
    public boolean sendFriendRequest( FriendRequests addRequests);
    public  boolean addContact (int userId , int contactId);
    public boolean deleteContact (int userId , int contactId);
    public  boolean blockContact (int userId , int contactId);
    public  boolean unblockContact (int userId , int contactId);

    boolean isBlocked(int userId, int contactId);
}
