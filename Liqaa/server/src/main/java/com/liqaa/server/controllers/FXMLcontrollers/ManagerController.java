package com.liqaa.server.controllers.FXMLcontrollers;

import com.liqaa.server.Main;
import com.liqaa.server.controllers.services.implementations.UserServicesImpl;
import com.liqaa.server.network.ServerConnection;
import com.liqaa.server.util.FilePaths;
import com.liqaa.server.util.SceneManager;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.CurrentStatus;
import com.liqaa.shared.models.enums.Gender;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class ManagerController {
    @javafx.fxml.FXML
    private Pane statisticsButton;
    @javafx.fxml.FXML
    private Pane announcementsButton;
    @javafx.fxml.FXML
    private Pane managerButton;
    @javafx.fxml.FXML
    private Button resetPasswordButton;
    @javafx.fxml.FXML
    private TableView usersDataTable;
    @FXML
    private Button OFFButton;
    @FXML
    private Button ONButton;
    @javafx.fxml.FXML
    private Pane logoutButton;
    @FXML
    private TableColumn<User, String> Name;

    @FXML
    private TableColumn<User, String> Email;

    @FXML
    private TableColumn<User, String> Phone;

    @FXML
    private TableColumn<User, String> Gender;

    @FXML
    private TableColumn<User, String> Birthday;

    @FXML
    private TableColumn<User, String> bio;

    @FXML
    private TableColumn<User, String> Country;

    @FXML
    private TableColumn<User, String> Status;

    @FXML
    private TableColumn<User, String> Avalabilty;

    private ObservableList<User> userList;

    private boolean serviceStatus = false;

    public void initialize() {
        // Initialize Table Columns
        Name.setCellValueFactory(new PropertyValueFactory<>("displayName"));
        Email.setCellValueFactory(new PropertyValueFactory<>("email"));
        Phone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        Gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        Birthday.setCellValueFactory(new PropertyValueFactory<>("dateofBirth")); // Correct case-sensitive match
        bio.setCellValueFactory(new PropertyValueFactory<>("bio"));
        Country.setCellValueFactory(new PropertyValueFactory<>("country"));
        Status.setCellValueFactory(new PropertyValueFactory<>("currentstatus")); // Matches getCurrentstatus()
        Avalabilty.setCellValueFactory(new PropertyValueFactory<>("active"));
        /*******View******************/
        Name.setPrefWidth(50);
        Email.setPrefWidth(50);
        Phone.setPrefWidth(50);
        bio.setPrefWidth(100);
        // Fetch and Load Data into Table
        userList = FXCollections.observableArrayList();
        usersDataTable.setItems(userList);
        // Start a background thread to monitor database changes
        startDatabaseListener();
    }

    private void startDatabaseListener() {
        // Run in a background thread to prevent UI blocking
        Thread dbListenerThread = new Thread(() -> {
            while (true) {
                try {
                    // Simulate polling interval (e.g., 1 second)
                    Thread.sleep(1000);

                    // Fetch updated data from the database
                    List<User> updatedUsers = UserServicesImpl.getInstance().getAllUsers();
                    // Update the table on the JavaFX Application Thread
                    Platform.runLater(() -> {
                        userList.setAll(updatedUsers);
                        //System.out.println("TableView updated with users: " + userList);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
        });
        dbListenerThread.setDaemon(true);
        dbListenerThread.start();
    }



    @FXML
    void handleOFFButton(ActionEvent event) {
        OFFButton.setDisable(true);
        ONButton.setDisable(false);
        ServerConnection.getInstance().stop();
        System.out.println("server Off");

    }

    @FXML
    void handleONButton(ActionEvent event) {
        OFFButton.setDisable(false);
        ONButton.setDisable(true);
        ServerConnection.getInstance().start();
        System.out.println("server On");

    }
    @FXML
    public void logout(Event event) {
        System.out.println("manager exit");
        SceneManager.getInstance().showLogin();
    }

    @FXML
    public void switchToStatistics(Event event) {
        System.out.println("switchToStatistics called");
        SceneManager.getInstance().showStatistics();
    }

    @FXML
    public void switchToAnnouncements(Event event) {
        SceneManager.getInstance().showAnnouncements();
    }

    @FXML
    void addUserButton(ActionEvent event) {
        // Create the dialog
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Add User");
        // Create the header label
        Label headerLabel = new Label("Enter user details below");
        headerLabel.setStyle(
                "-fx-background-color: #329ba2; " +
                        "-fx-text-fill: white; " +
                        "-fx-font-size: 18px; " +           // Font size
                        "-fx-padding: 10px; "               // Padding for spacing
        );
        //headerLabel.setMaxWidth(Double.MAX_VALUE); // Allow the label to stretch

// Load the logo into an ImageView
        ImageView logoView = new ImageView();
        logoView.setImage(new Image(getClass().getResource("/com/liqaa/server/view/images/logo.png").toExternalForm()));
        logoView.setFitWidth(60); // Set the logo width
        logoView.setFitHeight(60); // Set the logo height
        logoView.setPreserveRatio(false); // Maintain aspect ratio

// Create an HBox for the header
        HBox headerSection = new HBox();
        headerSection.setStyle(
                "-fx-background-color: #329ba2; " +
                        "-fx-padding:10px; "                // Padding for spacing
        );
        headerSection.setAlignment(Pos.CENTER_LEFT); // Align the content to the left
        headerSection.setSpacing(180); // Add spacing between the label and logo

// Add the label and logo to the HBox
        headerSection.getChildren().addAll(headerLabel, logoView);

// Set the headerSection as the header of the dialog
        dialog.getDialogPane().setHeader(headerSection);

        // Create buttons
        // Create the "Add" button with custom style
        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

// Apply custom styles to buttons
        dialog.getDialogPane().lookupButton(addButtonType).setStyle(
                "-fx-background-color: #329ba2; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-font-size: 14px;"
        );

        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle(
                "-fx-background-radius: 15; " +
                        "-fx-font-size: 14px;"
        );


        // Create the form fields
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        phoneField.setMaxWidth(200);

        TextField nameField = new TextField();
        nameField.setPromptText("Display Name");
        nameField.setMaxWidth(200);

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setMaxWidth(200);

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(200);

        PasswordField confirmPasswordField = new PasswordField();
        confirmPasswordField.setPromptText("Confirm Password");
        confirmPasswordField.setMaxWidth(200);

        TextField countryField = new TextField();
        countryField.setPromptText("Country");
        countryField.setMaxWidth(200);

        TextArea bioField = new TextArea();
        bioField.setPromptText("Bio");
        bioField.setPrefRowCount(3);
        bioField.setMaxWidth(200);

        ComboBox<String> genderBox = new ComboBox<>();
        genderBox.getItems().addAll("MALE", "FEMALE");
        genderBox.setPromptText("Select Gender");
        genderBox.setMaxWidth(200);

        DatePicker dateOfBirthPicker = new DatePicker();
        dateOfBirthPicker.setPromptText("Date of Birth");
        dateOfBirthPicker.setMaxWidth(200);

        CheckBox isActiveCheckBox = new CheckBox("Is Active");

        ComboBox<CurrentStatus> statusBox = new ComboBox<>();
        statusBox.getItems().addAll(CurrentStatus.AVAILABLE, CurrentStatus.AWAY, CurrentStatus.BUSY, CurrentStatus.OFFLINE);
        statusBox.setPromptText("Select Status");
        statusBox.setMaxWidth(200);

        Button uploadImageButton = new Button("Upload Image");
        uploadImageButton.setStyle(
                "-fx-background-color: #329ba2; " +
                        "-fx-text-fill: white; " +
                        "-fx-background-radius: 15; " +
                        "-fx-font-size: 14px;"
        );
        ImageView imageView = new ImageView();
        imageView.setFitHeight(100);
        imageView.setFitWidth(100);

        // Handle image upload
        uploadImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                Image image = new Image(selectedFile.toURI().toString());
                imageView.setImage(image);
                imageView.setUserData(selectedFile); // Store the file for later use
            }
            else
            {
                selectedFile=null;
            }
        });

        // Layout the form
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);

        // Top section for profile picture
        HBox imageLayout = new HBox(10, imageView, uploadImageButton);
        imageLayout.setAlignment(Pos.CENTER);

        // Form fields layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("Phone:"), 0, 0);
        grid.add(phoneField, 1, 0);

        grid.add(new Label("Display Name:"), 0, 1);
        grid.add(nameField, 1, 1);

        grid.add(new Label("Email:"), 0, 2);
        grid.add(emailField, 1, 2);

        grid.add(new Label("Password:"), 0, 3);
        grid.add(passwordField, 1, 3);

        grid.add(new Label("Confirm Password:"), 0, 4);
        grid.add(confirmPasswordField, 1, 4);

        grid.add(new Label("Country:"), 0, 5);
        grid.add(countryField, 1, 5);

        grid.add(new Label("Bio:"), 0, 6);
        grid.add(bioField, 1, 6);

        grid.add(new Label("Gender:"), 0, 7);
        grid.add(genderBox, 1, 7);

        grid.add(new Label("Date of Birth:"), 0, 8);
        grid.add(dateOfBirthPicker, 1, 8);

        grid.add(new Label("Status:"), 0, 9);
        grid.add(statusBox, 1, 9);

        grid.add(isActiveCheckBox, 1, 10);

        layout.getChildren().addAll(imageLayout, grid);
        dialog.getDialogPane().setContent(layout);

        // Enable/Disable Add button based on validation
        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);
        addButton.setDisable(true);

        // Validate phone number and fields
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,15}")) {
                phoneField.setText(oldValue);
            }
            validateFields(addButton, nameField, phoneField, emailField, passwordField, confirmPasswordField);
        });

        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFields(addButton, nameField, phoneField, emailField, passwordField, confirmPasswordField);
        });

        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateFields(addButton, nameField, phoneField, emailField, passwordField, confirmPasswordField);
        });

        // Handle dialog button clicks
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Password Mismatch");
                    alert.setHeaderText(null);
                    alert.setContentText("Passwords do not match!");
                    alert.showAndWait();
                    return null;
                }

                File profileImageFile = (File) imageView.getUserData();
                byte[] profilePicture = null;

                // Convert image file to byte array
                if (profileImageFile != null) {
                    try {
                        profilePicture = Files.readAllBytes(profileImageFile.toPath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                // Map gender to enum
                Gender gender = null;
                String selectedGender = genderBox.getValue();
                if (selectedGender != null) {
                    switch (selectedGender) {
                        case "MALE":
                            gender = com.liqaa.shared.models.enums.Gender.MALE;
                            break;
                        case "FEMALE":
                            gender = com.liqaa.shared.models.enums.Gender.FEMALE;
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid gender: " + selectedGender);
                    }
                }

                // Create user object
                User user = new User(
                        phoneField.getText(),
                        nameField.getText(),
                        emailField.getText(),
                        passwordField.getText(),
                        countryField.getText(),
                        bioField.getText(),
                        gender,
                        (dateOfBirthPicker.getValue() != null) ? java.sql.Date.valueOf(dateOfBirthPicker.getValue()) : null,
                        isActiveCheckBox.isSelected(),
                        statusBox.getValue(),
                        profilePicture,
                        LocalTime.now()
                );

                System.out.println(user);
                UserServicesImpl.getInstance().signUp(user);
            }
            return null;
        });

        dialog.showAndWait();
    }

    // Helper method to validate fields
    private void validateFields(Button addButton, TextField nameField, TextField phoneField, TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField) {
        boolean isDisabled = nameField.getText().trim().isEmpty() ||
                phoneField.getText().trim().isEmpty() ||
                emailField.getText().trim().isEmpty() ||
                passwordField.getText().trim().isEmpty() ||
                confirmPasswordField.getText().trim().isEmpty();
        addButton.setDisable(isDisabled);
    }


    public void switchToManager(MouseEvent mouseEvent) {

    }
}
