package com.liqaa.shared.models.entities;

import java.io.Serializable;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class Contacts implements Serializable {

    private final int userId; //Made final for immutability
    private final int contactId;
    private boolean isBlocked;
    private Date createdAt;

    public Contacts(int userId, int contactId,  boolean isBlocked) {
        this.userId = userId;
        this.contactId = contactId;
        this.isBlocked = isBlocked;
    }
    public Contacts(int userId, int contactId, Date createdAt, boolean isBlocked) {
        this.userId = userId;
        this.contactId = contactId;
        this.createdAt = createdAt;
        this.isBlocked = isBlocked;
    }

    public void setIsBlocked(Boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getUserId() {
        return userId;
    }

    public int getContactId() {
        return contactId;
    }

    public Boolean getIsBlocked() {
        return isBlocked;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "Contacts{"
                + "userId=" + userId
                + ", contactId=" + contactId
                + ", createdAt=" + createdAt
                + ", isBlocked=" + isBlocked
                + '}';
    }

    // equals()  for proper object comparison
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Contacts contacts = (Contacts) o;
        return userId == contacts.userId
                && contactId == contacts.contactId
                && isBlocked == contacts.isBlocked
                && Objects.equals(createdAt, contacts.createdAt);
    }

}
