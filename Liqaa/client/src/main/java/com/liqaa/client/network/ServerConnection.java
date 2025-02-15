package com.liqaa.client.network;

import com.liqaa.shared.network.Server;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class ServerConnection
{

    private static Server server;
    private final static String serverURI = "rmi://localhost:1900/liqaa";

    private ServerConnection(){}

    public static Server getServer(){
        try {
            if(server == null) {
                server = (Server) Naming.lookup(serverURI);
            }
//            server.ping();
            return server;
        } catch (RemoteException | NotBoundException | MalformedURLException e)
        {
            Platform.runLater(()->{
                Alert alert = new Alert(Alert.AlertType.ERROR, "Server Disconnected");
                alert.setHeaderText(null);
                alert.showAndWait();
//                CurrentChat.setCurrentChat(null);
//                DataCenter.getInstance().flushLists();
//                SwitchScenes.getInstance().switchToSignInSecond(PrimaryStage.getInstance().getPrimaryStage());
            });
            return null;
        }
    }

    public static void disconnect(){
        server = null;
    }
}
