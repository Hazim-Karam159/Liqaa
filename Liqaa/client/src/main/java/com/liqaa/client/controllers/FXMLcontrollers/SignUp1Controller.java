package com.liqaa.client.controllers.FXMLcontrollers;

import com.liqaa.client.util.SceneManager;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.enums.Gender;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ResourceBundle;

public class SignUp1Controller implements Initializable {

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label NameLabel;
    @FXML
    private Label PhoneLabel;
    @FXML
    private PasswordField ConfirmationpasswordField;
    @FXML
    private Button NextButton;
    @FXML
    private Label PasswordLabel;
    @FXML
    private Label EmailLabel;
    @FXML
    private Button SignInButton;
    @FXML
    private TextField CountryField;
    @FXML
    private ComboBox<String> GenderField;
    @FXML
    private Button SignUpButton;
    @FXML
    private TextField NameField;
    @FXML
    private TextField EmailField;
    @FXML
    private TextField PhoneField;
    @FXML
    private DatePicker DateField;
    @FXML
    private Label CountryLabel;
    @FXML
    private Label ConfirmationPasswordLabel;

    // Added missing error labels:
    @FXML
    private Label GenderLabel;
    @FXML
    private Label DateLabel;

    private User tempUser;
    public static User User1 =new User();

    private static final Font ERROR_FONT = new Font("Arial", 10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(() -> {
            if (GenderField != null) {
                GenderField.getItems().clear();
                GenderField.getItems().addAll("male", "female");
            }
            clearAllErrors();
            setupValidationListeners();
            updateNextButtonState();
        });
    }

