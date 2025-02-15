package com.liqaa.client.util;

import com.liqaa.shared.models.entities.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class SceneManager
{
    private static SceneManager instance;
    private final Stage stage;
    private SceneType currentScene = SceneType.UNKNOWN; // Default


    private SceneManager(Stage stage) {
        this.stage = stage;
    }

    public static void initialize(Stage stage)
    {
        if (instance == null) {
            instance = new SceneManager(stage);
        }
    }

    public static SceneManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SceneManager is not initialized. Call initialize(Stage stage) first.");
        }
        return instance;
    }


    public SceneType getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(SceneType currentScene) {
        this.currentScene = currentScene;
    }


    public void showUserInfoSceneInNewStage() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/userInfo.fxml"));
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setScene(newScene);
            newStage.show();
            setCurrentScene(SceneType.UNKNOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSettings() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/setting.fxml"));
            Scene newScene = new Scene(root);
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.UNDECORATED);
            newStage.setScene(newScene);
            newStage.show();
            setCurrentScene(SceneType.UNKNOWN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showPrimaryScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/primarynew.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.PRIMARY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showNotificationScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/notificationsnew.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.NOTIFICATION);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showContactScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/contacts.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.CONTACT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showSignInScene() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/signIn.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.SIGN_IN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showRememberedSignIn() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/rememberSettings.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.SIGN_IN);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showSignUpScene1() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/SignUp1.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.SIGN_UP_1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showSignUpScene2( ) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/signUp2.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.SIGN_UP_2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum SceneType
    {
        PRIMARY,
        NOTIFICATION,
        CONTACT,
        SIGN_IN,
        SIGN_UP_1,
        SIGN_UP_2,
        UNKNOWN // Fallback for untracked scenes
    }
}