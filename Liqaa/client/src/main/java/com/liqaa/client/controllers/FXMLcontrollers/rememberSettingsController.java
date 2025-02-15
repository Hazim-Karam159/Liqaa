package com.liqaa.client.controllers.FXMLcontrollers;

import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.network.ClientImpl;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.client.util.Settings;
import com.liqaa.shared.models.entities.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Properties;

public class rememberSettingsController {

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private Button DifferentAccountButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ImageView CircleImage;

    @FXML
    private ImageView passwordToggleImage; // مربوط بـ FXML

    private boolean isPasswordVisible = false;
    @FXML
    private Button loginButton;
    @FXML
    private Label userName;

    @FXML
    public void initialize() {
        passwordToggleImage.setFitWidth(40);
        passwordToggleImage.setFitHeight(40);
        passwordToggleImage.setPreserveRatio(false);
        passwordToggleImage.setSmooth(true);
        passwordToggleImage.setCache(true);

        userName.setText(Settings.getSavedUser().getDisplayName());

        byte[] profilePictureData = Settings.getSavedUser().getProfilepicture();
        if (profilePictureData != null) {
            CircleImage.setImage(new Image(new ByteArrayInputStream(profilePictureData)));
        } else {
            CircleImage.setImage(new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png")));
        }
        setImage("/com/liqaa/client/view/images/password2.png");
    }
    @FXML
    private void handleSignInButton() {
        SceneManager.getInstance().showSignInScene();
    }

    @FXML
    private void handleSignUpButton() {
        SceneManager.getInstance().showSignUpScene1();
    }
    @FXML
    private void handleLoginButton(ActionEvent event) {

        String password = passwordField.getText();
        User user = null;
        try {
            user = ServerConnection.getServer().signIn(ClientImpl.getClient(), Settings.getSavedUser().getPhoneNumber(), password);
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occurred while attempting to sign in. Please try again.");
            alert.show();
            return;
        }
            if (user == null) {
                Alert alert1 = new Alert(Alert.AlertType.ERROR);
                alert1.setContentText("Invalid Phone Number or Password.");
                alert1.show();
                passwordField.clear();
            } else
            {
                user.setPasswordHash(password);
                Settings.saveUser(user, DataCenter.getInstance().isRememberMeEnabled());

                DataCenter.getInstance().setCurrentUser(user);
                SceneManager.getInstance().showPrimaryScene();
            }
        }




    @FXML
    private void handleDifferentAccountButton() {
        SceneManager.getInstance().showSignInScene();
    }

    @FXML
    private void changeProfileImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            CircleImage.setImage(new Image(file.toURI().toString()));
        }
    }




    @FXML
    private void togglePasswordIcon() {
        String imagePath = isPasswordVisible
                ? "/com/liqaa/client/view/images/password2.png"
                : "/com/liqaa/client/view/images/password1.png";

        setImage(imagePath);
        isPasswordVisible = !isPasswordVisible;
    }

    private void setImage(String path) {
        URL imageUrl = getClass().getResource(path);
        if (imageUrl == null) {
            System.out.println("❌ Image not found: " + path);
            return;
        }

        Image newImage = new Image(imageUrl.toString());
        passwordToggleImage.setImage(newImage);
    }

}











