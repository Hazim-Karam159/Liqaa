package com.liqaa.server.controllers.FXMLcontrollers;

import com.liqaa.server.Main;
import com.liqaa.server.controllers.services.implementations.MessageServiceImpl;
import com.liqaa.server.util.FilePaths;
import com.liqaa.server.util.SceneManager;
import com.liqaa.shared.models.entities.Announcement;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

public class AnnouncementsController {
    @javafx.fxml.FXML
    private TextField titleTextField;
    @javafx.fxml.FXML
    private TextField announcementTextField;
    @javafx.fxml.FXML
    private Button sendButton;

    @javafx.fxml.FXML
    public void logout(Event event) {
        System.out.println("logout called");
        SceneManager.getInstance().showLogin();
    }

    @FXML
    public void switchToStatistics(Event event) {
        System.out.println("switchToStatistics called");
        SceneManager.getInstance().showStatistics();
    }





    @javafx.fxml.FXML
    public void switchToAnnouncements(Event event)
    {
        SceneManager.getInstance().showAnnouncements();
    }

    @javafx.fxml.FXML
    public void sendAnnouncement(Event actionEvent) {

        try {
            Announcement AnnouncementObject=new Announcement( titleTextField.getText(),announcementTextField.getText());
            MessageServiceImpl.getInstance().createAnnouncement(AnnouncementObject);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        titleTextField.clear();
        announcementTextField.clear();

    }

    @javafx.fxml.FXML
    public void switchToManager(Event event)
    {
        SceneManager.getInstance().showManager();
    }
}