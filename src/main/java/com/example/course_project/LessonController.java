package com.example.course_project;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private TextField teacherIdField, groupIdField, courseIdField, typeField, costField;

    public LessonController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Занятия");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        statusLabel = new Label();

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

        teacherIdField = new TextField();
        teacherIdField.setPromptText("ID преподавателя");
        groupIdField = new TextField();
        groupIdField.setPromptText("ID группы");
        courseIdField = new TextField();
        courseIdField.setPromptText("ID курса");
        typeField = new TextField();
        typeField.setPromptText("Тип (Лекция/Практика)");
        costField = new TextField();
        costField.setPromptText("Стоимость в час");

        Button addBtn = new Button("Добавить");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(new AddLessonHandler());

        Button deleteBtn = new Button("Удалить");
        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteBtn.setOnAction(new DeleteLessonHandler());

        table.getColumns().addAll(idCol, teacherCol, groupCol, courseCol, typeCol, costCol);

        HBox controls = new HBox(5, teacherIdField, groupIdField, courseIdField, typeField, costField, addBtn, deleteBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(label, table, controls, statusLabel);
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

    private void addLesson() {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO lessons (teacher_id, group_id, course_id, activity_type, cost_per_hour) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(teacherIdField.getText()));
            stmt.setInt(2, Integer.parseInt(groupIdField.getText()));
            stmt.setInt(3, Integer.parseInt(courseIdField.getText()));
            stmt.setString(4, typeField.getText());
            stmt.setBigDecimal(5, new java.math.BigDecimal(costField.getText()));
            stmt.executeUpdate();
            statusLabel.setText("Занятие добавлено.");
            loadLessons();
        } catch (Exception e) {
            statusLabel.setText("Ошибка добавления занятия.");
        }
    }

    private void deleteLesson() {
        Lesson selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите занятие для удаления.");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM lessons WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            statusLabel.setText("Занятие удалено.");
            loadLessons();
        } catch (Exception e) {
            statusLabel.setText("Ошибка удаления занятия.");
        }
    }

    private class AddLessonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { addLesson(); }
    }

    private class DeleteLessonHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { deleteLesson(); }
    }

    // Внутренние классы для колонок
    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }
    private class TeacherIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getTeacherId()).asObject();
        }
    }
    private class GroupIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getGroupId()).asObject();
        }
    }
    private class CourseIdCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Lesson, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getCourseId()).asObject();
        }
    }
    private class TypeCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Lesson, String> param) {
            return new SimpleStringProperty(param.getValue().getActivityType().toString());
        }
    }
    private class CostCellFactory implements Callback<TableColumn.CellDataFeatures<Lesson, BigDecimal>, ObservableValue<BigDecimal>> {
        public ObservableValue<BigDecimal> call(TableColumn.CellDataFeatures<Lesson, BigDecimal> param) {
            return new SimpleObjectProperty<>(param.getValue().getCostPerHour());
        }
    }
}

