package com.example.course_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginView extends Application {
    public void start(Stage primaryStage) {
        Label usernameLabel = new Label("Имя пользователя:");
        TextField usernameField = new TextField();

        Label passwordLabel = new Label("Пароль:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Войти");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        loginButton.setOnAction(new LoginHandler(usernameField, passwordField, primaryStage));

        Button registerButton = new Button("Регистрация");
        registerButton.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        registerButton.setOnAction(new RegisterHandler());

        Button changePassBtn = new Button("Сменить пароль");
        changePassBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        changePassBtn.setOnAction(new ChangePasswordHandler(primaryStage));


        Label messageLabel = new Label();

        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(20));
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(loginButton, 0, 2);
        grid.add(registerButton, 1, 2);
        grid.add(messageLabel, 0, 3, 2, 1);
        grid.add(changePassBtn, 0, 4, 2, 1);

        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Вход в систему");
        primaryStage.show();
    }

    // для авторизации
    private class LoginHandler implements EventHandler<ActionEvent> {
        private TextField usernameField;
        private PasswordField passwordField;
        private Stage primaryStage;

        public LoginHandler(TextField usernameField, PasswordField passwordField, Stage primaryStage) {
            this.usernameField = usernameField;
            this.passwordField = passwordField;
            this.primaryStage = primaryStage;
        }

        public void handle(ActionEvent event) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            User user = authenticate(username, password);

            if (user != null) {
                if ("admin".equals(user.getRole())) {
                    new AdminView(user).start(new Stage());
                } else {
                    new TeacherView(user).start(new Stage());
                }
                primaryStage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Неверное имя пользователя или пароль!", ButtonType.OK);
                alert.show();
            }
        }

        private User authenticate(String username, String password) {
            try {
                Connection conn = Database.getConnection();
                String sql = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, username);
                stmt.setString(2, md5(password));
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"),
                            rs.getInt("teacher_id"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
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

    //для перехода к регистрации
    private class RegisterHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            new RegistrationView().start(new Stage());
        }
    }

    private class ChangePasswordHandler implements EventHandler<ActionEvent> {
        private Stage ownerStage;

        public ChangePasswordHandler(Stage ownerStage) {
            this.ownerStage = ownerStage;
        }

        public void handle(ActionEvent event) {
            ChangePassword.showDialog(ownerStage);
        }
    }
}
