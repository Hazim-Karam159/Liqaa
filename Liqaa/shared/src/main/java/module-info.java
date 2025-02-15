module com.liqaa.shared {
    exports com.liqaa.shared.models.entities;
    exports com.liqaa.shared.models.enums;
    exports com.liqaa.shared.exceptions;
    exports com.liqaa.shared.models;
    exports com.liqaa.shared.util;
    exports com.liqaa.shared.network;

    requires java.sql;
    requires javafx.base;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.rmi;
    requires mysql.connector.java;
    requires jakarta.xml.bind;
}