package com.liqaa.client.controllers.services.interfaces;

import com.liqaa.shared.models.entities.User;

import java.rmi.RemoteException;

public interface UserInfoService {
    boolean updateUser(User user) throws RemoteException;
}
