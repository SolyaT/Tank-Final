module com.example.javatank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.javatank to javafx.fxml;
    exports com.example.javatank;
    exports com.example.javatank.core;
    opens com.example.javatank.core to javafx.fxml;
}