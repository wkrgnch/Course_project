package com.example.course_project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.sql.*;

public class UserController extends VBox {
    private TableView<User> table;
    private TextField loginField, passwordField;
    private ComboBox<String> roleBox;
    private Label statusLabel;

    public UserController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Пользователи");

        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<User, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new IdCellFactory());
        TableColumn<User, String> usernameCol = new TableColumn<>("Логин");
        usernameCol.setCellValueFactory(new UsernameCellFactory());
        TableColumn<User, String> roleCol = new TableColumn<>("Роль");
        roleCol.setCellValueFactory(new RoleCellFactory());


        loginField = new TextField(); loginField.setPromptText("Логин");
        passwordField = new TextField(); passwordField.setPromptText("Пароль");
        roleBox = new ComboBox<>();
        roleBox.getItems().addAll("admin", "teacher");
        roleBox.setValue("teacher");

        Button addBtn = new Button("Добавить");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(new AddUserHandler());

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(new DeleteUserHandler());

        Button changeRoleBtn = new Button("Изменить роль");
        changeRoleBtn.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white;");
        changeRoleBtn.setOnAction(new ChangeRoleHandler());

        statusLabel = new Label();

        table.getColumns().addAll(idCol, usernameCol, roleCol);

        HBox controls = new HBox(5, loginField, passwordField, roleBox, addBtn, deleteBtn, changeRoleBtn);
        controls.setPadding(new Insets(5));
        getChildren().addAll(label, table, controls, statusLabel);
        loadUsers();
    }

    private void loadUsers() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, username, password, role, IFNULL(teacher_id, 0) as teacher_id FROM users");
            while (rs.next()) {
                table.getItems().add(new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getInt("teacher_id")
                ));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки пользователей!");
        }
    }

    private void addUser() {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO users (username, password, role) VALUES (?, MD5(?), ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, loginField.getText());
            stmt.setString(2, passwordField.getText());
            stmt.setString(3, roleBox.getValue());
            stmt.executeUpdate();
            statusLabel.setText("Пользователь добавлен.");
            loadUsers();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка добавления пользователя.");
        }
    }

    private void deleteUser() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите пользователя!");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM users WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selected.getUsername());
            stmt.executeUpdate();
            statusLabel.setText("Пользователь удалён.");
            loadUsers();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка удаления пользователя.");
        }
    }

    private void changeRole() {
        User selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите пользователя!");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "UPDATE users SET role=? WHERE username=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, roleBox.getValue());
            stmt.setString(2, selected.getUsername());
            stmt.executeUpdate();
            statusLabel.setText("Роль изменена.");
            loadUsers();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка изменения роли.");
        }
    }


    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<User, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<User, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }
    private class UsernameCellFactory implements Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
            return new SimpleStringProperty(param.getValue().getUsername());
        }
    }
    private class RoleCellFactory implements Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
            return new SimpleStringProperty(param.getValue().getRole());
        }
    }


    private class AddUserHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { addUser(); }
    }

    private class DeleteUserHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { deleteUser(); }
    }

    private class ChangeRoleHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { changeRole(); }
    }
}

