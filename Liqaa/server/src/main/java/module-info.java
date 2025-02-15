module com.liqaa.server {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.liqaa.shared;
    requires mysql.connector.java;
    requires java.sql;
    requires com.zaxxer.hikari;
    requires org.slf4j;
    requires java.rmi;

//    requires org.json;
//    requires jakarta.xml.bind;
//    requires java.desktop;
//    requires jdk.httpserver;
//    requires okhttp3;

    exports com.liqaa.server;
    exports com.liqaa.server.controllers.FXMLcontrollers to javafx.fxml;

    opens com.liqaa.server to javafx.fxml;
    opens com.liqaa.server.controllers.FXMLcontrollers to javafx.fxml;
    exports com.liqaa.server.controllers.reposotories.implementations;
    exports com.liqaa.server.network;
}