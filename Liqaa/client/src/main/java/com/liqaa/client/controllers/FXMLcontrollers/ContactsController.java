package com.liqaa.client.controllers.FXMLcontrollers;

import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.util.SceneManager;
import com.liqaa.shared.models.entities.*;
import com.liqaa.shared.models.enums.NotificationType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import com.liqaa.client.controllers.services.implementations.ContactServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;
import javafx.scene.control.Dialog;
import com.liqaa.shared.models.Contact;



public class ContactsController implements Initializable {
    @FXML
    private ListView<Contact> contactsList;

    @FXML
    private TextField searchField; // تأكد من وجود fx:id في الـ FXML

    @FXML
    private Circle profilePhoto;
    // القائمة الأصلية للجهات الاتصال
    private ObservableList<Contact> originalContactsList = FXCollections.observableArrayList();

    // القائمة المفلترة
    private FilteredList<Contact> filteredContactsList = new FilteredList<>(originalContactsList, p -> true);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        byte[] imageData = DataCenter.getInstance().getCurrentUser().getProfilepicture();
        if (imageData == null) {
            Image defaultProfileImage = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png"));
            profilePhoto.setFill(new ImagePattern(defaultProfileImage));
        } else {
            Image profileImage = new Image(new ByteArrayInputStream(imageData));
            profilePhoto.setFill(new ImagePattern(profileImage));
        }
        profilePhoto.setStroke(null);
        setupListView();
        try {
            populateContacts();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // ربط القائمة المفلترة بالـ ListView
        contactsList.setItems(filteredContactsList);

        // إضافة Listener على TextField
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredContactsList.setPredicate(contact -> {
                // إذا كان النص المكتوب فارغ، عرض كل الجهات الاتصال
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // تحويل النص المكتوب والنص في القائمة إلى حروف صغيرة (Case Insensitive)
                String lowerCaseFilter = newValue.toLowerCase();

                // التحقق من وجود النص في الاسم أو الرقم
                if (contact.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // عرض الجهة الاتصال إذا كانت تطابق البحث
                } else if (contact.getPhoneNumber().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // عرض الجهة الاتصال إذا كانت تطابق البحث
                }

                return false; // إخفاء الجهة الاتصال إذا لم تكن تطابق البحث
            });
        });
    }
    // Method to set the logo for a dialog


    // Method to set the logo for a dialog with a specific color
    private void setDialogLogo(Dialog<?> dialog, double hue, double saturation, double brightness, double contrast) {
        ImageView logoView = new ImageView(new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/logo.png")));
        logoView.setFitHeight(40); // Adjust size as needed
        logoView.setFitWidth(40); // Adjust size as needed

        // Apply color adjustment
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(hue);       // تغيير درجة اللون (-1 إلى 1)
        colorAdjust.setSaturation(saturation); // تغيير التشبع (-1 إلى 1)
        colorAdjust.setBrightness(brightness); // تغيير السطوع (-1 إلى 1)
        colorAdjust.setContrast(contrast);     // تغيير التباين (-1 إلى 1)

        logoView.setEffect(colorAdjust); // تطبيق التأثير على الصورة
        dialog.setGraphic(logoView); // Set the logo as the graphic of the dialog
    }
    private void setupListView() {
        contactsList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Contact> call(ListView<Contact> param) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            setGraphic(createContactCell(item));
                        }
                    }
                };
            }
        });
    }


    private HBox createContactCell(Contact item) {
        HBox hBox = new HBox(10); // تقليل المسافة بين العناصر
        hBox.setAlignment(Pos.CENTER_LEFT);

        // صورة المستخدم مع الحالة
        ImageView imageView = new ImageView(item.getPhoto());
        imageView.setFitHeight(25);
        imageView.setFitWidth(25);

        Circle circle = new Circle(4);
        circle.setFill(item.getStatus().equals("Available") ? Color.GREEN : Color.RED);
        StackPane photoWithStatus = new StackPane(imageView, circle);
        StackPane.setAlignment(circle, Pos.BOTTOM_RIGHT);

        // النصوص
        Text nameText = new Text(item.getName());
        nameText.setWrappingWidth(100); // تحديد عرض ثابت للاسم

        Text phoneText = new Text(item.getPhoneNumber());
        phoneText.setWrappingWidth(120); // تحديد عرض ثابت للرقم

        Text bioText = new Text(item.getBio());
        bioText.setWrappingWidth(150); // تحديد عرض ثابت للـ Bio

        String status = "";
        if(item.getStatus().equals("AVAILABLE")) status = "Available";
        if(item.getStatus().equals("BUSY")) status = "Busy";
        if(item.getStatus().equals("AWAY")) status = "Away";
        if(item.getStatus().equals("OFFLINE")) status = "Offline";
        Text statusText = new Text(status);
        statusText.setWrappingWidth(80); // تحديد عرض ثابت للحالة

        Text categoryText = new Text(item.getCategory());
        categoryText.setWrappingWidth(100); // تحديد عرض ثابت للفئة

        // الأيقونات
        ImageView blockIcon = new ImageView(item.getBlock());
        blockIcon.setFitHeight(20);
        blockIcon.setFitWidth(20);

        ImageView editIcon = new ImageView(item.getEdit());
        editIcon.setFitHeight(20);
        editIcon.setFitWidth(20);

        ImageView deleteIcon = new ImageView(item.getDelete());
        deleteIcon.setFitHeight(20);
        deleteIcon.setFitWidth(20);

        hBox.getChildren().addAll(
                photoWithStatus, nameText, phoneText, bioText, statusText, categoryText, editIcon, blockIcon, deleteIcon
        );

        // 3 on right actions
        editIcon.setOnMouseClicked(event -> {
            System.out.println("edit");
            try {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Edit Contact Category");
                dialog.setHeaderText("Enter the categories names separated by comma:");
                dialog.setContentText("Category:");
                dialog.setGraphic(null);

                setDialogLogo(dialog, 0.1, 0.3, 0.50, 0.0);
                // Apply custom styles to the dialog
                dialog.getDialogPane().getStylesheets().add(
                        getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
                );

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(categoryName -> {
                    StringBuilder fin = new StringBuilder();
                    String[] categoriesNames = categoryName.split("\\s*,\\s*");
                    for(String category: categoriesNames){
                        try {
                            Boolean ok = false;
                            for (Category cat: ContactServiceImpl.getInstance().getUserCategories(DataCenter.getInstance().getcurrentUserId())){
                                if(cat.getCategoryName().equals(category)) ok = true;
                            }
                            if(ok){
                                fin.append(category + ", ");
                            }
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    if (fin.length() > 1) {
                        fin.deleteCharAt(fin.length() - 1);
                        fin.deleteCharAt(fin.length() - 1);
                    }
                    item.setCategory(fin.toString());
                });
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error in addCategoryAction: " + e.getMessage());
            }
        });

        blockIcon.setOnMouseClicked(event -> {
            System.out.println("block");
            int contactId = 0;
            try {
                contactId = ServerConnection.getServer().getUserInfo(item.getPhoneNumber()).getId();
                Boolean curStatus = ServerConnection.getServer().isBlocked(DataCenter.getInstance().getcurrentUserId(), contactId);
                if (curStatus){ // blocked
                    ServerConnection.getServer().unblockContact(DataCenter.getInstance().getcurrentUserId(), contactId);
                    Image blockImg = new Image(getClass().getResource("/com/liqaa/client/view/images/block.png").toString());
                    item.setBlock(blockImg);
                }
                else{
                    ServerConnection.getServer().blockContact(DataCenter.getInstance().getcurrentUserId(), contactId);
                    Image unblockImg = new Image(getClass().getResource("/com/liqaa/client/view/images/unblock.png").toString());
                    item.setBlock(unblockImg);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        deleteIcon.setOnMouseClicked(event -> {
            System.out.println("delete");
            try {
                int contactId = ServerConnection.getServer().getUserInfo(item.getPhoneNumber()).getId();
                ServerConnection.getServer().deleteContact(DataCenter.getInstance().getcurrentUserId(), contactId);
                originalContactsList.remove(item);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });

        return hBox;
    }

    private void populateContacts() throws RemoteException {
        List<User> contacts = ContactServiceImpl.getInstance().getUserFriends(DataCenter.getInstance().getcurrentUserId());
        for(User user : contacts){
            byte[] imageData = user.getProfilepicture();
            Image profileImage;
            if (imageData == null) {
                profileImage = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png"));
            } else {
                profileImage = new Image(new ByteArrayInputStream(imageData));
            }
            List<Category> userCategories = ContactServiceImpl.getInstance().getUserCategories(user.getId());
            String categoriesStr = convertCategoriesToString(userCategories);
            Boolean curStatus = ServerConnection.getServer().isBlocked(DataCenter.getInstance().getcurrentUserId(), user.getId());
            Image img;
            if(curStatus){ // blocked
                img = new Image(getClass().getResource("/com/liqaa/client/view/images/unblock.png").toString());
            }
            else{
                img = new Image(getClass().getResource("/com/liqaa/client/view/images/block.png").toString());
            }
            originalContactsList.add(new Contact(profileImage, user.getDisplayName(), user.getPhoneNumber(), user.getBio(), user.getCurrentstatus().toString(), categoriesStr, new Image(getClass().getResource("/com/liqaa/client/view/images/edit.png").toString()), img, new Image(getClass().getResource("/com/liqaa/client/view/images/delete.png").toString())));
        }
    }

    // Event handlers for the buttons
    @FXML
    private void profile_action(MouseEvent event) {
        SceneManager.getInstance().showUserInfoSceneInNewStage();
    }

    @FXML
    private void home_action(MouseEvent event) {
        SceneManager.getInstance().showPrimaryScene();
    }

    @FXML
    private void notification_action(MouseEvent event) {
        SceneManager.getInstance().showNotificationScene();
    }

    @FXML
    private void contact_action(MouseEvent event) {
//        SceneManager.getInstance().showContactScene();
    }

    @FXML
    private void chatbot_action(MouseEvent event) {
       // todo: SceneManager.getInstance().showChatBotScene();
    }

    @FXML
    private void settings_action(MouseEvent event) {
//    todo:    SceneManager.getInstance().showSettingsScene();
    }

    @FXML
    private void logout_action(MouseEvent event) {
        System.out.println("Logout button clicked!");
    }

    private String convertCategoriesToString(List<Category> userCategories){
        StringBuilder categories = new StringBuilder();
        for(int i=0; i<(int)userCategories.size(); i++){
            categories.append(userCategories.get(i).getCategoryName());
            if(i != (int)userCategories.size() - 1){
                categories.append(", ");
            }
        }
        return categories.toString();
    }

    @FXML
    private void addContactAction() {
        try {
            // Create the dialog
            Dialog<Contact> dialog = new Dialog<>();
            dialog.setTitle("Add Contact");
            dialog.setHeaderText("Enter the contact details:");
            setDialogLogo(dialog, 0.1, 0.3, 0.50, 0.0);

            ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

            // Apply custom styles to the dialog
            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
            );

            // Create the fields
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Phone Field
            TextField phoneField = new TextField();
            phoneField.setPromptText("Phone Number");

            // Add the phone field to the grid
            grid.add(new Label("Phone:"), 0, 0);
            grid.add(phoneField, 1, 0);

            // Set the grid as the dialog content
            dialog.getDialogPane().setContent(grid);

            // Convert the result to a Contact object
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == addButton) {
                    User addedContact;
                    try {
                        addedContact = ServerConnection.getServer().getUserInfo(phoneField.getText());
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    byte[] imageData = addedContact.getProfilepicture();
                    Image profileImage;
                    if (imageData == null) {
                        profileImage = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png"));
                    } else {
                        profileImage = new Image(new ByteArrayInputStream(imageData));
                    }
                    Image img;
                    try {
                        if(ServerConnection.getServer().isBlocked(DataCenter.getInstance().getcurrentUserId(), addedContact.getId())){ // blocked
                            img = new Image(getClass().getResource("/com/liqaa/client/view/images/unblock.png").toString());
                        }
                        else{
                            img = new Image(getClass().getResource("/com/liqaa/client/view/images/block.png").toString());
                        }
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        ServerConnection.getServer().sendFriendRequest(new FriendRequests(DataCenter.getInstance().getcurrentUserId(), addedContact.getId()));
                        ServerConnection.getServer().createNotification(new Notification(addedContact.getId(), DataCenter.getInstance().getcurrentUserId(), NotificationType.FRIEND_REQUEST, false));
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return new Contact(
                            profileImage,
                            addedContact.getDisplayName(),
                            addedContact.getPhoneNumber(),
                            addedContact.getBio(),
                            addedContact.getCurrentstatus().toString(),
                            "", // Category
                            new Image(getClass().getResource("/com/liqaa/client/view/images/edit.png").toString()),
                            img,
                            new Image(getClass().getResource("/com/liqaa/client/view/images/delete.png").toString())
                    );
                }
                return null;
            });

            // Show the dialog and handle the result
            Optional<Contact> result = dialog.showAndWait();
            result.ifPresent(contact -> {
                originalContactsList.add(contact); // Add the new contact to the original list
                System.out.println("New Contact: " + contact.getName() + " - " + contact.getPhoneNumber());
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in addContactAction: " + e.getMessage());
        }
    }

    @FXML
    private void addGroupAction() {
        System.out.println("add group is clicked.........");
        try {
            // Create the dialog
            Dialog<Map<String, Object>> dialog = new Dialog<>();
            dialog.setTitle("Add Group");
            dialog.setHeaderText("Create a new group");
            dialog.setGraphic(null); // Remove the question mark icon

            setDialogLogo(dialog, 0.1, 0.3, 0.50, 0.0);

            ButtonType createButton = new ButtonType("Create", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(createButton, ButtonType.CANCEL);

            // Apply custom styles to the dialog
            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
            );

            // Create the layout
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            // Group Image with Label
            HBox groupImageBox = new HBox(10);
            groupImageBox.setAlignment(Pos.CENTER_LEFT);

            Group newGroup = new Group();
            Image defaultGroupImg = new Image(getClass().getResource("/com/liqaa/client/view/images/defaultGroupProfile.png").toString());
            ImageView groupImageView = new ImageView(defaultGroupImg);
            Path imagePath = Paths.get(getClass().getResource("/com/liqaa/client/view/images/defaultGroupProfile.png").toURI());
            byte[] groupPicture = Files.readAllBytes(imagePath);

            newGroup.setImage(groupPicture);
            groupImageView.setFitHeight(100);
            groupImageView.setFitWidth(100);
            groupImageView.setPreserveRatio(true);
            groupImageView.setCursor(Cursor.HAND);


            Circle clip = new Circle(50, 50, 50); // Circle(centerX, centerY, radius)
            groupImageView.setClip(clip);


            DropShadow dropShadow = new DropShadow();
            dropShadow.setRadius(10);
            dropShadow.setOffsetX(0);
            dropShadow.setOffsetY(0); // إزاحة رأسية
            dropShadow.setColor(Color.color(0, 0, 0, 0.3)); // لون الظل (أسود مع شفافية)
            groupImageView.setEffect(dropShadow);

            Label chooseProfileLabel = new Label("Choose Group Profile");
            chooseProfileLabel.setStyle("-fx-text-fill: #384E6A; -fx-font-size: 14px;");
            chooseProfileLabel.setCursor(Cursor.HAND); // تغيير شكل المؤشر إلى يد عند التمرير فوق النص

            // Event handler لفتح FileChooser عند النقر على الصورة أو النص
            EventHandler<MouseEvent> openFileChooserHandler = event -> {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Choose Group Image");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
                );
                File selectedFile = fileChooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
                if (selectedFile != null) {
                    Image selectedImg = new Image(selectedFile.toURI().toString());
                    groupImageView.setImage(selectedImg);
                    byte[] newGroupPicture = null;
                    try {
                        newGroupPicture = Files.readAllBytes((Path) selectedImg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    newGroup.setImage(newGroupPicture);
                }
            };

            // إضافة معالج الحدث للصورة والنص
            groupImageView.setOnMouseClicked(openFileChooserHandler);
            chooseProfileLabel.setOnMouseClicked(openFileChooserHandler);

            groupImageBox.getChildren().addAll(groupImageView, chooseProfileLabel);

            // Name Field
            TextField nameField = new TextField();
            nameField.setPromptText("Group Name");

            // Description Field
            TextField descriptionField = new TextField();
            descriptionField.setPromptText("Group Description");

            // Choose Contacts Button
            Button chooseContactsButton = new Button("Choose Contacts");
            chooseContactsButton.setStyle("-fx-background-color: #384E6A; -fx-text-fill: white; -fx-font-weight: bold;");

            // List to store selected contacts
            ObservableList<Contact> selectedContacts = FXCollections.observableArrayList();

            chooseContactsButton.setOnAction(event -> {
                // Open a dialog to select contacts
                Dialog<Void> contactsDialog = new Dialog<>();
                contactsDialog.setTitle("Choose Contacts");
                contactsDialog.setHeaderText("Select contacts to add to the group");
                contactsDialog.setGraphic(null); // Remove the question mark icon

                // Apply custom styles to the dialog
                contactsDialog.getDialogPane().getStylesheets().add(
                        getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
                );

                // Set button types (Add and Close)
                ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
                ButtonType closeButton = new ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE);
                contactsDialog.getDialogPane().getButtonTypes().addAll(addButton, closeButton);

                // Create a ListView with custom cells
                ListView<Contact> contactsListView = new ListView<>();
                contactsListView.setItems(contactsList.getItems()); // Use the existing contacts list

                // Set custom cell factory to display contacts with images and checkboxes
                contactsListView.setCellFactory(param -> new ListCell<>() {
                    private final CheckBox checkBox = new CheckBox();
                    private final HBox hBox = new HBox(10);
                    private final ImageView imageView = new ImageView();
                    private final Text nameText = new Text();

                    {
                        hBox.setAlignment(Pos.CENTER_LEFT);
                        imageView.setFitHeight(25);
                        imageView.setFitWidth(25);
                        hBox.getChildren().addAll(checkBox, imageView, nameText); // CheckBox أول حاجة
                    }

                    @Override
                    protected void updateItem(Contact item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setGraphic(null);
                        } else {
                            imageView.setImage(item.getPhoto());
                            nameText.setText(item.getName());
                            checkBox.setSelected(selectedContacts.contains(item)); // Keep selected state
                            checkBox.setOnAction(e -> {
                                if (checkBox.isSelected()) {
                                    selectedContacts.add(item); // Add to selected contacts
                                } else {
                                    selectedContacts.remove(item); // Remove from selected contacts
                                }
                            });
                            setGraphic(hBox);
                        }
                    }
                });

                // Add the ListView to the dialog
                contactsDialog.getDialogPane().setContent(contactsListView);

                // Handle the Add button action
                contactsDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == addButton) {
                        System.out.println("Selected Contacts: " + selectedContacts.stream()
                                .map(Contact::getName)
                                .collect(Collectors.joining(", ")));
                    }
                    return null;
                });

                contactsDialog.showAndWait();
            });

            // Add components to the grid
            grid.add(groupImageBox, 0, 0, 2, 1);
            grid.add(new Label("Name:"), 0, 1);
            grid.add(nameField, 1, 1);
            grid.add(new Label("Description:"), 0, 2);
            grid.add(descriptionField, 1, 2);
            grid.add(chooseContactsButton, 0, 3, 2, 1);

            dialog.getDialogPane().setContent(grid);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == createButton) {
                    Map<String, Object> groupData = new HashMap<>();
                    groupData.put("image", groupImageView.getImage());
                    groupData.put("name", nameField.getText());
                    groupData.put("description", descriptionField.getText());
                    groupData.put("members", new ArrayList<>(selectedContacts)); // Store selected contacts

                    ArrayList<Integer> groupMembersIds = new ArrayList<>();
                    for(int i=0; i<(int)selectedContacts.size(); i++){
                        int userId = 0;
                        try {
                            userId = ServerConnection.getServer().getUserInfo(selectedContacts.get(i).getPhoneNumber()).getId();
                        } catch (RemoteException e) {
                            throw new RuntimeException(e);
                        }
                        groupMembersIds.add(userId);
                    }
                    try {
                        ServerConnection.getServer().createGroup(newGroup, groupMembersIds);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }

                    newGroup.setName(nameField.getText());
                    newGroup.setDescription(descriptionField.getText());
                    byte[] groupPic = null;
                    try {
                        //groupPic = Files.readAllBytes((Path) groupImageView.getImage());
                        try {
                            if (groupImageView.getImage() != null) {
                                String imageUrl = groupImageView.getImage().getUrl();
                                if (imageUrl.startsWith("file:/")) { // Ensure it's a local file
                                    Path imagePath2 = Paths.get(new URI(imageUrl));
                                    groupPic = Files.readAllBytes(imagePath2);
                                } else {
                                    System.err.println("Image is not from a local file, skipping byte conversion.");
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    newGroup.setImage(groupPic);
                    newGroup.setCreatedBy(DataCenter.getInstance().getcurrentUserId());
                    try {
                        ServerConnection.getServer().createGroup(newGroup, groupMembersIds);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                    return groupData;
                }
                return null;
            });

            Optional<Map<String, Object>> result = dialog.showAndWait().map(obj -> (Map<String, Object>) obj);


            result.ifPresent(groupData -> {
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in addGroupAction: " + e.getMessage());
        }
    }

    @FXML
    private void addCategoryAction() {
        try {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Add Category");
            dialog.setHeaderText("Enter the new category name:");
            dialog.setContentText("Category:");
            dialog.setGraphic(null);

            setDialogLogo(dialog, 0.1, 0.3, 0.50, 0.0);
            // Apply custom styles to the dialog
            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
            );

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(categoryName -> {
                List<Category> newCategory = new ArrayList<>();
                newCategory.add(new Category(DataCenter.getInstance().getcurrentUserId(), categoryName));
                try {
                    ContactServiceImpl.getInstance().addCategories(newCategory);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in addCategoryAction: " + e.getMessage());
        }
    }


    @FXML
    private void removeCategoryAction() {
        try {
            ComboBox<String> categoryComboBox = new ComboBox<>();
            categoryComboBox.setPromptText("Select a category to remove");
            List<Category> userCategories = ContactServiceImpl.getInstance().getCategories(DataCenter.getInstance().getcurrentUserId());
            for (Category category : userCategories) {
                categoryComboBox.getItems().add(category.getCategoryName());
            }

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Remove Category");
            dialog.setHeaderText("Select the category to remove:");

            // إضافة ID للـ Dialog
            dialog.getDialogPane().setId("removeCategoryDialog");

            // أو إضافة Class
            dialog.getDialogPane().getStyleClass().add("custom-dialog");

            dialog.getDialogPane().setContent(categoryComboBox);

            // إضافة أزرار Remove و Cancel
            dialog.getDialogPane().getButtonTypes().addAll(new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE), ButtonType.CANCEL);

            // تطبيق الاستايل
            dialog.getDialogPane().getStylesheets().add(
                    getClass().getResource("/com/liqaa/client/view/styles/contactStyle.css").toExternalForm()
            );

            Optional<ButtonType> result = dialog.showAndWait();
            if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                String selectedCategory = categoryComboBox.getValue();
                if (selectedCategory != null && !selectedCategory.isEmpty()) {
                    System.out.println("Category to remove: " + selectedCategory);
                    //removeCategoryFromDummyData(selectedCategory);
                    ServerConnection.getServer().removeCategory(selectedCategory, DataCenter.getInstance().getcurrentUserId());
                } else {
                    System.out.println("No category selected.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error in removeCategoryAction: " + e.getMessage());
        }
    }
}