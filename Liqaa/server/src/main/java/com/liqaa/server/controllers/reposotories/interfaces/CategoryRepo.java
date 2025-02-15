package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.Category;

import java.util.List;
import java.util.Locale;

public interface CategoryRepo
{
    int createCategory(Category category);
    Category getCategory(int id);
    boolean updateCategoryName(int id, String name);
    boolean deleteCategory(int id);
    List<Category> getUserCategories(int id);
    List<Category> getAllCategories(int [] ids);
    public List<Category> getCategoriesForContact(int userId, int contactId);
    void removeCategory(String categoryName, int userId);
}

