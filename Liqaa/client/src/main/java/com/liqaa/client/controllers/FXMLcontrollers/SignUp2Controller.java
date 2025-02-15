package com.liqaa.client.controllers.FXMLcontrollers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.util.Arrays;

import com.liqaa.client.Main;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.network.ClientImpl;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.client.util.Settings;
import com.liqaa.server.network.ServerImpl;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.network.Client;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static com.liqaa.client.controllers.FXMLcontrollers.SignUp1Controller.User1;

public class SignUp2Controller {

    @FXML
    private Button ChoosePicture;

    @FXML
    private Button RegistrationButton;

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private Circle CircleImage;
    /**
     * Attributes
     */
    private File selectedFile;
    private byte[] UserImage;


    public void initialize() {

        try {
            Image userImage = new Image(getClass().getResource("/com/liqaa/client/view/images/Defaultimage.png").toExternalForm());
            Platform.runLater(() -> {
                CircleImage.setFill(new ImagePattern(userImage));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleProfilePictureButton(ActionEvent event) {
        System.out.println("Choose picture clicked");
        // choose photo
        // Create a FileChooser to select an image
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        // Show the FileChooser and get the selected file
        Stage stage = (Stage) ChoosePicture.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            // Creating an image from the selected file
//            userImage = Files.readAllBytes(selectedFile.toPath());
            Image image = new Image(selectedFile.toURI().toString());
            // Setting the image view
            CircleImage.setFill(new ImagePattern(image));

        }
    }

    @FXML
    void handleRegistrationButton(ActionEvent event) {
        System.out.println("Register clicked");
        try {
            // Set the profile image
            if (selectedFile != null) {
                byte[] userImageBytes = Files.readAllBytes(selectedFile.toPath());
                User1.setProfilepicture(userImageBytes);
            }
            // Call server via RMI
            System.out.println("singnUp2 controller: " + User1);
            User registeredUser = ServerConnection.getServer().signUp(User1);
            System.out.println("singnUp2 controller: " + registeredUser);
            Platform.runLater(() -> {
                if (registeredUser != null) {
                    Settings.saveUser(registeredUser, DataCenter.getInstance().isRememberMeEnabled());
                    DataCenter.getInstance().setCurrentUser(registeredUser);
                    try {
                        ServerImpl.getServer().registerClient(ClientImpl.getClient(), registeredUser.getId());
                        System.out.println("singnUp2 controller: " + registeredUser);

                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
//                    DataCenter.getInstance().getcurrentUserId()
//                    ServerConnection.getServer().updateUserInfo();
                    SceneManager.getInstance().showPrimaryScene();
                    // todo: update isActive
                } else {
                    showErrorAlert("Registration Failed", "User already exists");
                }
            });
        } catch (RemoteException e) {
            Platform.runLater(() ->
                    showErrorAlert("Connection Error", "Failed to connect to server"));
        } catch (Exception e) {
            Platform.runLater(() ->
                    showErrorAlert("Registration Error", "Failed to create account"));
        }
    }

    @FXML
    void handleSignInButton(ActionEvent event) {
        System.out.println("Sign In button clicked.");
        try {
            SceneManager.getInstance().showSignInScene();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("signIn exception: " + ex.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occurred while attempting to sign in. Please try again.");
            alert.show();
        }
    }

    @FXML
    void handleSignUpButton(ActionEvent event) {
            System.out.println("Sign up button clicked.");
            try {
                SceneManager.getInstance().showSignUpScene1();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("signup exception: " + ex.getMessage());
            }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }
}