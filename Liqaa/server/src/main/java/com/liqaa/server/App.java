package com.liqaa.server;

import java.rmi.RemoteException;
import java.sql.SQLException;

import static com.liqaa.server.Main.startServer;
import static javafx.application.Application.launch;

public class App
{
    public static void main(String[] args)  {
        try {
            Main.main(null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
