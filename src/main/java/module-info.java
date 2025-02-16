module com.example.vol {
    requires javafx.controls;   // For basic JavaFX components
    requires javafx.fxml;      // For FXML functionality
   // requires javafx.web;       // For WebView functionality
    requires java.sql;         // For database connection
    requires javafx.media;

    // Other dependencies
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    // requires eu.hansolo.tilesfx; // If you want to use tilesfx
    requires com.almasb.fxgl.all;
    requires java.desktop; // If you want to use FXGL game library

    opens com.example.vol to javafx.fxml;  // Open the main package to javafx.fxml
    opens controllers to javafx.fxml;     // Open the controllers package to javafx.fxml
    exports com.example.vol;              // Export the main package
    exports controllers;                  // Export the controllers package
}
