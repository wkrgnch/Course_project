package com.example.course_project;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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

    private void showTeacherDialog(String username, String password) {
        Stage dialog = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        TextField surnameField = new TextField();
        surnameField.setPromptText("Фамилия");
        TextField nameField = new TextField();
        nameField.setPromptText("Имя");
        TextField patronymicField = new TextField();
        patronymicField.setPromptText("Отчество");
        TextField experienceField = new TextField();
        experienceField.setPromptText("Стаж");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Телефон");

        Button okBtn = new Button("OK");
        okBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        okBtn.setOnAction(new RegisterTeacherHandler(surnameField, nameField, patronymicField, experienceField, phoneField,
                username, password, dialog));

        root.getChildren().addAll(new Label("Данные преподавателя:"), surnameField, nameField, patronymicField, experienceField, phoneField, okBtn);
        dialog.setScene(new Scene(root, 300, 350));
        dialog.setTitle("Регистрация преподавателя");
        dialog.show();
    }

    private class RegisterTeacherHandler implements EventHandler<ActionEvent> {
        private TextField surnameField, nameField, patronymicField, experienceField, phoneField;
        private String username, password;
        private Stage dialog;

        public RegisterTeacherHandler(TextField surname, TextField name, TextField patr, TextField exp, TextField phone,
                                String username, String password, Stage dialog) {
            this.surnameField = surname;
            this.nameField = name;
            this.patronymicField = patr;
            this.experienceField = exp;
            this.phoneField = phone;
            this.username = username;
            this.password = password;
            this.dialog = dialog;
        }

        public void handle(ActionEvent event) {
            try {
                Connection conn = Database.getConnection();
                // сначала вставим в teachers
                String sqlTeacher = "INSERT INTO teachers (surname, name, patronymic, experience, phone_number) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmtTeacher = conn.prepareStatement(sqlTeacher, Statement.RETURN_GENERATED_KEYS);
                stmtTeacher.setString(1, surnameField.getText());
                stmtTeacher.setString(2, nameField.getText());
                stmtTeacher.setString(3, patronymicField.getText());
                stmtTeacher.setInt(4, Integer.parseInt(experienceField.getText()));
                stmtTeacher.setString(5, phoneField.getText());
                stmtTeacher.executeUpdate();
                ResultSet rs = stmtTeacher.getGeneratedKeys();
                int teacherId = -1;
                if (rs.next()) {
                    teacherId = rs.getInt(1);
                }
                // затем в users с этим teacher_id
                if (teacherId != -1) {
                    String sqlUser = "INSERT INTO users (username, password, role, teacher_id) VALUES (?, MD5(?), ?, ?)";
                    PreparedStatement stmtUser = conn.prepareStatement(sqlUser);
                    stmtUser.setString(1, username);
                    stmtUser.setString(2, password);
                    stmtUser.setString(3, "teacher");
                    stmtUser.setInt(4, teacherId);
                    stmtUser.executeUpdate();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Регистрация успешна!", ButtonType.OK);
                    alert.showAndWait();
                }
                dialog.close();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка при регистрации!", ButtonType.OK);
                alert.show();
            }
        }
    }

    private class RegisterHandler implements EventHandler<ActionEvent> {
        private TextField usernameField;
        private PasswordField passwordField;
        private ComboBox<String> roleBox;
        private Stage stage;

        public RegisterHandler(TextField usernameField, PasswordField passwordField,
                               ComboBox<String> roleBox, Stage stage) {
            this.usernameField = usernameField;
            this.passwordField = passwordField;
            this.roleBox = roleBox;
            this.stage = stage;
        }

        public void handle(ActionEvent event) {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();
            String role = roleBox.getValue();

            if ("teacher".equals(role)) {
                showTeacherDialog(username, password);
            } else {
                // регистрация админа (без teacher_id)
                try {
                    Connection conn = Database.getConnection();
                    String sql = "INSERT INTO users (username, password, role) VALUES (?, MD5(?), ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
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
        }
    }
}

