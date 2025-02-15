package com.liqaa.client.controllers.FXMLcontrollers.components;

import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.enums.ConversationType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

import java.io.ByteArrayInputStream;
import java.util.Arrays;

public class ChatItemController {
    @FXML
    private ImageView image;
    @FXML
    private Label msgCounter;
    @FXML
    private Label name;
    @FXML
    private Circle msgCircle;
    @FXML
    private Label lastMsgTime;
    @FXML
    private Label status;

    private ChatInfo chatInfo;

    public void setChatInfo(ChatInfo chatInfo)
    {
        this.chatInfo = chatInfo;
        name.setText(chatInfo.getName());
        if (chatInfo.getImage() != null)
            image.setImage(new javafx.scene.image.Image(new ByteArrayInputStream(chatInfo.getImage())));
        else if (chatInfo.getType() == ConversationType.GROUP)
            image.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultGroupProfile.png")));
        else
            image.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png")));
        msgCounter.setText(String.valueOf(chatInfo.getUnreadMsgCount()));
        java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("hh:mm a");
        if (chatInfo.getLastMsgTime() != null) {
            lastMsgTime.setText(chatInfo.getLastMsgTime().format(formatter));
        } else {
            lastMsgTime.setText("");
        }
        status.setTextFill(javafx.scene.paint.Color.GRAY);
        if (chatInfo.getType() == ConversationType.GROUP) {
            status.setText("");
        } else if (chatInfo.getStatus() != null) {
            status.setText(chatInfo.getStatus().name());
            if (!chatInfo.getStatus().name().equalsIgnoreCase("OFFLINE")) {
                status.setTextFill(javafx.scene.paint.Color.GREEN);
            }
        } else {
            status.setText("");
        }

        if (chatInfo.getUnreadMsgCount() == 0) {
            msgCounter.setVisible(false);
            msgCircle.setVisible(false);
        } else {
            msgCounter.setVisible(true);
            msgCircle.setVisible(true);
            if (chatInfo.getUnreadMsgCount() > 10)
                msgCounter.setText("+9");
            else
                msgCounter.setText(String.valueOf(chatInfo.getUnreadMsgCount()));
        }
    }

    @FXML
    private void handleClick(MouseEvent event)
    {
        System.out.println("Chat item clicked: " + chatInfo.getName());
        // Add additional actions to be performed on click
    }
}