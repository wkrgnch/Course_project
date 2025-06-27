package com.example.course_project;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;


import java.sql.*;

public class TeacherController {
    private TeacherView view;
    private int teacherId;

    public TeacherController(TeacherView view, int teacherId) {
        this.view = view;
        this.teacherId = teacherId;
        loadInfo();
        loadCourses();
        view.getCreateCourseButton().setOnAction(
                new CreateCourseHandler(view, teacherId)
        );
    }

    public void loadInfo() {
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT surname,name,patronymic FROM teachers WHERE id=?"
             )) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String fio = rs.getString("surname")+" "+
                        rs.getString("name")+" "+
                        rs.getString("patronymic");
                view.getInfoLabel().setText("Преподаватель: "+fio);
            }
        } catch (SQLException e) {
            view.getInfoLabel().setText("Ошибка загрузки преподавателя");
        }
    }

    public void loadCourses() {
        @SuppressWarnings("unchecked")
        TableView<CourseRow> tbl = (TableView<CourseRow>)view.getTable();
        tbl.getItems().clear();
        tbl.getColumns().clear();
        // настройка колонок...
        ObservableList<CourseRow> data = FXCollections.observableArrayList();
        String sql =
                "SELECT c.id AS cid, c.course_name, g.group_number, " +
                        "g.speciality, g.direction " +
                        "FROM lessons l " +
                        "JOIN courses c ON l.course_id=c.id " +
                        "JOIN groups g ON l.group_id=g.id " +
                        "WHERE l.teacher_id=?";
        try (Connection c = Database.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, teacherId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                data.add(new CourseRow(
                        rs.getInt("cid"),
                        rs.getString("course_name"),
                        rs.getInt("group_number"),
                        rs.getString("speciality"),
                        rs.getString("direction")
                ));
            }
            tbl.setItems(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

