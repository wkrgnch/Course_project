package com.example.course_project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.sql.*;

public class AssignmentController extends VBox {
    private TableView<Assignment> table;
    private Label statusLabel;
    private TextField teacherIdField, courseIdField, hoursField;

    public AssignmentController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Назначения");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        statusLabel = new Label();

        TableColumn<Assignment, Integer> teacherCol = new TableColumn<>("ID преподавателя");
        teacherCol.setCellValueFactory(new TeacherIdCellFactory());
        TableColumn<Assignment, Integer> courseCol = new TableColumn<>("ID курса");
        courseCol.setCellValueFactory(new CourseIdCellFactory());
        TableColumn<Assignment, Integer> hoursCol = new TableColumn<>("Часы");
        hoursCol.setCellValueFactory(new HoursCellFactory());

        teacherIdField = new TextField();
        teacherIdField.setPromptText("ID преподавателя");
        courseIdField = new TextField();
        courseIdField.setPromptText("ID курса");
        hoursField = new TextField();
        hoursField.setPromptText("Часы");

        Button addBtn = new Button("Добавить");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(new AddAssignmentHandler());

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(new DeleteAssignmentHandler());
        table.getColumns().addAll(teacherCol, courseCol, hoursCol);

        HBox controls = new HBox(5, teacherIdField, courseIdField, hoursField, addBtn, deleteBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(label, table, controls, statusLabel);
        loadAssignments();

    }

    private void loadAssignments() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM assignments");
            while (rs.next()) {
                table.getItems().add(new Assignment(
                        rs.getInt("teacher_id"),
                        rs.getInt("course_id"),
                        rs.getInt("number_of_academic_hours")
                ));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки назначений!");
        }
    }


    private void addAssignment() {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO assignments (teacher_id, course_id, number_of_academic_hours) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(teacherIdField.getText()));
            stmt.setInt(2, Integer.parseInt(courseIdField.getText()));
            stmt.setInt(3, Integer.parseInt(hoursField.getText()));
            stmt.executeUpdate();
            statusLabel.setText("Назначение добавлено.");
            loadAssignments();
        } catch (Exception e) {
            statusLabel.setText("Ошибка добавления назначения.");
        }
    }

    private void deleteAssignment() {
        Assignment selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите назначение для удаления.");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM assignments WHERE teacher_id=? AND course_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getTeacherId());
            stmt.setInt(2, selected.getCourseId());
            stmt.executeUpdate();
            statusLabel.setText("Назначение удалено.");
            loadAssignments();
        } catch (Exception e) {
            statusLabel.setText("Ошибка удаления назначения.");
        }
    }

    private class AddAssignmentHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { addAssignment(); }
    }

    private class DeleteAssignmentHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { deleteAssignment(); }
    }

    private class TeacherIdCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getTeacherId()).asObject();
        }
    }
    private class CourseIdCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getCourseId()).asObject();
        }
    }
    private class HoursCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getHours()).asObject();
        }
    }
}

