package com.example.course_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Обработчик создания нового курса: открывает диалог ввода названия курса,
 * добавляет запись в таблицу courses, затем связывает курс с преподавателем
 * в таблице assignments (с 0 часов), и обновляет список курсов в TeacherView.
 */
public class CreateCourseHandler implements EventHandler<ActionEvent> {
    private final TeacherView view;
    private final int teacherId;

    public CreateCourseHandler(TeacherView view, int teacherId) {
        this.view = view;
        this.teacherId = teacherId;
    }

    @Override
    public void handle(ActionEvent event) {
        // Диалог для ввода названия нового курса
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText("Create New Course");
        dialog.setContentText("Enter course name:");
        dialog.showAndWait();
        String courseName = dialog.getResult();
        if (courseName == null || courseName.trim().isEmpty()) {
            // Пользователь отменил или не ввёл название
            return;
        }

        Connection conn = null;
        PreparedStatement psCourse = null;
        PreparedStatement psAssign = null;
        ResultSet rsKeys = null;

        try {
            conn = Database.getConnection();
            conn.setAutoCommit(false);  // начинаем транзакцию

            // Вставляем новый курс и получаем сгенерированный ID
            String sqlCourse = "INSERT INTO courses(course_name) VALUES(?)";
            psCourse = conn.prepareStatement(sqlCourse, PreparedStatement.RETURN_GENERATED_KEYS);
            psCourse.setString(1, courseName);
            psCourse.executeUpdate();
            rsKeys = psCourse.getGeneratedKeys();
            if (!rsKeys.next()) {
                throw new SQLException("No ID obtained for new course");
            }
            int newCourseId = rsKeys.getInt(1);

            // Создаём связь преподавателя и курса в assignments с 0 часов
            String sqlAssign = "INSERT INTO assignments(teacher_id, course_id, number_of_academic_hours) VALUES(?, ?, 0)";
            psAssign = conn.prepareStatement(sqlAssign);
            psAssign.setInt(1, teacherId);
            psAssign.setInt(2, newCourseId);
            psAssign.executeUpdate();

            conn.commit();  // фиксируем транзакцию

            LoginController.showAlert("Success", "Course \"" + courseName + "\" created");
            // Обновляем таблицу курсов
            new TeacherController(view, teacherId).loadCourses();

        } catch (SQLException ex) {
            // При ошибке откатываем изменения
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollEx) {
                rollEx.printStackTrace();
            }
            ex.printStackTrace();
            LoginController.showAlert("Error", "Database error: " + ex.getMessage());
        } finally {
            // Восстанавливаем авто-коммит и закрываем ресурсы
            try {
                if (conn != null) conn.setAutoCommit(true);
                if (rsKeys != null) rsKeys.close();
                if (psCourse != null) psCourse.close();
                if (psAssign != null) psAssign.close();
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }
}
