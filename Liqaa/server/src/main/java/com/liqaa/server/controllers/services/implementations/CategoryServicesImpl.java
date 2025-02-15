package com.liqaa.server.controllers.services.implementations;

import com.liqaa.server.controllers.reposotories.implementations.CategoryRepoImpl;
import com.liqaa.server.controllers.reposotories.implementations.UserContactCategoryRepoImpl;
import com.liqaa.server.controllers.services.interfaces.CategoryServices;
import com.liqaa.shared.models.entities.Category;
import com.liqaa.shared.models.entities.UserContactCategory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryServicesImpl implements CategoryServices
{
    private static CategoryServicesImpl instance;
    public CategoryServicesImpl() {}

    public static synchronized CategoryServicesImpl getInstance()
    {
        if (instance == null)
            instance = new CategoryServicesImpl();

        return instance;
    }

    @Override
    public List<Category> getCategories(int userId)
    {
        if(userId > 0)
            return CategoryRepoImpl.getInstance().getUserCategories(userId);
        return List.of();
    }

    @Override
    public List<Integer> addCategories(List<Category> categories)
    {
        List<Integer> ids = new ArrayList<>();
        if (categories != null && !categories.isEmpty())
        {
            for (Category category : categories)
            {
                int userId = category.getUserId();
                if (userId > 0)
                {
                    int id = CategoryRepoImpl.getInstance().createCategory(category);
                    if (id > 0)
                        ids.add(id);
                }
                else
                    System.err.println("Invalid user ID for category: " + category);
            }
        }
        return ids;
    }

    @Override
    public void addContactsToCategory(int categoryId, List<Integer> contactIds)
    {
        if (categoryId <= 0 || contactIds == null || contactIds.isEmpty())
            return;

        for (int contactId : contactIds)
        {
            if (contactId <= 0)
            {
                System.err.println("Invalid contact ID: " + contactId);
                continue;
            }
            UserContactCategory userContactCategory = new UserContactCategory(contactId, categoryId);
            try
            {
                UserContactCategoryRepoImpl.getInstance().createUserContactCategory(userContactCategory);
            }
            catch (SQLException e)
            {
                System.err.println("Error adding contact to category: " + e.getMessage());
            }
        }
    }

    @Override
    public void removeContactsFromCateogry(int categoryId, List<Integer> contactIds)
    {
        if (categoryId <= 0 || contactIds == null || contactIds.isEmpty())
            return;

        for (int contactId : contactIds)
        {
            if (contactId <= 0)
            {
                System.err.println("Invalid contact ID: " + contactId);
                continue;
            }
            UserContactCategory userContactCategory = new UserContactCategory(contactId, categoryId);
            try
            {
                UserContactCategoryRepoImpl.getInstance().deleteUserContactCategory(userContactCategory);
            }
            catch (SQLException e)
            {
                System.err.println("Error removing contact from category: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Integer> getCategoryContacts(int categoryId)
    {
        if (categoryId > 0)
        {
            try
            {
                return UserContactCategoryRepoImpl.getInstance().getCategoryUsers(categoryId);
            }
            catch (SQLException e)
            {
                System.err.println("Error getting category contacts: " + e.getMessage());
            }
        }
        return List.of();
    }

    @Override
    public void renameCategory(int categoryId, String newName)
    {
        if (categoryId <= 0 || newName == null || newName.isEmpty())
        {
            System.err.println("Invalid category ID, user ID, or name");
            return;
        }
        if (!CategoryRepoImpl.getInstance().updateCategoryName(categoryId, newName))
            System.err.println("Error renaming category: " + categoryId);
    }

    @Override
    public void removeCategory(int categoryId, int userId)
    {
        if (categoryId <= 0 || userId <= 0)
        {
            System.err.println("Invalid category ID or user ID");
            return;
        }
        if (!CategoryRepoImpl.getInstance().deleteCategory(categoryId))
            System.err.println("Error deleting category: " + categoryId);
    }

    @Override
    public void removeCategory(String categoryName, int userId)
    {
        if (categoryName == null || categoryName.isEmpty() || userId <= 0)
        {
            System.err.println("Invalid category name or user ID");
            return;
        }
        CategoryRepoImpl.getInstance().removeCategory(categoryName, userId);
    }

    @Override
    public List<Category> getCategoriesForContact(int userId, int contactId)
    {
        if (userId <= 0 || contactId <= 0)
        {
            System.err.println("Invalid user ID or contact ID");
            return List.of();
        }
        return CategoryRepoImpl.getInstance().getCategoriesForContact(userId, contactId);
    }

    @Override
    public Map<Category, List<Integer>> getCategoriesWithContacts(int userId)
    {
        Map<Category, List<Integer>> result = new HashMap<>();
        if (userId > 0)
        {
            try
            {
                List<Category> categories = CategoryRepoImpl.getInstance().getUserCategories(userId);
                for (Category category : categories)
                {
                    List<Integer> contactIds = UserContactCategoryRepoImpl.getInstance().getCategoryUsers(category.getId());
                    result.put(category, contactIds);
                }
            }
            catch (SQLException e)
            {
                System.err.println("Error getting categories with contacts: " + e.getMessage());
            }
        }
        return result;
    }
}