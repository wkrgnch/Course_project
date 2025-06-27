package com.example.course_project;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.math.BigDecimal;
import java.sql.*;

public class LessonController extends VBox {
    private TableView<Lesson> table;
    private Label statusLabel;

    public LessonController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Занятия");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Lesson, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new IdCellFactory());
        TableColumn<Lesson, Integer> teacherCol = new TableColumn<>("ID преподавателя");
        teacherCol.setCellValueFactory(new TeacherIdCellFactory());
        TableColumn<Lesson, Integer> groupCol = new TableColumn<>("ID группы");
        groupCol.setCellValueFactory(new GroupIdCellFactory());
        TableColumn<Lesson, Integer> courseCol = new TableColumn<>("ID курса");
        courseCol.setCellValueFactory(new CourseIdCellFactory());
        TableColumn<Lesson, String> typeCol = new TableColumn<>("Тип");
        typeCol.setCellValueFactory(new TypeCellFactory());
        TableColumn<Lesson, BigDecimal> costCol = new TableColumn<>("Стоимость");
        costCol.setCellValueFactory(new CostCellFactory());

        table.getColumns().addAll(idCol, teacherCol, groupCol, courseCol, typeCol, costCol);

        statusLabel = new Label();
        getChildren().addAll(label, table, statusLabel);

        loadLessons();
    }

    private void loadLessons() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM lessons");
            while (rs.next()) {
                table.getItems().add(new Lesson(
                        rs.getInt("id"),
                        rs.getInt("teacher_id"),
                        rs.getInt("group_id"),
                        rs.getInt("course_id"),
                        rs.getString("activity_type"),
                        rs.getBigDecimal("cost_per_hour")
                ));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки занятий!");
        }
    }

    // Внутренние классы для колонок
    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }
    private class TeacherIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getTeacherId()).asObject();
        }
    }
    private class GroupIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getGroupId()).asObject();
        }
    }
    private class CourseIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, javafx.beans.value.ObservableValue<Integer>> {
        public javafx.beans.value.ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new javafx.beans.property.SimpleIntegerProperty(param.getValue().getCourseId()).asObject();
        }
    }
    private class TypeCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, String>, javafx.beans.value.ObservableValue<String>> {
        public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<Lesson, String> param) {
            return new javafx.beans.property.SimpleStringProperty(param.getValue().getActivityType().toString());
        }
    }
    private class CostCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, BigDecimal>, javafx.beans.value.ObservableValue<BigDecimal>> {
        public javafx.beans.value.ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<Lesson, BigDecimal> param) {
            return new javafx.beans.property.SimpleObjectProperty<>(param.getValue().getCostPerHour());
        }
    }
}

