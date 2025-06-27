package com.example.course_project;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.sql.*;

public class LoginHandler implements EventHandler<ActionEvent> {
    private LoginView view;
    private Stage stage;

    public LoginHandler(LoginView view, Stage stage) {
        this.view = view;
        this.stage = stage;
    }

    @Override
    public void handle(ActionEvent event) {
        String user = view.getUsernameField().getText();
        String pass = view.getPasswordField().getText();
        try {
            String hash = LoginController.md5(pass);
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "SELECT id, role, teacher_id FROM users " +
                                 "WHERE username=? AND password=?"
                 )) {
                ps.setString(1, user);
                ps.setString(2, hash);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String role = rs.getString("role");
                    if ("admin".equals(role)) {
                        AdminView av = new AdminView();
                        new AdminController(av);
                        stage.setScene(
                                new Scene(av.getRoot(), 800, 600)
                        );
                    } else {
                        int tid = rs.getInt("teacher_id");
                        TeacherView tv = new TeacherView(tid);
                        new TeacherController(tv, tid);
                        stage.setScene(
                                new Scene(tv.getRoot(), 800, 600)
                        );
                    }
                } else {
                    LoginController.showAlert(
                            "Login failed","Invalid credentials"
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LoginController.showAlert("Error","Hash or DB error");
        }
    }
}
