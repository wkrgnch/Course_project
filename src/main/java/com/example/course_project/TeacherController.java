package com.example.course_project;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.sql.*;

public class TeacherController extends VBox {
    private TableView<Teacher> table;
    private TextField surnameField, nameField, patronymicField, experienceField, phoneField;
    private Label statusLabel;

    public TeacherController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Список преподавателей");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Teacher, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new IdCellFactory());
        TableColumn<Teacher, String> surnameCol = new TableColumn<>("Фамилия");
        surnameCol.setCellValueFactory(new SurnameCellFactory());
        TableColumn<Teacher, String> nameCol = new TableColumn<>("Имя");
        nameCol.setCellValueFactory(new NameCellFactory());
        TableColumn<Teacher, String> patrCol = new TableColumn<>("Отчество");
        patrCol.setCellValueFactory(new PatronymicCellFactory());
        TableColumn<Teacher, Integer> expCol = new TableColumn<>("Стаж");
        expCol.setCellValueFactory(new ExperienceCellFactory());
        TableColumn<Teacher, String> phoneCol = new TableColumn<>("Телефон");
        phoneCol.setCellValueFactory(new PhoneCellFactory());

        table.getColumns().addAll(idCol, surnameCol, nameCol, patrCol, expCol, phoneCol);

        surnameField = new TextField(); surnameField.setPromptText("Фамилия");
        nameField = new TextField(); nameField.setPromptText("Имя");
        patronymicField = new TextField(); patronymicField.setPromptText("Отчество");
        experienceField = new TextField(); experienceField.setPromptText("Стаж");
        phoneField = new TextField(); phoneField.setPromptText("Телефон");

        Button addButton = new Button("Добавить");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(new AddTeacherHandler());

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(new DeleteTeacherHandler());

        statusLabel = new Label();

        HBox controls = new HBox(5, surnameField, nameField, patronymicField, experienceField, phoneField, addButton, deleteButton);
        controls.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(label, table, controls, statusLabel);
        loadTeachers();
    }

    private void loadTeachers() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM teachers");
            while (rs.next()) {
                table.getItems().add(new Teacher(
                        rs.getInt("id"),
                        rs.getString("surname"),
                        rs.getString("name"),
                        rs.getString("patronymic"),
                        rs.getInt("experience"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки преподавателей!");
        }
    }

    private void addTeacher() {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO teachers (surname, name, patronymic, experience, phone_number) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, surnameField.getText());
            stmt.setString(2, nameField.getText());
            stmt.setString(3, patronymicField.getText());
            stmt.setInt(4, Integer.parseInt(experienceField.getText()));
            stmt.setString(5, phoneField.getText());
            stmt.executeUpdate();
            statusLabel.setText("Преподаватель добавлен.");
            loadTeachers();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка добавления преподавателя.");
        }
    }

    private void deleteTeacher() {
        Teacher selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите преподавателя!");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM teachers WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            statusLabel.setText("Преподаватель удалён.");
            loadTeachers();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка удаления преподавателя.");
        }
    }

    // ===== Внутренние классы для колонок =====
    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Teacher, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }
    private class SurnameCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, String>, javafx.beans.value.ObservableValue<String>> {
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> param) {
            return new javafx.beans.property.SimpleStringProperty(param.getValue().getSurname());
        }
    }
    private class NameCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, String>, javafx.beans.value.ObservableValue<String>> {
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> param) {
            return new javafx.beans.property.SimpleStringProperty(param.getValue().getName());
        }
    }
    private class PatronymicCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, String>, javafx.beans.value.ObservableValue<String>> {
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> param) {
            return new javafx.beans.property.SimpleStringProperty(param.getValue().getPatronymic());
        }
    }
    private class ExperienceCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Teacher, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getExperience()).asObject();
        }
    }
    private class PhoneCellFactory implements Callback<TableColumn.CellDataFeatures<Teacher, String>, javafx.beans.value.ObservableValue<String>> {
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Teacher, String> param) {
            return new javafx.beans.property.SimpleStringProperty(param.getValue().getPhoneNumber());
        }
    }

    // ===== Внутренние классы-обработчики =====
    private class AddTeacherHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent event) {
            addTeacher();
        }
    }

    private class DeleteTeacherHandler implements javafx.event.EventHandler<javafx.event.ActionEvent> {
        public void handle(javafx.event.ActionEvent event) {
            deleteTeacher();
        }
    }
}
