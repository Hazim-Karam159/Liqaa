package com.liqaa.server.controllers.services.interfaces;

import com.liqaa.shared.models.entities.Category;

import java.util.List;
import java.util.Map;

public interface CategoryServices
{
    List<Category> getCategories(int userId);
    public List<Integer> addCategories(List<Category> categories);
    void addContactsToCategory(int categoryId, List<Integer> contactIds);
    void removeContactsFromCateogry(int categoryId, List<Integer> contactIds);
    List<Integer> getCategoryContacts(int categoryId);                  // May be removed later
    public void renameCategory(int categoryId, String newName);
    void removeCategory(int categoryId, int userId);
    void removeCategory(String categoryName, int userId);

    List<Category> getCategoriesForContact(int userId, int contactId);
    Map<Category, List<Integer>> getCategoriesWithContacts(int userId);
}
