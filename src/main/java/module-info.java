module com.lester.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.lester.carrentalsystem to javafx.fxml;
    exports com.lester.carrentalsystem;
    exports com.lester.carrentalsystem.Controller;
    opens com.lester.carrentalsystem.Controller to javafx.fxml;
}