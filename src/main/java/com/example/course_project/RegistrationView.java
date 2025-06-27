package com.example.course_project;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;


public class RegistrationView extends Application {
    public void start(Stage stage) {
        Label usernameLabel = new Label("Имя пользователя:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();

        Label roleLabel = new Label("Роль:");
        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "teacher");
        roleBox.setValue("teacher");

        Button registerButton = new Button("Зарегистрироваться");
        registerButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        registerButton.setOnAction(new RegisterHandler(usernameField, passwordField, roleBox, stage));

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleBox, 1, 2);
        grid.add(registerButton, 1, 3);

        Scene scene = new Scene(grid, 350, 200);
        stage.setScene(scene);
        stage.setTitle("Регистрация нового пользователя");
        stage.show();
    }

    private class RegisterHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        private TextField usernameField;
        private PasswordField passwordField;
        private ComboBox<String> roleBox;
        private Stage stage;

        public RegisterHandler(TextField usernameField, PasswordField passwordField, ComboBox<String> roleBox, Stage stage) {
            this.usernameField = usernameField;
            this.passwordField = passwordField;
            this.roleBox = roleBox;
            this.stage = stage;
        }

        public void handle(javafx.event.ActionEvent event) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleBox.getValue();

            try {
                Connection conn = Database.getConnection();
                String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, md5(password));
                stmt.setString(3, role);
                stmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Регистрация успешна!", ButtonType.OK);
                alert.showAndWait();
                stage.close();
            } catch (SQLException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при регистрации!", ButtonType.OK);
                alert.show();
            }
        }

        private String md5(String input) {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] digest = md.digest(input.getBytes());
                BigInteger no = new BigInteger(1, digest);
                String hashText = no.toString(16);
                while (hashText.length() < 32) {
                    hashText = "0" + hashText;
                }
                return hashText;
            } catch (Exception e) {
                return null;
            }
        }
    }
}

