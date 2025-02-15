package com.liqaa.client.controllers.services.implementations;

import com.liqaa.shared.models.Contact;
import com.liqaa.client.util.SceneManager;
import com.liqaa.shared.models.ChatInfo;
import com.liqaa.shared.models.entities.*;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class DataCenter
{
    private static DataCenter instance = null;

    private ObservableList<Category> categories = FXCollections.observableArrayList();
    private ObservableList<ChatInfo> chatList = FXCollections.observableArrayList();
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<Contact> originalContactsList = FXCollections.observableArrayList();

    private ObservableList<Notification> notificationList;
    private String curTab;

    private Category currentCategory ;
    private User currentUser; //= new User(1,"Ibrahim","ibrahim@gmail.com","password",null);// for testing
    private ChatInfo currentChat;
    private int currentConversationId;
    private SceneManager.SceneType currentScene;
    private boolean rememberMeState = true;

    private DataCenter() {}

    public static DataCenter getInstance()
    {
        if (instance == null)
            instance = new DataCenter();

        return instance;
    }

    public ObservableList getCategories()
    {
        return categories;
    }

    public ObservableList<ChatInfo> getChats()
    {
        return chatList;
    }

    public ObservableList<Message> getMessages()
    {
        return messages;
    }

    public User getCurrentUser()
    {
        return currentUser;
    }

    public int getcurrentUserId()
    {
        return currentUser.getId();
    }

    public ChatInfo getCurrentChat()
    {
        return currentChat;
    }

    public void setCurrentChat(ChatInfo chat)
    {
        currentChat = chat;
    }

    public int getCurrentConversationId()
    {
        return currentChat.getConversationId();
    }

    public Category getCurrentCategory()
    {
        return currentCategory;
    }


    public void setCurrentCategory(Category category)
    {
        currentCategory = category;
    }

    public void setCurrentUser(User user)
    {
        currentUser = user;
    }

    public void setCurrentConversationId(int conversationId)
    {
        currentConversationId = conversationId;
    }

    public SceneManager.SceneType getCurrentScene()
    {
        return SceneManager.getInstance().getCurrentScene();
    }

    public void setCurrentScene(SceneManager.SceneType scene)
    {
        SceneManager.getInstance().setCurrentScene(scene);
    }

    public void flushData()
    {
        chatList.clear();
        messages.clear();
        categories.clear();
//        initializeDefaultCategories();
    }
    public void initializeDefaultCategories()
    {
        categories.setAll(
                new Category(-2, 0, "All"),
                new Category(-3, 0, "Unread"),
                new Category(-4, 0, "Groups")
        );
        setCurrentCategory(categories.get(0));
    }

    public ObservableList<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(ObservableList<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    public String getCurTab() {
        return curTab;
    }

    public void setCurTab(String curTab) {
        this.curTab = curTab;
    }

    public ObservableList<Contact> getOriginalContactsList() {
        return originalContactsList;
    }

    public void setOriginalContactsList(ObservableList<Contact> originalContactsList) {
        this.originalContactsList = originalContactsList;
    }

    // Add list change listeners for automatic UI updates
    public void initializeListeners() {
        chatList.addListener((ListChangeListener.Change<? extends ChatInfo> change) -> {
            while (change.next()) {
                if (change.wasAdded() || change.wasUpdated() || change.wasRemoved()) {
                    sortAndFilterChatList();
                }
            }
        });
    }

    private void sortAndFilterChatList() {
        FXCollections.sort(chatList, (c1, c2) ->
                c2.getLastMsgTime().compareTo(c1.getLastMsgTime())
        );
    }

    // Add helper methods for list management
    public void updateChatInList(ChatInfo updatedChat) {
        int index = chatList.indexOf(updatedChat);
        if (index >= 0) {
            chatList.set(index, updatedChat);
        }
    }

    public boolean isRememberMeEnabled() {
        return rememberMeState;
    }

    void setRememberMeState(boolean getRememberMeCheckBox) {
        this.rememberMeState = getRememberMeCheckBox;
    }

}