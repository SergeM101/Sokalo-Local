module com.sokalo.sokalolocal {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.sokalo.sokalolocal to javafx.fxml;
    exports com.sokalo.sokalolocal;
}