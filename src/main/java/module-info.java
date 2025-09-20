module com.sokalo {
    requires javafx.controls; // for controlsFX
    requires javafx.fxml;   // for formFX

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;  // For sqlite

    opens com.sokalo.controller to javafx.fxml;

    exports com.sokalo;     // grants access from the main class
    exports com.sokalo.controller;  // exports controllers
}