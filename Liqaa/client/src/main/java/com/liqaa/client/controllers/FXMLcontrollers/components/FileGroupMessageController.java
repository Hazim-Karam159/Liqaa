package com.liqaa.client.controllers.FXMLcontrollers.components;

import com.liqaa.client.controllers.services.implementations.BaseMessageController;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.controllers.services.implementations.MessageServices;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.MessageType;
import com.liqaa.shared.util.NotificationPopup;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;

public class FileGroupMessageController extends BaseMessageController {
    @FXML private Label timeLabel;
    @FXML private ImageView statusIcon;
    @FXML
    private AnchorPane footerContainer;
    @FXML
    private ImageView download;
    @FXML
    private Text fileName;
    @FXML
    private Text fileSize;
    @FXML
    private ImageView fileIcon;
    @FXML
    private Text fileType;
    @FXML
    private Text senderName;
    @FXML
    ImageView senderImage;

    FileMessage fileMessage;

    Message currentMessage = new Message();
    @Override
    public void setMessage(Message message,int currentUserId)
    {

        //todo: call user service to get sender info istead of dummy data
        User sender = new User(2,"Ahmed hassan","ahmed@gmail","password",null );

        senderName.setText(sender.getDisplayName());
        if(sender.getProfilepicture() == null)
            senderImage.setImage(new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png")));
        else
            senderImage.setImage(new Image(new ByteArrayInputStream(sender.getProfilepicture())));


        fileMessage = MessageServices.getInstance().getFileInfo(message.getId());
        System.out.println(fileMessage);
        boolean isCurrentUser = message.getSenderId() == currentUserId;

        fileName.setText(fileMessage.getFileName());

        if(fileMessage.getFileSize() < 1024)
            fileSize.setText(fileMessage.getFileSize() + " B");
        else if(fileMessage.getFileSize() < 1048576)
            fileSize.setText(fileMessage.getFileSize() / 1024 + " KB");
        else if(fileMessage.getFileSize() < 1073741824)
            fileSize.setText(fileMessage.getFileSize() / 1048576 + " MB");
        else
            fileSize.setText(fileMessage.getFileSize() / 1073741824 + " GB");

        fileType.setText(message.getType().toString());
        if(isCurrentUser)
            timeLabel.setText(formatTime(message.getSentAt()));
        else
            timeLabel.setText(formatTime(message.getReceivedAt()));
        setFileIcon(fileIcon,message);
        setStatusIndicator(statusIcon, message);

        currentMessage=message;
    }

    @FXML
    public void downloadFile(MouseEvent event) {
        DirectoryChooser chooser = new DirectoryChooser();
        File dir = chooser.showDialog(null);

        if (dir != null && currentMessage != null) {
            new Thread(() -> {
                try {
                    boolean success = MessageServices.getInstance().downloadFile(
                            currentMessage.getId(),
                            DataCenter.getInstance().getCurrentConversationId(),
                            dir.getAbsolutePath()
                    );

                    Platform.runLater(() -> {
                        Stage stage = (Stage) download.getScene().getWindow();
                        if (success) {
                            NotificationPopup.show(
                                    stage,
                                    "File status",
                                    "Download completed successfully!",
                                    NotificationPopup.NotificationType.INFO
                            );
                        } else {
                            NotificationPopup.show(
                                    stage,
                                    "Error",
                                    "Download failed",
                                    NotificationPopup.NotificationType.INFO
                            );
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        Stage stage = (Stage) download.getScene().getWindow();
                        NotificationPopup.show(
                                stage,
                                "Error",
                                "Error during download",
                                NotificationPopup.NotificationType.INFO
                        );
                    });
                }
            }).start();
        }
    }
}