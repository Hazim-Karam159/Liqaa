package com.liqaa.client.controllers.FXMLcontrollers.components;

import com.liqaa.client.controllers.services.implementations.BaseMessageController;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class TextMessageController extends BaseMessageController
{

    //    @FXML private VBox textContainer;
    @FXML
    private HBox messageBubble;
    @FXML
    private Text contentLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ImageView statusIcon;
    @FXML
    private TextFlow contentFlow;
    @FXML
    private AnchorPane footerContainer;

    @Override
    public void setMessage(Message message, int currentUserId)
    {
//        if (messageBubble == null) {
//            throw new IllegalStateException("messageBubble not initialized! Check FXML file");
//        }

        boolean isCurrentUser = message.getSenderId() == currentUserId;

        // Clear previous content before adding new message
        contentFlow.getChildren().clear();
        Text messageText = new Text(message.getContent());
        messageText.setStyle("-fx-fill: #413f3f; -fx-font-size: 14px;");
        contentFlow.getChildren().add(messageText);

        // Set message time
        if(isCurrentUser)
            timeLabel.setText(formatTime(message.getSentAt()));
        else
            timeLabel.setText(formatTime(message.getReceivedAt()));

        // Update message status icon
        setStatusIndicator(statusIcon, message);

        // Ensure the text flows naturally inside the bubble
//        HBox.setHgrow(messageBubble, Priority.NEVER);
    }
}
