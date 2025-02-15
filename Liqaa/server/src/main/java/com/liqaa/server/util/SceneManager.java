package com.liqaa.server.util;

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


    public void showLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/server/view/fxml/login.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.Login);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showManager() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/server/view/fxml/manager.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.Manager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void showStatistics() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/server/view/fxml/statistics.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.Statistics);
        } catch (IOException e) {
            System.err.println("Failed to load statistics.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void showAnnouncements() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/com/liqaa/server/view/fxml/announcements.fxml"));
            Scene newScene = new Scene(root);
            double width = stage.getWidth();
            double height = stage.getHeight();
            stage.setScene(newScene);
            stage.setWidth(width);
            stage.setHeight(height);
            setCurrentScene(SceneType.Announcements);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public enum SceneType
    {
        Login,
        Manager,
        Statistics,
        Announcements,
        UNKNOWN // Fallback for untracked scenes
    }
}