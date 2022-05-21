package com.example.hsr;

import com.example.hsr.controller.Controller;
import com.example.hsr.model.map.Camera;
import com.example.hsr.view.View;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Group root = new Group();
        Camera camera = new Camera();
        View view = new View(camera);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(new Controller(camera, view));
        root.getChildren().add(view);
        stage.setTitle("Hidden Surface Removal");
        stage.setScene(scene);
        view.draw();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}