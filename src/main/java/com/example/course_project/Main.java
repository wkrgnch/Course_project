package com.example.course_project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Создаём View и Controller для экрана логина
        LoginView view = new LoginView();
        new LoginController(view, primaryStage);

        // Показываем сцену
        Scene scene = new Scene(view.getRoot(), 400, 250);
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
