package com.liqaa.client.controllers.FXMLcontrollers;


import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.controllers.services.implementations.UserInfoServiceImpl;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.CurrentStatus;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class UserInfoController implements Initializable {
    @FXML
    private TextField bioTextField;

    @FXML
    private ComboBox<String> statusChooser;

    @FXML
    private TextField nameTextField;

    @FXML
    private Circle profilePhoto;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private Button saveBtn;

    private String userName, userPhone, userBio, userEmail, userStatus, userCountry;
    private byte[] userPhoto;

    private File selectedFile;

    private User currentUser = DataCenter.getInstance().getCurrentUser();

    private Image selectedImage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        String[] statusEnums = {"Available", "Busy", "Away", "Offline"};
        statusChooser.getItems().addAll(statusEnums);

        userPhoto = currentUser.getProfilepicture();
        userName = currentUser.getDisplayName();
        userPhone = currentUser.getPhoneNumber();
        userBio = currentUser.getBio();
        userEmail = currentUser.getEmail();
        userCountry = currentUser.getCountry();
        userStatus = String.valueOf(currentUser.getCurrentstatus());

        InputStream inputStream = userPhoto != null ? new ByteArrayInputStream(userPhoto) : getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png");
        Image image = new Image(inputStream);
        profilePhoto.setFill(new ImagePattern(image));
        profilePhoto.setStroke(null);
        nameTextField.setText(userName);
        phoneTextField.setText(userPhone);
        bioTextField.setText(userBio);
        emailTextField.setText(userEmail);
        countryTextField.setText(userCountry);
        if(userStatus.equals("AVAILABLE")) userStatus = "Available";
        else if(userStatus.equals("BUSY")) userStatus = "Busy";
        else if (userStatus.equals("AWAY")) userStatus = "Away";
        else if (userStatus.equals("OFFLINE")) userStatus = "Offline";
        statusChooser.setValue(userStatus);
    }

    public void editNameMouseClicked(MouseEvent mouseEvent) {
        System.out.println("edit name is clicked");
        nameTextField.setEditable(true);
    }

    public void editImgMouseClicked(MouseEvent mouseEvent) {
        System.out.println("edit image is clicked");
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        selectedFile = fileChooser.showOpenDialog(NotificationsController.myStage);
        if (selectedFile != null) {
            selectedImage = new Image(selectedFile.toURI().toString());
            profilePhoto.setFill(new ImagePattern(selectedImage));
        }
    }

    public void editBioMouseClicked(MouseEvent mouseEvent) {
        System.out.println("edit bio is clicked");
        bioTextField.setEditable(true);
    }

    public void saveAction() throws IOException {
        currentUser.setDisplayName(nameTextField.getText());
        currentUser.setBio(bioTextField.getText());
        String chosenStatus = statusChooser.getValue();
        if(chosenStatus.equals("Available")) currentUser.setCurrentstatus(CurrentStatus.AVAILABLE);
        else if(chosenStatus.equals("Busy")) currentUser.setCurrentstatus(CurrentStatus.BUSY);
        else if(chosenStatus.equals("Away")) currentUser.setCurrentstatus(CurrentStatus.AWAY);
        else if(chosenStatus.equals("Offline")) currentUser.setCurrentstatus(CurrentStatus.OFFLINE);
        if (selectedFile != null) {
            byte[] newProfilePicture = Files.readAllBytes(selectedFile.toPath());
            currentUser.setProfilepicture(newProfilePicture);
            Platform.runLater(() -> NotificationsController.mainProfilePhoto.setFill(new ImagePattern(selectedImage)));
        } else {
            currentUser.setProfilepicture(userPhoto);
        }
        UserInfoServiceImpl.getInstance().updateUser(currentUser);

        Platform.runLater(() -> {
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.close();
        });
    }

    public void closeAction(MouseEvent mouseEvent){
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.close();
    }
}