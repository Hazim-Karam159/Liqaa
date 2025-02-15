package com.liqaa.client.controllers.services.implementations;

import com.liqaa.shared.models.entities.Category;
import com.liqaa.shared.network.Server;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.client.controllers.services.implementations.DataCenter;

import java.rmi.RemoteException;
import java.util.List;

public class CategoryServices
{

    private static CategoryServices instance;

    private CategoryServices() throws RemoteException
    {
        if (ServerConnection.getServer() != null) {
            loadCategories();
        }
    }


    public static CategoryServices getInstance() throws RemoteException
    {
        if (instance == null) {
            instance = new CategoryServices();
        }
        return instance;
    }

    public void loadCategories()
    {
        try {
            int userId = DataCenter.getInstance().getCurrentUser().getId();
            List<Category> categoryList = ServerConnection.getServer().getCategories(userId);
            ObservableList<Category> observableCategories = FXCollections.observableArrayList(categoryList);

            Platform.runLater(() -> {
                DataCenter.getInstance().initializeDefaultCategories();
                DataCenter.getInstance().getCategories().addAll(observableCategories);
            });
        } catch (RemoteException e) {
            System.err.println("Error loading categories: " + e.getMessage());
        }
    }

    public ObservableList<Category> getCategories() {
        return DataCenter.getInstance().getCategories();
    }

    public void addCategories(List<Category> newCategories) {
        try {
            List<Integer> categoryIds = ServerConnection.getServer().addCategories(newCategories);
            for (int i = 0; i < newCategories.size(); i++) {
                newCategories.get(i).setId(categoryIds.get(i));
            }

            Platform.runLater(() -> {
                DataCenter.getInstance().getCategories().addAll(newCategories);
            });
        } catch (RemoteException e) {
            System.err.println("Error adding categories: " + e.getMessage());
        }
    }

    public void removeCategory(int categoryId) {
        try {
            int userId = DataCenter.getInstance().getCurrentUser().getId();
            ServerConnection.getServer().removeCategory(categoryId, userId);

            Platform.runLater(() -> {
                ObservableList<Category> categories = DataCenter.getInstance().getCategories();
                if (categories != null) {
                    categories.removeIf(category -> category.getId() == categoryId);
                }
            });
        } catch (RemoteException e) {
            System.err.println("Error removing category: " + e.getMessage());
        }
    }

    public void renameCategory(int categoryId, String newName) {
        try {
            ServerConnection.getServer().renameCategory(categoryId, newName);

            Platform.runLater(() -> {
                ObservableList<Category> categories = DataCenter.getInstance().getCategories();
                if (categories != null) {
                    categories.stream()
                            .filter(category -> category.getId() == categoryId)
                            .findFirst()
                            .ifPresent(category -> category.setCategoryName(newName));

                }
            });

        } catch (RemoteException e) {
            System.err.println("Error renaming category: " + e.getMessage());
        }
    }

    public void removeCategory(String categoryName, int userId) {
        try {
            ServerConnection.getServer().removeCategory(categoryName, userId);
        } catch (RemoteException e) {
            System.err.println("Error removing category: " + e.getMessage());
        }

    }
}