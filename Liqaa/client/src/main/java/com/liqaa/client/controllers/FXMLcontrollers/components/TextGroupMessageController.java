package com.liqaa.client.controllers.FXMLcontrollers.components;

import com.liqaa.client.controllers.services.implementations.BaseMessageController;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.Gender;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.ByteArrayInputStream;

public class TextGroupMessageController extends BaseMessageController
{

    //    @FXML private VBox textContainer;
    @FXML
    private HBox messageBubble;
    @FXML
    private Text contentLabel;
    @FXML
    private Label timeLabel;
//    @FXML
//    private ImageView statusIcon;
    @FXML
    private TextFlow contentFlow;
    @FXML
    private AnchorPane footerContainer;
    @FXML
    private ImageView senderImage;
    @FXML
    private Text senderName;

    @Override
    public void setMessage(Message message, int currentUserId)
    {

        boolean isCurrentUser = message.getSenderId() == currentUserId;

        //todo: call user service to get sender info istead of dummy data
        User sender = new User(2,"Ahmed hassan","ahmed@gmail","password",null );

        senderName.setText(sender.getDisplayName());
        if(sender.getProfilepicture() == null)
            senderImage.setImage(new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png")));
        else
            senderImage.setImage(new Image(new ByteArrayInputStream(sender.getProfilepicture())));

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
//        setStatusIndicator(statusIcon, message);

        // Ensure the text flows naturally inside the bubble
        HBox.setHgrow(messageBubble, Priority.NEVER);
    }
}