module com.example.tank_finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.tank_finalproject to javafx.fxml;
    exports com.example.tank_finalproject;
}