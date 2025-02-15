package com.liqaa.client.controllers.FXMLcontrollers;

import com.liqaa.client.controllers.FXMLcontrollers.components.CategoryListController;
import com.liqaa.client.controllers.FXMLcontrollers.components.ChatItemController;
import com.liqaa.client.controllers.services.implementations.BaseMessageController;
import com.liqaa.client.controllers.services.implementations.ConversationServices;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.controllers.services.implementations.MessageServices;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.client.util.Settings;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.FileMessage;
import com.liqaa.shared.models.entities.Message;
import com.liqaa.shared.models.enums.ConversationType;
import com.liqaa.shared.models.enums.MessageType;
import com.liqaa.shared.util.AlertNotifier;
import com.liqaa.shared.util.NotificationPopup;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.time.LocalDateTime;

import com.liqaa.shared.models.enums.CurrentStatus;

public class PrimaryController
{


    @FXML
    private TextField messageField;
    @FXML
    private ImageView chatUserImage;
    @FXML
    private Label chatUserName;
    @FXML
    private Label chatUserStatus;
    @FXML
    private StackPane logoPane;
    @FXML
    private VBox chatArea;
    @FXML
    private StackPane rightPane;
    @FXML
    private ImageView videoCallIcon;
    @FXML
    private ImageView voiceCallIcon;
    @FXML
    private ImageView contact_btn;
    @FXML
    private ImageView logout_btn;
    @FXML
    private ImageView chatbot_btn;
    @FXML
    private ImageView home_btn;
    @FXML
    private ImageView notification_btn;
    @FXML
        private ImageView settings_btn;
        @FXML
        private CategoryListController nullController;

       public static boolean isNewFileMessage= false;
       public static File selectedFile = null;

        @FXML private ListView<ChatInfo> chatListView;
        private ObservableList<ChatInfo> chatList= FXCollections.observableArrayList();
    @FXML
    private ListView<Message> chatBox;
    @FXML
    private ImageView attachFile;
    @FXML
    private ImageView emoji;
    @FXML
    private TextField search;
    @FXML
    private ImageView send;
    @FXML
    private Circle profileImage;

    private ListCell<Message> createMessageCell(ListView<Message> param)
    {
        return new ListCell<>() {
            private Node currentRoot;
            private BaseMessageController currentController;

            @Override
            protected void updateItem(Message message, boolean empty)
            {
                super.updateItem(message, empty);

                if (empty || message == null) {
                    setGraphic(null);
                    return;
                }

                try {
                    String fxmlFile = null;
                    if(message.getSenderId() == DataCenter.getInstance().getcurrentUserId())
                    {
                        fxmlFile = switch (message.getType())
                        {
                            case TEXT -> "/com/liqaa/client/view/fxml/components/sentTextMessage.fxml";
                            default -> "/com/liqaa/client/view/fxml/components/sentFileMessage.fxml";
                        };
                    }
                    else
                    {
                        if(DataCenter.getInstance().getCurrentChat().getType() == ConversationType.GROUP)
                        {
                            fxmlFile = switch (message.getType())
                            {
                                case TEXT -> "/com/liqaa/client/view/fxml/components/receivedTextGroupMessage.fxml";
                                default -> "/com/liqaa/client/view/fxml/components/receivedFileGroupMessage.fxml";
                            };
                        }
                        else
                        {
                            fxmlFile = switch (message.getType())
                            {
                                case TEXT -> "/com/liqaa/client/view/fxml/components/receivedTextMessage.fxml";
                                default -> "/com/liqaa/client/view/fxml/components/receivedFileMessage.fxml";
                            };
                        }

                    }

                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Node root = loader.load();
                    BaseMessageController controller = loader.getController();

                    if (root != currentRoot) {
                        currentRoot = root;
                        currentController = controller;
                        setGraphic(root);
                    }

                    currentController.setMessage(message, DataCenter.getInstance().getcurrentUserId());

                    HBox container = (HBox) root;
                    container.setAlignment(message.getSenderId() == DataCenter.getInstance().getcurrentUserId() ?
                            Pos.CENTER_RIGHT : Pos.CENTER_LEFT);

                } catch (IOException e)
                {
                    e.printStackTrace();
                    setGraphic(new Label("Error loading message"));
                }
            }
        };
    }


