package com.liqaa.client.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Optional;

public class Alerts
{

    public static Optional<ButtonType> showAlert(AlertType alertType, String title, String header, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);

        return alertType == AlertType.CONFIRMATION ? alert.showAndWait() : Optional.of(alert.showAndWait().orElse(null));
    }

    public static void showInfo(String title, String message) {
        showAlert(AlertType.INFORMATION, title, null, message);
    }

    public static void showWarning(String title, String message) {
        showAlert(AlertType.WARNING, title, null, message);
    }

    public static void showError(String title, String message) {
        showAlert(AlertType.ERROR, title, null, message);
    }

    public static boolean showConfirmation(String title, String message) {
        Optional<ButtonType> result = showAlert(AlertType.CONFIRMATION, title, null, message);
        return result.isPresent() && result.get() == ButtonType.OK;
    }
}