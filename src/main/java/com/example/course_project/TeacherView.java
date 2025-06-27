package com.example.course_project;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.*;

public class TeacherView extends Application {
    private User teacherUser;
    private TableView<TeacherCourseInfo> table;

    public TeacherView(User user) {
        this.teacherUser = user;
    }

    @Override
    public void start(Stage stage) {
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        Label welcomeLabel = new Label("Здравствуйте, " + teacherUser.getUsername());

        table = new TableView<>();
        TableColumn<TeacherCourseInfo, String> courseCol = new TableColumn<>("Курс");
        courseCol.setCellValueFactory(new CourseNameCellFactory());
        TableColumn<TeacherCourseInfo, String> groupCol = new TableColumn<>("Группа");
        groupCol.setCellValueFactory(new GroupNumberCellFactory());
        TableColumn<TeacherCourseInfo, String> specCol = new TableColumn<>("Специальность");
        specCol.setCellValueFactory(new SpecialityCellFactory());
        TableColumn<TeacherCourseInfo, String> dirCol = new TableColumn<>("Направление");
        dirCol.setCellValueFactory(new DirectionCellFactory());

        table.getColumns().addAll(courseCol, groupCol, specCol, dirCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Button addCourseBtn = new Button("Создать новый курс");
        addCourseBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addCourseBtn.setOnAction(new ShowAddCourseDialogHandler());

        layout.getChildren().addAll(welcomeLabel, table, addCourseBtn);

        loadTeacherCourses();

        Scene scene = new Scene(layout, 800, 400);
        stage.setScene(scene);
        stage.setTitle("Панель преподавателя");
        stage.show();
    }

    private void loadTeacherCourses() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            String sql = "SELECT c.course_name, g.group_number, g.speciality, g.direction " +
                    "FROM lessons l " +
                    "JOIN courses c ON l.course_id = c.id " +
                    "JOIN groups g ON l.group_id = g.id " +
                    "WHERE l.teacher_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, teacherUser.getTeacherId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                table.getItems().add(new TeacherCourseInfo(
                        rs.getString("course_name"),
                        rs.getInt("group_number"),
                        rs.getString("speciality"),
                        rs.getString("direction")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAddCourseDialog() {
        Stage dialog = new Stage();
        VBox dialogVBox = new VBox(10);
        dialogVBox.setPadding(new Insets(10));

        TextField courseNameField = new TextField();
        courseNameField.setPromptText("Название курса");

        TextField hoursField = new TextField();
        hoursField.setPromptText("Количество часов");

        Button addBtn = new Button("Добавить");
        addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addBtn.setOnAction(new AddTeacherCourseHandler(courseNameField, hoursField, dialog));

        dialogVBox.getChildren().addAll(new Label("Новый курс:"), courseNameField, hoursField, addBtn);
        Scene dialogScene = new Scene(dialogVBox, 300, 200);
        dialog.setScene(dialogScene);
        dialog.setTitle("Добавление курса");
        dialog.show();
    }


    public static class TeacherCourseInfo {
        private String courseName;
        private int groupNumber;
        private String speciality;
        private String direction;

        public TeacherCourseInfo(String courseName, int groupNumber, String speciality, String direction) {
            this.courseName = courseName;
            this.groupNumber = groupNumber;
            this.speciality = speciality;
            this.direction = direction;
        }

        public String getCourseName() { return courseName; }
        public int getGroupNumber() { return groupNumber; }
        public String getSpeciality() { return speciality; }
        public String getDirection() { return direction; }
    }


    private class CourseNameCellFactory implements Callback<TableColumn.CellDataFeatures<TeacherCourseInfo, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<TeacherCourseInfo, String> param) {
            return new SimpleStringProperty(param.getValue().getCourseName());
        }
    }

    private class GroupNumberCellFactory implements Callback<TableColumn.CellDataFeatures<TeacherCourseInfo, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<TeacherCourseInfo, String> param) {
            return new SimpleStringProperty(String.valueOf(param.getValue().getGroupNumber()));
        }
    }

    private class SpecialityCellFactory implements Callback<TableColumn.CellDataFeatures<TeacherCourseInfo, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<TeacherCourseInfo, String> param) {
            return new SimpleStringProperty(param.getValue().getSpeciality());
        }
    }

    private class DirectionCellFactory implements Callback<TableColumn.CellDataFeatures<TeacherCourseInfo, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<TeacherCourseInfo, String> param) {
            return new SimpleStringProperty(param.getValue().getDirection());
        }
    }

    private class ShowAddCourseDialogHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) { showAddCourseDialog(); }
    }

    private class AddTeacherCourseHandler implements EventHandler<ActionEvent> {
        private TextField courseNameField;
        private TextField hoursField;
        private Stage dialog;

        public AddTeacherCourseHandler(TextField field, TextField hoursField, Stage dialog) {
            this.courseNameField = field;
            this.hoursField = hoursField;
            this.dialog = dialog;
        }

        public void handle(ActionEvent event) {
            String courseName = courseNameField.getText().trim();
            String hoursText = hoursField.getText().trim();
            if (courseName.isEmpty() || hoursText.isEmpty()) return;
            try {
                int hours = Integer.parseInt(hoursText);
                Connection conn = Database.getConnection();

                // добавляем курс
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO courses (course_name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, courseName);
                stmt.executeUpdate();
                ResultSet rs = stmt.getGeneratedKeys();
                int courseId = -1;
                if (rs.next()) {
                    courseId = rs.getInt(1);
                }

                // добавляем назначение преподавателя
                if (courseId != -1) {
                    PreparedStatement stmt2 = conn.prepareStatement("INSERT INTO assignments (teacher_id, course_id, number_of_academic_hours) VALUES (?, ?, ?)");
                    stmt2.setInt(1, teacherUser.getTeacherId());
                    stmt2.setInt(2, courseId);
                    stmt2.setInt(3, hours);
                    stmt2.executeUpdate();
                }
                loadTeacherCourses();
                dialog.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}