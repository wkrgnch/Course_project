package com.example.course_project;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.sql.*;

public class CourseController extends VBox {
    private TableView<Course> table;
    private TextField courseNameField;
    private Label statusLabel;

    public CourseController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Список курсов");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Course, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new IdCellFactory());

        TableColumn<Course, String> nameCol = new TableColumn<>("Название курса");
        nameCol.setCellValueFactory(new NameCellFactory());

        table.getColumns().addAll(idCol, nameCol);

        courseNameField = new TextField();
        courseNameField.setPromptText("Название курса");

        Button addButton = new Button("Добавить");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(new AddCourseHandler());

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(new DeleteCourseHandler());

        statusLabel = new Label();

        HBox controls = new HBox(10, courseNameField, addButton, deleteButton);
        controls.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(label, table, controls, statusLabel);
        loadCourses();
    }

    private void loadCourses() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM courses");
            while (rs.next()) {
                table.getItems().add(new Course(rs.getInt("id"), rs.getString("course_name")));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки курсов!");
        }
    }

    private void addCourse() {
        String name = courseNameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("Введите название курса.");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO courses (course_name) VALUES (?)");
            stmt.setString(1, name);
            stmt.executeUpdate();
            statusLabel.setText("Курс добавлен.");
            loadCourses();
            courseNameField.clear();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка добавления курса.");
        }
    }

    private void deleteCourse() {
        Course selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите курс для удаления.");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM courses WHERE id=?");
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            statusLabel.setText("Курс удалён.");
            loadCourses();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка удаления курса.");
        }
    }


    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<Course, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Course, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }

    private class NameCellFactory implements Callback<TableColumn.CellDataFeatures<Course, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Course, String> param) {
            return new SimpleStringProperty(param.getValue().getCourseName());
        }
    }

    private class AddCourseHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            addCourse();
        }
    }

    private class DeleteCourseHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            deleteCourse();
        }
    }
}

