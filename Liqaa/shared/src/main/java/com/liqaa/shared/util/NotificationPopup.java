package com.liqaa.shared.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;


public class NotificationPopup {
    private static final int NOTIFICATION_HEIGHT = 100;
    private static final int NOTIFICATION_WIDTH = 300;
    private static final int SHOW_TIME_MS = 3000; // 3 seconds
    private static double lastNotificationY = -1; // Track last notification position



    public static void show(Stage ownerStage, String title, String message, NotificationType type) {
        Platform.runLater(() -> {
            Stage notificationStage = new Stage(StageStyle.TRANSPARENT);
            notificationStage.initOwner(ownerStage);

            // Create the notification content
            VBox notificationBox = createNotificationBox(title, message, type);

            // Set up the scene
            Scene scene = new Scene(notificationBox);
            scene.setFill(null);
            notificationStage.setScene(scene);

            // Position the notification
            positionNotification(notificationStage);

            // Show with animation
            notificationStage.show();
            animateNotification(notificationStage);
        });
    }

    public static void showMessageNotification(Stage ownerStage, String senderName, String messagePreview) {
        String title = "New Message from " + senderName;
        String message = messagePreview.length() > 50 ?
                messagePreview.substring(0, 47) + "..." :
                messagePreview;

        show(ownerStage, title, message, NotificationType.NEW_MESSAGE);
    }

    private static VBox createNotificationBox(String title, String message, NotificationType type) {
        VBox notificationBox = new VBox(10);
        notificationBox.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 15;" +
                        "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.3), 10, 0, 0, 0);" +
                        "-fx-border-color: " + type.getColor() + ";" +
                        "-fx-border-width: 0 0 0 4;" + // Left border only
                        "-fx-border-radius: 10;"
        );

        // Header with icon and title
        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        // Load icon
        try {
            Image image = new Image(NotificationPopup.class.getResourceAsStream(type.getImagePath()));
            ImageView icon = new ImageView(image);
            icon.setFitHeight(24);
            icon.setFitWidth(24);
            header.getChildren().add(icon);
        } catch (Exception e) {
            System.err.println("Could not load notification icon: " + e.getMessage());
        }

        // Title
        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-weight: bold;" +
                        "-fx-font-size: 14;" +
                        "-fx-text-fill: " + type.getColor() + ";"
        );
        header.getChildren().add(titleLabel);

        // Message
        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setStyle(
                "-fx-font-size: 12;" +
                        "-fx-text-fill: #666666;"
        );

        notificationBox.getChildren().addAll(header, messageLabel);
        notificationBox.setPrefWidth(NOTIFICATION_WIDTH);
        notificationBox.setMaxWidth(NOTIFICATION_WIDTH);
        notificationBox.setMinHeight(NOTIFICATION_HEIGHT);

        return notificationBox;
    }

    private static void positionNotification(Stage notificationStage) {
        Screen screen = Screen.getPrimary();
        double screenWidth = screen.getVisualBounds().getWidth();
        double screenHeight = screen.getVisualBounds().getHeight();

        if (lastNotificationY == -1) {
            lastNotificationY = screenHeight - NOTIFICATION_HEIGHT - 20;
        } else {
            lastNotificationY -= (NOTIFICATION_HEIGHT + 10); // Stack notifications with 10px gap
            if (lastNotificationY < 20) { // Reset if we're too high
                lastNotificationY = screenHeight - NOTIFICATION_HEIGHT - 20;
            }
        }

        notificationStage.setX(screenWidth - NOTIFICATION_WIDTH - 20);
        notificationStage.setY(lastNotificationY);

        // Reset position after notification duration
        Timeline resetPosition = new Timeline(
                new KeyFrame(Duration.millis(SHOW_TIME_MS + 200), e -> {
                    if (lastNotificationY == notificationStage.getY()) {
                        lastNotificationY = -1;
                    }
                })
        );
        resetPosition.play();
    }

    private static void animateNotification(Stage notificationStage) {
        // Fade in
        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(notificationStage.getScene().getRoot().opacityProperty(), 0.0)),
                new KeyFrame(Duration.millis(200), new KeyValue(notificationStage.getScene().getRoot().opacityProperty(), 1.0))
        );
        fadeIn.play();

        // Schedule fade out and close
        Timeline fadeOut = new Timeline(
                new KeyFrame(Duration.millis(SHOW_TIME_MS),
                        new KeyValue(notificationStage.getScene().getRoot().opacityProperty(), 1.0)
                ),
                new KeyFrame(Duration.millis(SHOW_TIME_MS + 200), e -> notificationStage.close(),
                        new KeyValue(notificationStage.getScene().getRoot().opacityProperty(), 0.0)
                )
        );
        fadeOut.play();
    }

    public enum NotificationType
    {
        ANNOUNCEMENT("/images/announcement.png", "#2196F3"),    // Blue
        FRIEND_REQUEST("/images/friend_request.png", "#9C27B0"), // Purple
        REQUEST_ACCEPTED("/images/accepted.png", "#4CAF50"),    // Green
        REQUEST_DECLINED("/images/declined.png", "#F44336"),    // Red
        NEW_MESSAGE("/images/new_message.png", "#00BCD4"),      // Cyan
        INFO("/images/info.png", "#607D8B");                    // Gray

        private final String imagePath;
        private final String color;

        NotificationType(String imagePath, String color) {
            this.imagePath = imagePath;
            this.color = color;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getColor() {
            return color;
        }
    }
}