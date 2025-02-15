package com.liqaa.client.controllers.services.implementations;

import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.MessageType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class BaseMessageController
{
    protected static final Image SINGLE_CHECK;
    protected static final Image DOUBLE_CHECK;
    protected static final Image BLUE_DOUBLE_CHECK;

    protected static final Image DOC_ICON;
    protected static final Image IMAGE_ICON;
    protected static final Image SOUND_ICON;
    protected static final Image VIDEO_ICON;

    static
    {
        SINGLE_CHECK = loadImage("/com/liqaa/client/view/images/seenFalse.png");
        DOUBLE_CHECK = loadImage("/com/liqaa/client/view/images/sentMsg.png");
        BLUE_DOUBLE_CHECK = loadImage("/com/liqaa/client/view/images/seenTrue.png");

        DOC_ICON   = loadImage("/com/liqaa/client/view/images/fileMsg.png");
        IMAGE_ICON = loadImage("/com/liqaa/client/view/images/imageMsg.png");
        SOUND_ICON = loadImage("/com/liqaa/client/view/images/soundMsg.png");
        VIDEO_ICON = loadImage("/com/liqaa/client/view/images/videoMsg.png");

    }

    private static Image loadImage(String path)
    {
        try {
            return new Image(BaseMessageController.class.getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
            return null;
        }
    }
    public abstract void setMessage(Message message, int currentUserId);

    protected String formatTime(LocalDateTime time) {
        return time != null ? time.format(DateTimeFormatter.ofPattern("hh:mm a")) : "";
    }

    protected void setStatusIndicator(ImageView statusIcon, Message message)
    {
        if(statusIcon == null)
            return;
        if (message.getSeenAt() != null) {

            statusIcon.setImage(BLUE_DOUBLE_CHECK);
        } else if (message.getReceivedAt() != null) {
            statusIcon.setImage(DOUBLE_CHECK);
        } else if (message.isSent()) {
            statusIcon.setImage(SINGLE_CHECK);
        }
    }

    protected void setFileIcon(ImageView fileIcon, Message message) {
        if (message.getType() == MessageType.DOCUMENT) {
            fileIcon.setImage(DOC_ICON);
        } else if (message.getType() == MessageType.IMAGE) {
            fileIcon.setImage(IMAGE_ICON);
        } else if (message.getType() == MessageType.AUDIO) {
            fileIcon.setImage(SOUND_ICON);
        } else if (message.getType() == MessageType.VIDEO) {
            fileIcon.setImage(VIDEO_ICON);
        }
    }
}