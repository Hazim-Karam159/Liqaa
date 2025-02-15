package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.services.interfaces.ContactServicesInt;
import com.liqaa.shared.models.entities.Contacts;
import com.liqaa.shared.models.entities.FriendRequests;
import com.liqaa.shared.models.entities.User;
import com.liqaa.server.controllers.reposotories.implementations.ContactImplementation;
import com.liqaa.server.controllers.reposotories.implementations.FriendRequestImplementation;
import com.liqaa.shared.models.enums.FriendRequestStatus;

import java.util.List;

public class ContactServicesImpl implements ContactServicesInt {

    private static ContactServicesImpl contactServicesImpl;
    private User user;
    private ContactServicesImpl()
    {
    }
    public static ContactServicesImpl getInstance()
    {
        if(contactServicesImpl==null)
        {
            contactServicesImpl=new ContactServicesImpl();
        }
        return  contactServicesImpl;
    }

    public User getContact(String name)// throws null exception
    {
        user=ContactImplementation.getContactImplObject().getContact(name);
        return user;
    }
    public List<User> getUserFriends(int userID)//list of Contacts info
    {
        List<User> UserFriends = ContactImplementation.getContactImplObject().getUserFriends(userID);
        return  UserFriends;
    }
    public List<FriendRequests> getUserFriendRequests(int userID)
    {

        // requests of user sent or received >> pending
        List<FriendRequests> friendRequestlist=FriendRequestImplementation.getContactImplObject().getUserFriendRequests(userID, FriendRequestStatus.PENDING);
        //List<FriendRequests> friendRequestSentlist=FriendRequestImplementation.getContactImplObject().getUserSentFriendRequests(userID, FriendRequestStatus.PENDING);
        //friendRequestlist.addAll(friendRequestSentlist);
        return friendRequestlist;
    }
    @Override
    public boolean updateFriendsRequestStatus( FriendRequests updatedRequest)//Accepted>>add contact
    {
        // public boolean updateFriendRequestStatus(int senderID, int receiverID, FriendRequestStatus newStatus)
        if (updatedRequest.getRequestStatus()==FriendRequestStatus.REJECTED)
        {
            ////rejected>>remove request
           if( FriendRequestImplementation.getContactImplObject().updateFriendRequestStatus(updatedRequest.getSenderId(),updatedRequest.getReceiverId(),updatedRequest.getRequestStatus())) {
               FriendRequestImplementation.getContactImplObject().deleteFriendRequest(updatedRequest.getSenderId(), updatedRequest.getReceiverId());
               return true;
           }
        }
        else if(updatedRequest.getRequestStatus()==FriendRequestStatus.ACCEPTED) {
            // if Accepted >> create a new contact
            //sender will be a friend to receiver "this user"
            if( FriendRequestImplementation.getContactImplObject().updateFriendRequestStatus(updatedRequest.getSenderId(),updatedRequest.getReceiverId(),updatedRequest.getRequestStatus())) {
            Contacts contact=new Contacts(updatedRequest.getReceiverId(),updatedRequest.getSenderId(),false);
            ContactImplementation.getContactImplObject().createContact(contact);
            return true;
            }
        }
       return false;
    }
    @Override
    public boolean sendFriendRequest( FriendRequests addRequests)
    {
        return FriendRequestImplementation.getContactImplObject().createFriendRequest(addRequests);
    }
    public  boolean addContact (int userId , int contactId)
    {
        Contacts contact=new Contacts(userId,contactId,false);
        return  ContactImplementation.getContactImplObject().createContact(contact);

    }
   @Override
    public  boolean deleteContact (int userId , int contactId)
    {
        return ContactImplementation.getContactImplObject().deleteById(userId,contactId);
    }
    public boolean blockContact (int userId , int contactId)
    {
         return  ContactImplementation.getContactImplObject().blockContact(userId,contactId);
    }
    public  boolean unblockContact (int userId , int contactId)
    {
        return  ContactImplementation.getContactImplObject().unblockContact(userId,contactId);
    }

    @Override
    public boolean isBlocked(int userId, int contactId) {
        return ContactImplementation.getContactImplObject().isBlocked(userId, contactId);
    }
}