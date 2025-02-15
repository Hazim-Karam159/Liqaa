package com.liqaa.client;

import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.network.ClientImpl;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.client.util.Settings;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.network.Server;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image appIcon = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/logoIcon.png"));
        stage.getIcons().add(appIcon);

        SceneManager.initialize(stage);

        User savedUser = Settings.getSavedUser();
        if (savedUser != null) {
            if (savedUser.getPasswordHash() != null && !savedUser.getPasswordHash().isEmpty())
            {
                try {
                    User authenticatedUser = ServerConnection.getServer().signIn(ClientImpl.getClient(), savedUser.getPhoneNumber(), savedUser.getPasswordHash());
                    if (authenticatedUser != null) {
                        DataCenter.getInstance().setCurrentUser(authenticatedUser);
                        SceneManager.getInstance().showPrimaryScene();
                    } else {
                        showAlert("Failed to authenticate the saved user. Please sign in again.");
                        SceneManager.getInstance().showSignInScene();
                    }
                } catch (RemoteException e) {
                    showAlert("An error occurred while attempting to sign in. Please try again.");
                    SceneManager.getInstance().showSignInScene();
                }
            } else {
                SceneManager.getInstance().showRememberedSignIn();
            }
        } else
            SceneManager.getInstance().showSignInScene();


        stage.setTitle("Liqaa");
        stage.show();
        testServerConnection();
    }


    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }


private void testServerConnection() throws RemoteException
    {
        Server server = ServerConnection.getServer();
        if (server != null) {
            String message = server.ping();
            System.out.println("Connected to server successfully. Server responded: " + message);
        } else {
            System.err.println("Failed to connect to server.");
        }
    }

    public static void main(String[] args) throws SQLException, RemoteException
    {
        launch();
    }
}