package com.liqaa.client.controllers.services.interfaces;

import com.liqaa.shared.models.entities.User;

public interface CurrentUser
{
    public User getCurrentUser();
    public void setCurrentUser(User user);
}

