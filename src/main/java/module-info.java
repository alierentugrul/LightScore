module com.example.javaproject {
    requires javafx.fxml;
    requires de.jensd.fx.glyphs.fontawesome;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires javafx.controls;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires com.jfoenix;

    opens com.example.javaproject.Controllers to com.google.gson, javafx.fxml;
    opens com.example.javaproject.Controllers.Admin to com.google.gson, javafx.fxml;
    opens com.example.javaproject.Views to javafx.fxml;
    opens com.example.javaproject to javafx.fxml;

    exports com.example.javaproject;
    exports com.example.javaproject.Controllers;
    exports com.example.javaproject.Controllers.Admin;
    exports com.example.javaproject.Controllers.Client;
    exports com.example.javaproject.Models;
    exports com.example.javaproject.Views;
    opens com.example.javaproject.Controllers.Client to com.google.gson, javafx.fxml;
}