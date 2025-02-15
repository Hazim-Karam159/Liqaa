package com.liqaa.client.controllers.FXMLcontrollers.components;
import com.liqaa.client.controllers.services.implementations.CategoryServices;
import com.liqaa.client.controllers.services.implementations.ConversationServices;
import com.liqaa.client.controllers.services.implementations.DataCenter;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;
import com.liqaa.shared.models.entities.Category;
import javafx.util.Callback;
import com.liqaa.shared.util.AlertNotifier;

import java.rmi.RemoteException;

import static javafx.application.Platform.runLater;

public class CategoryListController
{
    @FXML
    private ListView<Category> categoryListView;

    @FXML
    public void initialize()
    {

        try {
            CategoryServices.getInstance().loadCategories();
        } catch (RemoteException e)
        {
            Alert alert = AlertNotifier.createAlert("Error", "Error loading categories", e.getMessage());
            alert.showAndWait();
            System.err.println("Error loading categories: " + e.getMessage());
        }
        categoryListView.setItems(DataCenter.getInstance().getCategories());

        categoryListView.setOnMouseClicked(this::handleCategoryClick);

        categoryListView.setCellFactory(new Callback<ListView<Category>, ListCell<Category>>()
        {
            @Override
            public ListCell<Category> call(ListView<Category> param)
            {
                return new ListCell<Category>() {
                    @Override
                    protected void updateItem(Category item, boolean empty)
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null)
                            setText(null);
                         else
                            setText(item.getCategoryName());
                    }
                };
            }
        });
        runLater(() -> categoryListView.getSelectionModel().selectFirst());
    }

    private void handleCategoryClick(MouseEvent event)
    {
        Category selectedCategory = categoryListView.getSelectionModel().getSelectedItem();

        if (selectedCategory != null)
        {
            DataCenter.getInstance().setCurrentCategory(selectedCategory);

            int categoryId = selectedCategory.getId();

            //todo: call category services
            switch (categoryId)
            {
                case -2: // "All" category
                    System.out.println("Loading all conversations"+ DataCenter.getInstance().getcurrentUserId());
                    ConversationServices.getInstance().loadAllConversations(DataCenter.getInstance().getcurrentUserId());
                    break;
                case -3: // "Unread" category
                    ConversationServices.getInstance().loadUnreadConversations(DataCenter.getInstance().getcurrentUserId());
                    break;
                case -4: // "Groups" category
                    ConversationServices.getInstance().loadGroupConversations(DataCenter.getInstance().getcurrentUserId());
                    break;

                default:
                    ConversationServices.getInstance().loadCategoryConversations(DataCenter.getInstance().getCurrentUser().getId(), categoryId);
                    break;
            }
        }
    }

}