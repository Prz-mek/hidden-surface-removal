module com.example.virtualcamera {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;


    opens com.example.hsr to javafx.fxml;
    exports com.example.hsr;
    exports com.example.hsr.controller;
    opens com.example.hsr.controller to javafx.fxml;
}