package com.liqaa.shared.models.entities;

import java.time.LocalDate;
import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Arrays;

import  com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.models.enums.Gender;

/**
 * ******** The user class implements Serializable "marker interface" to
 * convert data to a format that can be sent via the network.
 */
/**
 * implementing Serializable allows objects to be converted into a format (byte
 * stream) that can be sent over a network
 */
public class User implements Serializable {

    /**
     * User Attribute *
     */
    private int id;
    private String phoneNumber;
    private String displayName;
    private String email;
    private String passwordHash;
    private String country;
    private String bio;
    private Gender gender;
    private Date dateOfBirth;
    private boolean isActive;
    private CurrentStatus currentStatus = CurrentStatus.AVAILABLE; // Default value
    private byte[] profilePicture;
    private LocalTime createdAt;

    /**
     * ********** User class Constructors********
     */
    public User() {
        // Initialize fields here
    }

    // Constructor
    public User( String phoneNumber, String displayName, String email, String passwordHash, String country, String bio, Gender gender, Date dateOfBirth, boolean isActive, CurrentStatus currentStatus, byte[] profilePicture, LocalTime createdAt) {
        //this.id = id;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.country = country;
        this.bio = bio;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.isActive = isActive;
        this.currentStatus = currentStatus;
        this.profilePicture = profilePicture;
        this.createdAt = createdAt;
    }

    public User(String displayName, String passwordHash, String phoneNumber) {
        this.displayName = displayName;
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
    }

    public User(String passwordHash, String phoneNumber) {
        this.passwordHash = passwordHash;
        this.phoneNumber = phoneNumber;
    }

    public User(int id, String displayName, String email, String passwordHash, byte[] profilePicture)
    {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePicture = profilePicture;
        this.isActive = true;
        this.currentStatus = CurrentStatus.AVAILABLE;
    }



    /**
     * ************************ Setter *************************************
     */
    public void setId(int id) { this.id = id; }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setDateofBirth(Date  dateofBirth) {
        this.dateOfBirth = dateofBirth;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public void setCurrentstatus(CurrentStatus currentstatus) {
        this.currentStatus = currentstatus;
    }

    public void setProfilepicture(byte[] profilepicture) {
        this.profilePicture = profilepicture;
    }

    public void setCreatedAt(LocalTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * *********************** Getter********************
     */
    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getCountry() {
        return country;
    }

    public String getBio() {
        return bio;
    }

    public Gender getGender() {
        return gender;
    }

    public Date getDateofBirth() {
        return dateOfBirth;
    }

    public boolean isActive() {
        return isActive;
    }

    public CurrentStatus getCurrentstatus() {
        return currentStatus;
    }

    public byte[] getProfilepicture() {
        return profilePicture;
    }

    public LocalTime getCreatedAt() {
        return createdAt;
    }

    // toString() method for debugging
    @Override
    public String toString() {
        return "User{"
                + "id=" + getId()
                + ", phoneNumber='" + phoneNumber + '\''
                + ", displayName='" + displayName + '\''
                + ", email='" + email + '\''
                + ", passwordHash='" + passwordHash + '\''
                + ", country='" + country + '\''
                + ", bio='" + bio + '\''
                + ", gender=" + gender
                + ", dateOfBirth=" + dateOfBirth
                + ", isActive=" + isActive
                + ", currentStatus=" + currentStatus
                + ", profilePicture=" + Arrays.toString(profilePicture)
                + ", createdAt=" + createdAt
                + '}';
    }
}