    private void setupValidationListeners() {
        if (NameField != null) {
            NameField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!UserInfoValidation.isVaildName(newVal)) {
                    showFieldError(NameLabel, "Please enter characters only");
                } else {
                    NameLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (PhoneField != null) {
            PhoneField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!UserInfoValidation.isValidPhoneNumber(newVal)) {
                    showFieldError(PhoneLabel, "Please enter a valid phone number");
                } else {
                    PhoneLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (EmailField != null) {
            EmailField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!UserInfoValidation.isVaildEmail(newVal)) {
                    showFieldError(EmailLabel, "Please enter a valid email");
                } else {
                    EmailLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (passwordField != null) {
            passwordField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!UserInfoValidation.isValidPassword(newVal)) {
                    showFieldError(PasswordLabel, "Password must be at least 6 characters with letters and numbers");
                } else {
                    PasswordLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (ConfirmationpasswordField != null) {
            ConfirmationpasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (!passwordField.getText().equals(newVal)) {
                    showFieldError(ConfirmationPasswordLabel, "Passwords do not match");
                } else {
                    ConfirmationPasswordLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (CountryField != null) {
            CountryField.textProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null || newVal.trim().isEmpty()) {
                    showFieldError(CountryLabel, "Please enter a country");
                } else {
                    CountryLabel.setText("");
                }
                updateNextButtonState();
            });
        }

        if (GenderField != null) {
            GenderField.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) {
                    showFieldError(GenderLabel, "Please select a gender");
                } else {
                    GenderLabel.setText("Male");
                }
                updateNextButtonState();
            });
        }

        if (DateField != null) {
            DateField.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal == null) {
                    showFieldError(DateLabel, "Please select a date of birth");
                } else {
                    DateLabel.setText("");
                }
                updateNextButtonState();
            });
        }
    }

    private void showFieldError(Label label, String message) {
        if (label != null) {
            label.setFont(ERROR_FONT);
            label.setTextFill(Color.RED);
            label.setText(message);
        }
    }

    private void clearAllErrors() {
        Label[] labels = {
                EmailLabel, NameLabel, PhoneLabel, PasswordLabel,
                ConfirmationPasswordLabel, CountryLabel, GenderLabel, DateLabel
        };
        for (Label label : labels) {
            if (label != null) {
                label.setText("");
            }
        }
    }

    private void updateNextButtonState() {
        if (NextButton != null) {
            NextButton.setDisable(!isFormValid());
        }
    }

    private boolean isFormValid() {
        boolean isValid = NameField != null && NameField.getText() != null && !NameField.getText().isEmpty() &&
                PhoneField != null && PhoneField.getText() != null && !PhoneField.getText().isEmpty() &&
                EmailField != null && EmailField.getText() != null && !EmailField.getText().isEmpty() &&
                passwordField != null && passwordField.getText() != null && !passwordField.getText().isEmpty() &&
                ConfirmationpasswordField != null && ConfirmationpasswordField.getText() != null && !ConfirmationpasswordField.getText().isEmpty() &&
                CountryField != null && CountryField.getText() != null && !CountryField.getText().isEmpty() &&
                GenderField != null && GenderField.getValue() != null &&
                DateField != null && DateField.getValue() != null;

        System.out.println("isFormValid: " + isValid);
        return isValid;
    }

    @FXML
    public void setSignInButtonOnAction(ActionEvent event) {
        SceneManager.getInstance().showSignInScene();
    }

    @FXML
    public void setNextButtonOnAction(ActionEvent event) {
        if (validateAllFields()) {
            User1= createTempUser();
            SceneManager.getInstance().showSignUpScene2();
        }
    }

    private boolean validateAllFields() {
        boolean isValid = true;

        if (NameField == null || !UserInfoValidation.isVaildName(NameField.getText())) {
            showFieldError(NameLabel, "Please enter characters only");
            isValid = false;
        }

        if (PhoneField == null || !UserInfoValidation.isValidPhoneNumber(PhoneField.getText())) {
            showFieldError(PhoneLabel, "Please enter a valid phone number");
            isValid = false;
        }

        if (EmailField == null || !UserInfoValidation.isVaildEmail(EmailField.getText())) {
            showFieldError(EmailLabel, "Please enter a valid email");
            isValid = false;
        }

        if (passwordField == null || !UserInfoValidation.isValidPassword(passwordField.getText())) {
            showFieldError(PasswordLabel, "Password must be at least 6 characters with letters and numbers");
            isValid = false;
        }

        if (passwordField == null || ConfirmationpasswordField == null ||
                !passwordField.getText().equals(ConfirmationpasswordField.getText())) {
            showFieldError(ConfirmationPasswordLabel, "Passwords do not match");
            isValid = false;
        }

        if (CountryField == null || CountryField.getText() == null || CountryField.getText().trim().isEmpty()) {
            showFieldError(CountryLabel, "Please enter a country");
            isValid = false;
        }

        if (GenderField == null || GenderField.getValue() == null) {
            showFieldError(GenderLabel, "Please select a gender");
            isValid = false;
        }

        if (DateField == null || DateField.getValue() == null) {
            showFieldError(DateLabel, "Please select a date of birth");
            isValid = false;
        }

        if (!isValid) {
            showAlert("Please correct the errors before proceeding.");
        }

        return isValid;
    }

    private User createTempUser() {
        tempUser = new User();
        if (NameField != null) tempUser.setDisplayName(NameField.getText());
        if (PhoneField != null) tempUser.setPhoneNumber(PhoneField.getText());
        if (EmailField != null) tempUser.setEmail(EmailField.getText());
        if (passwordField != null) tempUser.setPasswordHash(passwordField.getText());
        if (CountryField != null) tempUser.setCountry(CountryField.getText());

        if (GenderField != null && GenderField.getValue() != null) {
            String genderInput = GenderField.getValue().toString();
            tempUser.setGender(Gender.valueOf(genderInput.toUpperCase()));
        }

        if (DateField != null && DateField.getValue() != null) {
            tempUser.setDateofBirth(java.sql.Date.valueOf(DateField.getValue().toString()));
        }
        return tempUser;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Registration Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
}
