package com.liqaa.client.controllers.FXMLcontrollers;

import com.liqaa.client.controllers.FXMLcontrollers.components.NotificationCard1Controller;
import com.liqaa.client.controllers.FXMLcontrollers.components.NotificationCard2Controller;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import com.liqaa.client.controllers.services.implementations.NotificationServiceImpl;
import com.liqaa.client.util.SceneManager;
import com.liqaa.shared.models.entities.Notification;
import com.liqaa.shared.models.entities.User;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;


public class NotificationsController implements Initializable {
    static Stage myStage;
    @FXML
    private Button allBtn;

    @FXML
    private Button reqBtn;

    @FXML
    private Button annBtn;

    @FXML
    private MenuButton invitStatusBtn;

    @FXML
    private VBox notificationsContainer;

    @FXML
    private Circle profilePhoto;

    public static Circle mainProfilePhoto;
    private ObservableList<Notification> notificationList;
    private User currentUser = DataCenter.getInstance().getCurrentUser();
    private byte[] userPhoto;
    private String curTab;

    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (currentUser.getProfilepicture() == null) {
            Image image = new Image(getClass().getResourceAsStream("/com/liqaa/client/view/images/defaultProfileImage.png"));
            profilePhoto.setFill(new ImagePattern(image));
        } else {
            userPhoto = currentUser.getProfilepicture();
            InputStream inputStream = new ByteArrayInputStream(userPhoto);
            Image image = new Image(inputStream);
            profilePhoto.setFill(new ImagePattern(image));
        }
        profilePhoto.setStroke(null);
        mainProfilePhoto = profilePhoto;

