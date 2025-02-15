package com.liqaa.client.controllers.FXMLcontrollers;
import com.liqaa.client.Main;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.network.ClientImpl;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.client.util.Settings;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.network.Server;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.rmi.RemoteException;


public class SignInController {

    @FXML
    private Button LogInButton;

    @FXML
    private TextField PasswordField;

    @FXML
    private Button SignInButton;

    @FXML
    private Button SignUpButton;

    @FXML
    private TextField PhoneField;

    public void initialize() {

        System.out.println("sign in controller");
         SignInButton.setDisable(true);

}

    @FXML
    void handleLogInButton(ActionEvent event) {
        try {
            String phone = PhoneField.getText();
            String password = PasswordField.getText();

            User user = ServerConnection.getServer().signIn(ClientImpl.getClient(), phone, password);
            if (user == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Invalid Phone Number or Password.");
                alert.show();
                PasswordField.clear();
                PhoneField.clear();
            } else
            {
                user.setPasswordHash(password);
                Settings.saveUser(user, DataCenter.getInstance().isRememberMeEnabled());

                DataCenter.getInstance().setCurrentUser(user);
                SceneManager.getInstance().showPrimaryScene();
            }
        } catch (RemoteException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("An error occurred while attempting to sign in. Please try again.");
            alert.show();
            e.printStackTrace();
        }
    }


    @FXML
    void handleSignUpButton(ActionEvent event) {
        System.out.println("SignUpButton");
            System.out.println("Sign up button clicked.");
            try {
                SceneManager.getInstance().showSignUpScene1();
            }  catch (Exception ex) {
                ex.printStackTrace();
                System.out.println("signIn exception: " + ex.getMessage());
            }
    }

}


