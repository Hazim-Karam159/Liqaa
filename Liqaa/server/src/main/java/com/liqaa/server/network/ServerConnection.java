package com.liqaa.server.network;


import com.liqaa.server.network.ServerImpl;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerConnection
{

    private static final String SERVER_URI = "rmi://localhost:1900/liqaa";
    private static final int PORT = 1900;

    private static ServerConnection instance;

    private ServerConnection() {
        try {
            LocateRegistry.createRegistry(PORT);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }

    public void start() {
        try {
            Naming.rebind(SERVER_URI, ServerImpl.getServer());
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            Naming.unbind(SERVER_URI);
        } catch (MalformedURLException | RemoteException | NotBoundException e) {
            System.out.println("Server Already Disconnected");
        }
    }
}