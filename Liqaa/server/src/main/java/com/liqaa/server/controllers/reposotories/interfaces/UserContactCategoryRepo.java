package com.liqaa.server.controllers.reposotories.interfaces;

import com.liqaa.shared.models.entities.Category;
import com.liqaa.shared.models.entities.User;
import com.liqaa.shared.models.entities.UserContactCategory;

import java.sql.SQLException;
import java.util.List;

public interface UserContactCategoryRepo {
    public boolean createUserContactCategory(UserContactCategory userContactCategory) throws SQLException;
    public List<UserContactCategory> getAllUserContactCategories() throws SQLException;
    public boolean updateUserContactCategory(UserContactCategory userContactCategory) throws SQLException;
    public boolean deleteUserContactCategory(UserContactCategory userContactCategory) throws SQLException;
    public List<UserContactCategory> getSpecificUserWithCategories(int userId) throws SQLException;
    public List<Integer> getCategoryUsers(int categoryId) throws SQLException;
}