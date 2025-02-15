package com.liqaa.server.controllers.reposotories.implementations;
import com.liqaa.server.controllers.reposotories.interfaces.ContactInterface;
import com.liqaa.shared.models.entities.Contacts;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.models.enums.Gender;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ContactImplementation implements ContactInterface {

    private static ContactImplementation ContactImplObject;
    public  static ContactImplementation getContactImplObject()
    {
        if(ContactImplObject==null)
        {
            ContactImplObject=new ContactImplementation();
        }
        return  ContactImplObject;
    }

    public boolean isBlocked(int userId, int contactId) {
        boolean isBlocked = false;
        String query = "SELECT is_blocked FROM contacts WHERE user_id = ? AND contact_id = ?";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                isBlocked = resultSet.getBoolean("is_blocked");
            }
        } catch (SQLException e) {
            System.err.println("Error checking if contact is blocked: " + e.getMessage());
            e.printStackTrace();
        }
        return isBlocked;
    }

    @Override
    public boolean isContact(int userId, int contactId)
    {
        boolean exists=false;
        String query= "SELECT EXISTS (SELECT 1 FROM contacts WHERE user_id = ? AND contact_id = ?)";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
                statement.setInt(1, userId);
                statement.setInt(2, contactId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                     exists = resultSet.getBoolean(1);
                    System.out.println("Record exists: " + exists);}

        }catch (SQLException e)
        {
            System.err.println("Is contact throws Exception: " + e.getMessage());
            e.printStackTrace();
        }
        return exists;
    }
    @Override
    public User getContact(String DisplayName )
    {
        User user=new User();
        String query="SELECT users.id,users.name,users.phone_number,users.bio,users.current_status ,users.is_active ,users.profile_picture FROM  users  JOIN  contacts ON users.id = contacts.contact_id WHERE users.name = ? ";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
            statement.setString(1, DisplayName);
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
                user.setId(result.getInt("id"));
                user.setDisplayName(result.getString("name"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setBio(result.getString("bio"));
                user.setCurrentstatus(CurrentStatus.valueOf(result.getString("current_status").toUpperCase()));
                user.setIsActive(result.getBoolean("is_active"));
                Blob profilePhotoBlob = result.getBlob("profile_picture");
                if (profilePhotoBlob != null) {
                    int blobLength = (int) profilePhotoBlob.length();
                    byte[] profilePhotoBytes = profilePhotoBlob.getBytes(1, blobLength);
                    user.setProfilepicture(profilePhotoBytes);
                }
                else {
                    user.setProfilepicture(null);
                }
                return user;
            }
        }catch (SQLException e)
        {
            System.err.println("Error when get Contact info " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public  List<User> getUserFriends(int userID)
    {
        // Specific info about friend>> name, phone, status, bio, image,is active
        List<User> userFriends= new ArrayList<>();
        String query="SELECT users.id, users.name,users.phone_number,users.bio,users.current_status ,users.is_active ,users.profile_picture FROM  users  INNER JOIN contacts ON users.id = contacts.contact_id WHERE contacts.user_id = ? ";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query))
        {
            statement.setInt(1, userID);
            ResultSet result = statement.executeQuery();
            while (result.next())
            {
                User user=new User();
                user.setId(result.getInt("id"));
                user.setDisplayName(result.getString("name"));
                user.setPhoneNumber(result.getString("phone_number"));
                user.setBio(result.getString("bio"));
                user.setCurrentstatus(CurrentStatus.valueOf(result.getString("current_status").toUpperCase()));
                user.setIsActive(result.getBoolean("is_active"));
                Blob profilePhotoBlob = result.getBlob("profile_picture");
                if (profilePhotoBlob != null) {
                    int blobLength = (int) profilePhotoBlob.length();
                    byte[] profilePhotoBytes = profilePhotoBlob.getBytes(1, blobLength);
                    user.setProfilepicture(profilePhotoBytes);
                }
                else {
                    user.setProfilepicture(null);
                }
               userFriends.add(user);
            }
        }catch (SQLException e)
        {
            System.err.println("Error when get Contact info " + e.getMessage());
            e.printStackTrace();
        }
        return userFriends;
    }
    @Override
    public  boolean createContact (Contacts contact)
    {
        //isContact(int userId, int contactId)
        if(isContact(contact.getUserId(),contact.getContactId()))
        {
            System.out.println("Contact already exists");
            return false;
        }
        String query = "INSERT INTO contacts (user_id, contact_id,  is_blocked) VALUES (?, ?, ?)";
        try(Connection connection = DatabaseManager.getConnection();
        PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, contact.getUserId());
            statement.setInt(2, contact.getContactId());
            statement.setBoolean(3, contact.getIsBlocked());
            System.out.println("Contact created successfully");
            if (statement.executeUpdate() == 1) // if it is created it will return 1
                return true;
            return false; // // Error occurred during creation
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deleteById(int userId, int contactId)
    {
        if (!isContact(userId, contactId))
        {
            System.out.println("Contact does not exist");
            return false;
        }
        String query = "DELETE FROM contacts WHERE user_id = ? AND contact_id = ?";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            System.out.println("Contact deleted successfully");
            if(statement.executeUpdate()==1)
            {
                return  true;
            }
        }catch(SQLException e)
        {
            System.err.println("Error Deleting contact: " + e.getMessage());
            e.printStackTrace();
        }
        return  false;
    }

    @Override
    public boolean blockContact(int userId, int contactId) {
        if (!isContact(userId, contactId)) {
            System.out.println("Contact does not exist");
            return false; // Contact does not exist
        }
        String query = "UPDATE contacts SET is_blocked = true WHERE user_id = ? AND contact_id = ?";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Contact blocked successfully");
                return true; // Contact blocked successfully
            }
        } catch (SQLException e) {
            System.err.println("Error blocking contact: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Error occurred during update
    }

    @Override
    public  boolean unblockContact(int userId, int contactId)
    {
        if (!isContact(userId, contactId)) {
            System.out.println("Contact does not exist");
            return false; // Contact does not exist
        }
        String query = "UPDATE contacts SET is_blocked = false WHERE user_id = ? AND contact_id = ?";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, contactId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Contact un blocked successfully");
                return true; // Contact unblocked successfully
            }
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return false; // Error occurred during update
    }
    @Override
    public List<Contacts> getAllContacts (int id)
    {
        String query = "SELECT * FROM contacts WHERE user_id = ?";
        List<Contacts> contactsList = new ArrayList<>();
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt("user_id");
                    int contactId = rs.getInt("contact_id");
                   Date createdAt = rs.getObject("created_at", Date.class);
                    boolean isBlocked = rs.getBoolean("is_blocked");
                    contactsList.add(new Contacts(userId, contactId, createdAt, isBlocked));

            }
        }
        catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
        return contactsList;
    }

}
