package com.liqaa.shared.util;

import javafx.scene.control.Alert;

public class AlertNotifier
{
    public static Alert createAlert(String title, String header, String message)
    {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        return alert;
    }
}