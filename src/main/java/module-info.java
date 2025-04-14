module com.lester.carrentalsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires net.synedra.validatorfx;
    requires java.desktop;
    requires fontawesomefx;
    requires mysql.connector.j;



    opens com.lester.carrentalsystem to javafx.fxml;
    exports com.lester.carrentalsystem;
    exports com.lester.carrentalsystem.Controller;
    opens com.lester.carrentalsystem.Controller to javafx.fxml;

    exports com.lester.carrentalsystem.Controller.Client to javafx.fxml;
    opens com.lester.carrentalsystem.Controller.Client to javafx.fxml, javafx.base;
}