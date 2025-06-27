package com.example.course_project;



import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;

import java.sql.*;

public class AssignmentController extends VBox {
    private TableView<Assignment> table;
    private Label statusLabel;

    public AssignmentController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Назначения");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Assignment, Integer> teacherCol = new TableColumn<>("ID преподавателя");
        teacherCol.setCellValueFactory(new TeacherIdCellFactory());
        TableColumn<Assignment, Integer> courseCol = new TableColumn<>("ID курса");
        courseCol.setCellValueFactory(new CourseIdCellFactory());
        TableColumn<Assignment, Integer> hoursCol = new TableColumn<>("Часы");
        hoursCol.setCellValueFactory(new HoursCellFactory());

        table.getColumns().addAll(teacherCol, courseCol, hoursCol);

        statusLabel = new Label();
        getChildren().addAll(label, table, statusLabel);

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

    // Внутренние классы для колонок
    private class TeacherIdCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getTeacherId()).asObject();
        }
    }
    private class CourseIdCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getCourseId()).asObject();
        }
    }
    private class HoursCellFactory implements Callback<TableColumn.CellDataFeatures<Assignment, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Assignment, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getHours()).asObject();
        }
    }
}