        allAction();
    }

    public void profile_action(MouseEvent mouseEvent) throws IOException {
        System.out.println("profile btn is clicked");
        Parent parent = FXMLLoader.load(getClass().getResource("/com/liqaa/client/view/fxml/userInfo.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
        myStage = stage;
    }

    public void home_action(MouseEvent mouseEvent) {
        System.out.println("home btn is clicked");
        SceneManager.getInstance().showPrimaryScene();
    }

    public void notification_action(MouseEvent mouseEvent) {
        System.out.println("notification btn is clicked");
//        SceneManager.getInstance().showNotificationScene();
    }

    public void contact_action(MouseEvent mouseEvent) {
        SceneManager.getInstance().showContactScene();
        System.out.println("contact btn is clicked");
    }

    public void chatbot_action(MouseEvent mouseEvent) {
        System.out.println("chatbot btn is clicked");
    }

    public void settings_action(MouseEvent mouseEvent) {
        System.out.println("settings btn is clicked");
    }

    public void logout_action(MouseEvent mouseEvent) {
        System.out.println("logout btn is clicked");
    }

    private void updateUI() {
        notificationsContainer.getChildren().clear();
        if (notificationList.isEmpty()) {
            Label noNotificationsLabel = new Label("No notifications to show.");
            noNotificationsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: gray;");
            notificationsContainer.getChildren().add(noNotificationsLabel);
            return;
        }
        for (Notification notification : notificationList) {
            FXMLLoader loader;
            try {
                if(notification.getType().toString().equals("FRIEND_REQUEST")){
                    loader = new FXMLLoader(getClass().getResource("/com/liqaa/client/view/fxml/components/NotificationCard2.fxml"));
                }
                else {
                    loader = new FXMLLoader(getClass().getResource("/com/liqaa/client/view/fxml/components/NotificationCard1.fxml"));
                }
                HBox notificationCard = loader.load();

                Object controller = loader.getController();
                if (controller instanceof NotificationCard1Controller) {
                    ((NotificationCard1Controller) controller).setNotificationData(notification);
                } else if (controller instanceof NotificationCard2Controller) {
                    ((NotificationCard2Controller) controller).setNotificationData(notification);
                }
                notificationsContainer.getChildren().add(notificationCard);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void allAction() {
        curTab = "all";
        notificationList = FXCollections.observableArrayList();
        notificationList.addListener((ListChangeListener<Notification>) change -> {
            if(curTab.equals("all")){
                updateUI();
            }
        });
        Runnable task = () -> {
            if(curTab.equals("all")){
                Platform.runLater(this::loadAllNotifications);
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        updateUI();
    }

    public void acceptedAction() {
        curTab = "accepted";
        notificationList = FXCollections.observableArrayList();
        notificationList.addListener((ListChangeListener<Notification>) change -> {
            if(curTab.equals("accepted")){
                updateUI();
            }
        });
        Runnable task = () -> {
          if(curTab.equals("accepted")){
              loadAllAcceptedInvitations();
          }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        updateUI();
    }

    public void declinedAction(){
        curTab = "declined";
        notificationList = FXCollections.observableArrayList();
        notificationList.addListener((ListChangeListener<Notification>) change -> {
            if(curTab.equals("declined")){
                updateUI();
            }
        });
        Runnable task = () -> {
            if(curTab.equals("declined")){
                loadAllDeclinedInvitations();
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        updateUI();
    }

    public void reqAction(){
        curTab = "request";
        notificationList = FXCollections.observableArrayList();
        // the following line is not executed if the list is empty
        notificationList.addListener((ListChangeListener<Notification>) change -> {
            if(curTab.equals("request")) {
                updateUI();
            }
        });
        Runnable task = () -> {
            if(curTab.equals("request")){
                loadUpcomingRequests();
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        updateUI(); // to call updateUI() even if the list is empty
    }

    public void annAction(){
        curTab = "announcement";
        notificationList = FXCollections.observableArrayList();
        notificationList.addListener((ListChangeListener<Notification>) change -> {
            if(curTab.equals("announcement")) {
                updateUI();
            }
        });
        Runnable task = () -> {
            if(curTab.equals("announcement")){
                loadAnnouncements();
            }
        };
        executor.scheduleAtFixedRate(task, 0, 1, TimeUnit.SECONDS);
        updateUI();
    }

    public void allMouseEntered(){
        allBtn.setStyle("-fx-background-color: #3498DB;");
    }

    public void allMouseExited(){
        allBtn.setStyle("-fx-background-color: #384E6A;");
    }

    public void statusMouseEntered(){
        invitStatusBtn.setStyle("-fx-background-color: #3498DB;");
    }

    public void statusMouseExited(){
        invitStatusBtn.setStyle("-fx-background-color: #384E6A;");
    }

    public void reqMouseEntered(){
        reqBtn.setStyle("-fx-background-color: #3498DB;");
    }

    public void reqMouseExited(){
        reqBtn.setStyle("-fx-background-color: #384E6A;");
    }

    public void annMouseEntered(){
        annBtn.setStyle("-fx-background-color: #3498DB;");
    }

    public void annMouseExited(){
        annBtn.setStyle("-fx-background-color: #384E6A;");
    }


    // Loads:
    public void loadAllNotifications() {
        try {
            List<Notification> newNotifications = NotificationServiceImpl.getInstance().getAllNotifications(1); // replace it with the current user id
            Platform.runLater(() -> {
                notificationList.clear();
                notificationList.addAll(newNotifications);
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadAllAcceptedInvitations() {
        try {
            List<Notification> newNotifications = NotificationServiceImpl.getInstance().getAllAcceptedInvitations(DataCenter.getInstance().getcurrentUserId());
            Platform.runLater(() -> {
                notificationList.clear();
                notificationList.addAll(newNotifications);
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadAllDeclinedInvitations() {
        try {
            List<Notification> newNotifications = NotificationServiceImpl.getInstance().getAllDeclinedInvitations(DataCenter.getInstance().getcurrentUserId()); // replace it with the current user id
            Platform.runLater(() -> {
                notificationList.clear();
                notificationList.addAll(newNotifications);
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadUpcomingRequests() {
        try {
            List<Notification> newNotifications = NotificationServiceImpl.getInstance().getAllFriendRequests(DataCenter.getInstance().getcurrentUserId());
            Platform.runLater(() -> {
                notificationList.clear();
                notificationList.addAll(newNotifications);
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    public void loadAnnouncements() {
        try {
            List<Notification> newNotifications = NotificationServiceImpl.getInstance().getAnnouncements(DataCenter.getInstance().getcurrentUserId());
            Platform.runLater(() -> {
                notificationList.clear();
                notificationList.addAll(newNotifications);
            });
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }
}
