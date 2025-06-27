package com.example.course_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.sql.*;

public class ChangePassword {
    public static void showDialog(Stage parent) {
        Stage dialog = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField loginField = new TextField();
        loginField.setPromptText("Логин");

        PasswordField oldPassField = new PasswordField();
        oldPassField.setPromptText("Старый пароль");

        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("Новый пароль");

        Button changeBtn = new Button("Сменить");
        changeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Label statusLabel = new Label();

        changeBtn.setOnAction(new ChangePasswordHandler(loginField, oldPassField, newPassField, statusLabel));

        root.getChildren().addAll(
                new Label("Введите логин, старый и новый пароль:"),
                loginField, oldPassField, newPassField, changeBtn, statusLabel
        );
        dialog.setScene(new Scene(root, 300, 250));
        dialog.setTitle("Смена пароля");
        dialog.show();
    }

    // для обработки смены пароля
    private static class ChangePasswordHandler implements EventHandler<ActionEvent> {
        private TextField loginField;
        private PasswordField oldPassField;
        private PasswordField newPassField;
        private Label statusLabel;

        public ChangePasswordHandler(TextField loginField, PasswordField oldPassField,
                                     PasswordField newPassField, Label statusLabel) {
            this.loginField = loginField;
            this.oldPassField = oldPassField;
            this.newPassField = newPassField;
            this.statusLabel = statusLabel;
        }

        public void handle(ActionEvent event) {
            String login = loginField.getText().trim();
            String oldPass = oldPassField.getText().trim();
            String newPass = newPassField.getText().trim();
            try {
                Connection conn = Database.getConnection();
                String checkSql = "SELECT password FROM users WHERE username=?";
                PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                checkStmt.setString(1, login);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    String hash = rs.getString(1);
                    if (!hash.equals(md5(oldPass))) {
                        statusLabel.setText("Старый пароль неверен!");
                        return;
                    }
                    String updateSql = "UPDATE users SET password=? WHERE username=?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                    updateStmt.setString(1, md5(newPass));
                    updateStmt.setString(2, login);
                    updateStmt.executeUpdate();
                    statusLabel.setText("Пароль изменён!");
                } else {
                    statusLabel.setText("Пользователь не найден!");
                }
            } catch (Exception e) {
                statusLabel.setText("Ошибка смены пароля.");
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