    @FXML
        public void initialize()
        {
            byte[] userPhoto = DataCenter.getInstance().getCurrentUser().getProfilepicture();
            if (userPhoto == null) {
                Image image = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png"));
                profileImage.setFill(new ImagePattern(image));
            } else {
                InputStream inputStream = new ByteArrayInputStream(userPhoto);
                Image image = new Image(inputStream);
                profileImage.setFill(new ImagePattern(image));
            }
            profileImage.setStroke(null);


            FilteredList<ChatInfo> filteredData = new FilteredList<>(DataCenter.getInstance().getChats(), p -> true);

            // Add listener to search field
            search.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(chat -> {
                    // If search field is empty, show all conversations
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    String lowerCaseFilter = newValue.toLowerCase();

                    // Compare chat name with filter text
                    if (chat.getName() != null && chat.getName().toLowerCase().contains(lowerCaseFilter)) {
                        return true;
                    }

                    return false;
                });
            });

            chatListView.setCellFactory(new Callback<ListView<ChatInfo>, ListCell<ChatInfo>>()
            {
                @Override
                public ListCell<ChatInfo> call(ListView<ChatInfo> param) {
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(ChatInfo chat, boolean empty) {
                            super.updateItem(chat, empty);
                            if (empty || chat == null) {
                                setGraphic(null);
                            } else {
                                setGraphic(createChatCard(chat));
                                setOnMouseClicked(event -> handleChatClick(chat));
                            }
                        }
                    };
                }
            });
            try {
                chatArea.setVisible(false);
                logoPane.setVisible(true);
                chatArea.setOnKeyPressed(event ->
                {
                    if (event.getCode() == KeyCode.ESCAPE) {
                        chatArea.setVisible(false);
                        logoPane.setVisible(true);
                    }
                });
            } catch (Exception e) {
                System.err.println("Error initializing contacts: " + e.getMessage());
            }

            chatBox.setCellFactory(param -> createMessageCell((ListView<Message>) param));

            chatListView.setItems(filteredData);


            ConversationServices.getInstance().loadAllConversations(DataCenter.getInstance().getcurrentUserId());
            chatBox.setItems(DataCenter.getInstance().getMessages());

            DataCenter.getInstance().initializeListeners();
            chatBox.getItems().addListener((ListChangeListener<Message>) change -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        scrollDown();
                    }
                }
            });
            scrollDown();
        }


        private Parent createChatCard(ChatInfo chatInfo)
        {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/liqaa/client/view/fxml/components/chatItem.fxml"));
                Parent chatCard = loader.load();

                ChatItemController controller = loader.getController();
                controller.setChatInfo(chatInfo);

                return chatCard;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }



    private void handleChatClick(ChatInfo chatInfo)
    {
        DataCenter.getInstance().setCurrentChat(chatInfo);
        chatUserName.setText(chatInfo.getName());
        CurrentStatus status = chatInfo.getStatus();
        chatUserStatus.setText(status != null ? status.name() : " ");
        chatUserStatus.getStyleClass().clear(); // Remove any previous styles
        chatUserStatus.getStyleClass().add("chat-user-status"); // Add the base style
        if (status == CurrentStatus.AVAILABLE) {
            chatUserStatus.getStyleClass().add("online");
        } else if (status == CurrentStatus.OFFLINE) {
            chatUserStatus.getStyleClass().add("offline");
        } else if (status == CurrentStatus.BUSY) {
            chatUserStatus.getStyleClass().add("busy");
        } else if (status == CurrentStatus.AWAY) {
            chatUserStatus.getStyleClass().add("away");
        }
        // Update user image
        if (chatInfo.getImage() != null) {
            chatUserImage.setImage(new Image(new ByteArrayInputStream(chatInfo.getImage())));
        } else {
            chatUserImage.setImage(new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png")));
        }
        chatUserImage.setFitHeight(60); // Increase image size
        chatUserImage.setFitWidth(60);  // Increase image size


        MessageServices.getInstance().loadMessages(DataCenter.getInstance().getCurrentConversationId());
        MessageServices.getInstance().markMessagesAsSeen(DataCenter.getInstance().getCurrentConversationId());


        logoPane.setVisible(false);
        chatArea.setVisible(true);
        chatArea.requestFocus();

        Platform.runLater(() ->
        {
            if (!DataCenter.getInstance().getMessages().isEmpty()) {
                chatBox.scrollTo(DataCenter.getInstance().getMessages().size() - 1);
                System.out.println("Scrolling to last message");
            }
        });
    }

    public void scrollDown()
    {
        Platform.runLater(() ->
        {
            if (!DataCenter.getInstance().getMessages().isEmpty()) {
                chatBox.scrollTo(DataCenter.getInstance().getMessages().size() -1);
            }
        });
    }
    
    @FXML
    private void handleSendMessage()
    {
        try {
            String content = messageField.getText().trim();
            if (!content.isEmpty())
            {
                Message message = new Message(DataCenter.getInstance().getcurrentUserId(), DataCenter.getInstance().getCurrentConversationId(), content, MessageType.TEXT, LocalDateTime.now(), true, null, null);
                messageField.clear();

                MessageServices.getInstance().sendMessage(message);

                Platform.runLater(() ->
                {
//                    chatBox.scrollTo(DataCenter.getInstance().getMessages().size() );

                    if (!DataCenter.getInstance().getMessages().isEmpty())
                        chatBox.scrollTo(DataCenter.getInstance().getMessages().size()-1 );
                });
            }
        } catch (Exception e) {
            System.err.println("Error in handleSendMessage: " + e.getMessage());
        }
    }

    @FXML
    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            handleSendMessage();
        }
    }
    @FXML
    public void showProfile(MouseEvent mouseEvent) {
        System.out.println("profile btn is clicked");
        // Opens the user info scene in a new, undecorated stage.
        SceneManager.getInstance().showUserInfoSceneInNewStage();
    }

    @FXML
    public void home_action(MouseEvent mouseEvent) {
        System.out.println("home btn is clicked");
        // Switches the primary stage to the home scene.
        SceneManager.getInstance().showPrimaryScene();
    }

    @FXML
    public void notification_action(MouseEvent mouseEvent) {
        System.out.println("notification btn is clicked");
        // Switches the primary stage to the notifications scene.
        SceneManager.getInstance().showNotificationScene();
    }

    @FXML
    public void contact_action(MouseEvent mouseEvent) {
        System.out.println("contact btn is clicked");
        // Switches the primary stage to the contact scene.
        SceneManager.getInstance().showContactScene();
    }

    @FXML
    public void chatbot_action(MouseEvent mouseEvent) {
        System.out.println("chatbot btn is clicked");
        // Unfinished action – leave as is for now.
    }

    @FXML
    public void settings_action(MouseEvent mouseEvent) {
        System.out.println("settings btn is clicked");
        // Unfinished action – leave as is for now.
    }

    @FXML
    public void logout_action(MouseEvent mouseEvent) throws RemoteException {
        Settings.clearCredentials();
        ServerConnection.getServer().logout(DataCenter.getInstance().getcurrentUserId());
        SceneManager.getInstance().showRememberedSignIn();
    }
    @FXML
    public void addEmoji(MouseEvent event)
    {
        ContextMenu emojiMenu = new ContextMenu();

        String[] emojis = {"😊", "😂", "👍", "🔥", "🎉", "🙏", "🤔","👏"};

        for (String emoji : emojis) {
            MenuItem item = new MenuItem(emoji);
            item.setOnAction(e -> messageField.appendText(emoji));
            emojiMenu.getItems().add(item);
        }

        emojiMenu.show(emoji, Side.BOTTOM, 0, 0);
    }

    @FXML
    public void attachFile(MouseEvent event)
    {
        ContextMenu attachMenu = new ContextMenu();

        String[] types = {"Document", "Image", "Audio", "Video"};

        for (String type : types) {
            MenuItem item = new MenuItem(type);
            item.setOnAction(e -> openFileChooser(type));
            attachMenu.getItems().add(item);
        }
        attachMenu.show(attachFile, Side.BOTTOM, 0, 0);
    }

    private void openFileChooser(String type)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + type + " File");
        MessageType messageType = null;
        switch (type) {
            case "Document":
                messageType = MessageType.DOCUMENT;
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.docx", "*.txt"));
                break;
            case "Image":
                messageType = MessageType.IMAGE;
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg", "*.gif"));
                break;
            case "Audio":
                messageType = MessageType.AUDIO;
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Audio", "*.mp3", "*.wav", "*.ogg"));
                break;
            case "Video":
                messageType = MessageType.VIDEO;
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Videos", "*.mp4", "*.avi", "*.mkv"));
                break;
        }

        selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null)
        {
            Message message = new Message(DataCenter.getInstance().getcurrentUserId(), DataCenter.getInstance().getCurrentConversationId(), "file", messageType, LocalDateTime.now(), true, null, null);
            FileMessage fileMessage = new FileMessage(selectedFile.getName(), selectedFile.length(), selectedFile.getAbsolutePath());
                new Thread(() ->
                {
                    try
                    {
                        byte[] fileData = Files.readAllBytes(selectedFile.toPath());

                        boolean success = MessageServices.getInstance().uploadFile(
                                fileMessage,
                                message,
                                fileData,
                                DataCenter.getInstance().getCurrentConversationId()
                        );

                        Platform.runLater(() ->
                        {
                            if (success) {
                                int fileId = MessageServices.getInstance().sendFile(fileMessage, message);
                                Node source = (Node) attachFile.getScene().getRoot();
                                Stage stage = (Stage) source.getScene().getWindow();
                                NotificationPopup.show(
                                        stage,
                                        "File status",
                                        "Upload completed successfully!",
                                        NotificationPopup.NotificationType.INFO
                                );
                            } else {
                                NotificationPopup.show((Stage) attachFile.getScene().getWindow(), "Error","Upload failed", NotificationPopup.NotificationType.INFO);
                            }
                        });
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            NotificationPopup.show((Stage) attachFile.getScene().getWindow(), "Error","Error reading file", NotificationPopup.NotificationType.INFO);
                            if (!DataCenter.getInstance().getMessages().isEmpty()) {
                                chatBox.scrollTo(DataCenter.getInstance().getMessages().size() - 1);
                            }
                        });
                    }
                }).start();
            } else
                handleError("Error sending file: ", null);
    }


    private void handleError(String message, Exception e)
    {
        System.err.println(message);
        AlertNotifier.createAlert("Error", message, null);
    }

}