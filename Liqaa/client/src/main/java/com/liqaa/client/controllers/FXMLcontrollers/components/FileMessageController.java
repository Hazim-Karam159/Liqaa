package com.liqaa.client.controllers.FXMLcontrollers.components;

import com.liqaa.client.controllers.services.implementations.BaseMessageController;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.controllers.services.implementations.MessageServices;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.MessageType;
import com.liqaa.shared.util.NotificationPopup;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

import java.io.File;

public class FileMessageController extends BaseMessageController {
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

    Message currentMessage = new Message();
    FileMessage fileMessage;
    @Override
    public void setMessage(Message message,int currentUserId)
    {
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

        currentMessage= message;
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
                        if (success)
                        {
                            Node source = (Node) download.getScene().getRoot();
                            Stage stage = (Stage) source.getScene().getWindow();
                            NotificationPopup.show(
                                    stage,
                                    "File status",
                                    "Download completed successfully!",
                                    NotificationPopup.NotificationType.INFO
                            );
                        } else
                            NotificationPopup.show((Stage) download.getScene().getWindow(), "Error","Download failed", NotificationPopup.NotificationType.INFO);

                    });
                } catch (Exception e) {
                    Platform.runLater(() ->
                            NotificationPopup.show((Stage)  download.getScene().getWindow(), "Error","Error during download", NotificationPopup.NotificationType.INFO));
                }
            }).start();
        }
    }
}