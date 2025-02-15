package com.liqaa.server.controllers.FXMLcontrollers;

import com.liqaa.server.Main;
import com.liqaa.server.util.FilePaths;
import com.liqaa.server.util.SceneManager;
import com.liqaa.shared.models.entities.User;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class LoginController
{
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private ImageView passwordIcon;
    @FXML
    private TextField adminIdTextField;
    @FXML
    private Button AdminLogInButton;

    boolean isPasswordVisiable= true;

    @FXML
    public void handlePasswordVisability(Event event)
    {
        isPasswordVisiable = !isPasswordVisiable;
//        passwordTextField. (isPasswordVisiable);
        passwordIcon.setVisible(isPasswordVisiable);

    }

    @FXML
    public void handleAdminLogInButton(ActionEvent actionEvent)
    {
        if(( adminIdTextField.getText().equals("admin"))&&(passwordTextField.getText().equals("iti123")))
        {
        SceneManager.getInstance().showManager();
    }
      else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText("Invalid Admin ID or Password");
            alert.setContentText("Please enter a valid Admin ID and Password.");
            alert.showAndWait();
            adminIdTextField.clear();;
            passwordTextField.clear();
        }
    }
}
