package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.liqaa.shared.models.enums.FriendRequestStatus;

public class FriendRequests implements Serializable {

    private int senderId;
    private int receiverId;
    private FriendRequestStatus requestStatus = FriendRequestStatus.PENDING; // Default value
    private  LocalDateTime createdAt;
    private LocalDateTime  updatedAt;

    // Constructor
    public FriendRequests(int senderId, int receiverId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
    public FriendRequests(int senderId, int receiverId) {
        this.senderId = senderId;
        this.receiverId = receiverId;
    }
    public FriendRequests(int senderId, int receiverId,FriendRequestStatus status) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.requestStatus=status;
    }
    public FriendRequests() {

    }

    // Setter (only if mutability is required)
    public void setRequestStatus(FriendRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
        //this.updatedAt = LocalTime.now(); // Update the timestamp when status changes
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getSenderId() {
        return senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public FriendRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "FriendRequests{"
                + "senderId=" + senderId
                + ", receiverId=" + receiverId
                + ", requestStatus=" + requestStatus
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }

}
