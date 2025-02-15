module PIDEV {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.desktop;


    opens com.esprit.test to javafx.fxml;
    exports com.esprit.test;
    opens com.esprit.controllers.Voiture to javafx.fxml;

    opens com.esprit.models to javafx.base;

}