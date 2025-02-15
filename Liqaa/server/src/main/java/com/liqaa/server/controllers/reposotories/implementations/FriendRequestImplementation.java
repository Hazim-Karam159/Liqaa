package com.liqaa.server.controllers.reposotories.implementations;

import com.liqaa.server.controllers.reposotories.interfaces.FriendRequestInterface;
import com.liqaa.server.util.DatabaseManager;
import com.liqaa.shared.models.entities.FriendRequests;
import com.liqaa.shared.models.enums.FriendRequestStatus;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class FriendRequestImplementation implements FriendRequestInterface {

    private static FriendRequestImplementation FriendRequestImplObject;
    public  static FriendRequestImplementation getContactImplObject()
    {
        if(FriendRequestImplObject==null)
        {
            FriendRequestImplObject=new FriendRequestImplementation();
        }
        return  FriendRequestImplObject;
    }

    private boolean isRequestExists (int senderId , int receiverId)
    {
        // Check if the request already exists
        String query = "SELECT COUNT(*) FROM friendrequests WHERE sender_id = ? AND receiver_id = ?";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setInt(1,senderId);
            statement.setInt(2, receiverId);
            ResultSet rs = statement.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                System.out.println("Friend request already exists between senderId=" + senderId + " and receiverId=" + receiverId);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("request is not created:  " + e.getMessage());
            e.printStackTrace();
        }
     return  false;
    }
    @Override
    public boolean createFriendRequest(FriendRequests request) {
        // Check if the request already exists , or = null
        if(request== null || isRequestExists(request.getSenderId(),request.getReceiverId()))
        {
            //System.out.println("Friend request already exists");
            return false; //
        }
        // Insert the new friend request
        String insertSql = "INSERT INTO friendrequests (sender_id, receiver_id, status, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(insertSql)) {
            statement.setInt(1, request.getSenderId());
            statement.setInt(2, request.getReceiverId());
            statement.setString(3, request.getRequestStatus().toString());
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()) );
            statement.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            int result = statement.executeUpdate(); // This will return 1 if the row is inserted successfully
            if (result == 1) {
                System.out.println("Request inserted: " + result);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("request is not inserted:  " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

@Override
    public boolean deleteFriendRequest(int senderID, int receiverID)
    {
        // Check if the request already exists ,
        if( !(isRequestExists(senderID,receiverID)))
        {
            //System.out.println("Friend request  does not  exist");
            return false; //
        }

        // Delete friend request
        String query = "DELETE FROM friendrequests WHERE sender_id = ? AND receiver_id = ?";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {

                statement.setInt(1, senderID);
                statement.setInt(2, receiverID);
            int result= statement.executeUpdate(); // This will return 1if the row is deleted successfully
            if (result ==1 ) {
                System.out.println("Request Deleted: " + result);
                return true;
            }

        } catch (SQLException e) {
            System.err.println("request is not deleted:  " + e.getMessage());
            e.printStackTrace();
        }

        return false; //not Deleted
    }
    @Override
    public boolean updateFriendRequestStatus(int senderID, int receiverID, FriendRequestStatus newStatus) {
        // Check if the request already exists ,
        if (!isRequestExists(senderID, receiverID)) {
            //System.out.println("Friend request  does not  exist");
            return false; //
        }
        String query = "UPDATE friendrequests SET status = ?, updated_at = ? WHERE sender_id = ? AND receiver_id = ?";
        try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
            statement.setString(1, newStatus.toString());
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()) );
            statement.setInt(3, senderID);
            statement.setInt(4, receiverID);
            int result = statement.executeUpdate(); // This will return 1 if the row is updated successfully
            if (result == 1) {
                System.out.println("Request updated: " + result);
                return true;
            }
        } catch (SQLException e) {
            System.err.println("request is not deleted:  " + e.getMessage());
            e.printStackTrace();
        }
     return  false;
    }
    @Override
    public FriendRequests getFriendRequest(int senderID, int receiverID)
    {
        FriendRequests request =new FriendRequests();
        String query ="Select * from friendrequests where sender_id= ? AND receiver_id =? ";
         if(isRequestExists (senderID,receiverID)) {
             System.out.println("This Request exists ");
             try (PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)) {
                 statement.setInt(1, senderID);
                 statement.setInt(2, receiverID);
                 ResultSet resultSet = statement.executeQuery();
                 if (resultSet.next()) {
                     request.setSenderId(resultSet.getInt("sender_id"));
                     request.setReceiverId(resultSet.getInt("receiver_id"));
                     request.setRequestStatus(FriendRequestStatus.valueOf(resultSet.getString("status").toUpperCase()));
                     request.setCreatedAt(resultSet.getTimestamp("created_at")!= null ? LocalDateTime.from(resultSet.getTimestamp("created_at").toLocalDateTime()) : null);
                     request.setUpdatedAt(resultSet.getTimestamp("updated_at")!= null ? LocalDateTime.from(resultSet.getTimestamp("updated_at").toLocalDateTime()) : null);
                     return request;}
             } catch (SQLException e) {
                 System.err.println("Error when getting the request:  " + e.getMessage());
                 e.printStackTrace();
             }
         }

       return null ;
    }
    @Override
    public List<FriendRequests> getUserFriendRequests(int userID, FriendRequestStatus status)
    {
        List<FriendRequests> requests = new ArrayList<FriendRequests>();
        String query = "SELECT * FROM friendrequests WHERE receiver_id = ? AND status = ?";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)){
            statement.setInt(1,userID);
            statement.setString(2, status.toString());
            ResultSet result = statement.executeQuery();
            while(result.next()){
                FriendRequests request = new FriendRequests(); // Create a new object for each row
                request.setSenderId(result.getInt("sender_id"));
                request.setReceiverId(result.getInt("receiver_id"));
                request.setRequestStatus(FriendRequestStatus.valueOf(result.getString("status").toUpperCase()));
                request.setCreatedAt(result.getTimestamp("created_at")!= null ? LocalDateTime.from(result.getTimestamp("created_at").toLocalDateTime()) : null);
                request.setUpdatedAt(result.getTimestamp("updated_at")!= null ? LocalDateTime.from(result.getTimestamp("updated_at").toLocalDateTime()) : null);
                requests.add(request);
            }
            return requests;
        }catch (SQLException e) {
            e.printStackTrace();

        }return null;
    }
    @Override
    public List<FriendRequests> getUserSentFriendRequests(int userID, FriendRequestStatus status)
    {
        List<FriendRequests> requests = new ArrayList<FriendRequests>();
        String query = "SELECT * FROM friendrequests WHERE sender_id = ? AND status = ?";
        try(PreparedStatement statement = DatabaseManager.getConnection().prepareStatement(query)){
            statement.setInt(1, userID);
            statement.setString(2, status.toString());
            ResultSet result = statement.executeQuery();
            while(result.next()){
                FriendRequests request = new FriendRequests(); // Create a new object for each row
                request.setSenderId(result.getInt("sender_id"));
                request.setReceiverId(result.getInt("receiver_id"));
                request.setRequestStatus(FriendRequestStatus.valueOf(result.getString("status").toUpperCase()));
                request.setCreatedAt(result.getTimestamp("created_at")!= null ? LocalDateTime.from(result.getTimestamp("created_at").toLocalDateTime()) : null);
                request.setUpdatedAt(result.getTimestamp("updated_at")!= null ? LocalDateTime.from(result.getTimestamp("updated_at").toLocalDateTime()) : null);
                requests.add(request);
            }
            return requests;
        }catch (SQLException e) {
            e.printStackTrace();

        }
        return null;
    }
}
