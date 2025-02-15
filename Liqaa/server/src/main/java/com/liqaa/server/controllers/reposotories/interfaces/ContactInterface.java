package com.liqaa.server.controllers.reposotories.interfaces;
import com.liqaa.shared.models.entities.Contacts;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.User;

import java.util.List;

public interface ContactInterface {


    public boolean isContact(int userId, int contactId) ;
    public boolean createContact(Contacts contact) ;
    public boolean deleteById(int userId, int contactId);
    public  boolean blockContact(int userId, int contactId) ;
    public  boolean unblockContact(int userId, int contactId);
    public  List<Contacts> getAllContacts (int id);
    public User getContact(String DisplayName);
    public List<User> getUserFriends(int userID);

}
