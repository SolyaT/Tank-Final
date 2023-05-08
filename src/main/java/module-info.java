module com.example.javatank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.javatank to javafx.fxml;
    exports com.example.javatank;
}