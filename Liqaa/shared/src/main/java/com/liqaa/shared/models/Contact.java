package com.liqaa.shared.models;

import javafx.scene.image.Image;

import java.io.Serializable;

public class Contact implements Serializable {
    private Image photo;
    private String name;
    private String phoneNumber;
    private String bio;
    private String status;
    private String category;
    private Image block;
    private Image edit;
    private Image delete;

    public Contact() {
    }

    public Contact(Image photo, String name, String phoneNumber, String bio, String status, String category, Image edit, Image block, Image delete) {
        this.photo = photo;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.status = status;
        this.category = category;
        this.edit = edit;
        this.block = block;
        this.delete = delete;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Image getBlock() {
        return block;
    }

    public void setBlock(Image block) {
        this.block = block;
    }

    public Image getEdit() {
        return edit;
    }

    public void setEdit(Image edit) {
        this.edit = edit;
    }

    public Image getDelete() {
        return delete;
    }

    public void setDelete(Image delete) {
        this.delete = delete;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "photo=" + photo +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", bio='" + bio + '\'' +
                ", status='" + status + '\'' +
                ", category='" + category + '\'' +
                ", block=" + block +
                ", edit=" + edit +
                ", delete=" + delete +
                '}';
    }
}

