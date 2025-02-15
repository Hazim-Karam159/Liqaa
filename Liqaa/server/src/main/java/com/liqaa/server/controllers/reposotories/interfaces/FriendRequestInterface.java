package com.liqaa.server.controllers.reposotories.interfaces;
import com.liqaa.shared.models.entities.FriendRequests;
import com.liqaa.shared.models.enums.FriendRequestStatus;

import java.util.List;

public interface FriendRequestInterface {


    public boolean createFriendRequest(FriendRequests request);
    public boolean deleteFriendRequest(int senderID, int receiverID);
    public boolean updateFriendRequestStatus(int senderID, int receiverID, FriendRequestStatus newStatus);
   public FriendRequests getFriendRequest(int senderID, int receiverID);
    public List<FriendRequests> getUserFriendRequests(int userID, FriendRequestStatus status);
    public List<FriendRequests> getUserSentFriendRequests(int userID, FriendRequestStatus status);

}
