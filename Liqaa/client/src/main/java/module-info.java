module com.liqaa.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.liqaa.server;
    requires com.liqaa.shared;
    requires java.sql;
    requires java.rmi;
    requires java.management;

    exports com.liqaa.client.controllers.FXMLcontrollers.components;
    exports com.liqaa.client.controllers.FXMLcontrollers;
    opens com.liqaa.client to javafx.fxml;
    exports com.liqaa.client;
    opens com.liqaa.client.controllers.FXMLcontrollers to javafx.fxml;
    opens com.liqaa.client.controllers.FXMLcontrollers.components to javafx.fxml;
}