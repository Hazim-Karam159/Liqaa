package com.liqaa.client.controllers.services.implementations;

import com.liqaa.client.controllers.services.interfaces.UserInfoService;
import com.liqaa.client.network.ServerConnection;
import com.liqaa.shared.models.entities.User;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Objects;

public class UserInfoServiceImpl implements UserInfoService {
    private static UserInfoServiceImpl instance;

    public static synchronized UserInfoServiceImpl getInstance() {
        if (instance == null) {
            instance = new UserInfoServiceImpl();
        }
        return instance;
    }

    @Override
    public boolean updateUser(User user) throws RemoteException {
        System.out.println("in client update user impl: " + user.getBio());
        return ServerConnection.getServer().updateUserInfo(user);
    }
}
