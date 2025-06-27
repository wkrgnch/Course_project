package com.example.course_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.sql.*;

public class RegisterHandler implements EventHandler<ActionEvent> {
    private LoginView view;

    public RegisterHandler(LoginView view) {
        this.view = view;
    }

    @Override
    public void handle(ActionEvent event) {
        String user = view.getUsernameField().getText();
        String pass = view.getPasswordField().getText();
        try {
            String hash = LoginController.md5(pass);
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(
                         "INSERT INTO users(username,password,role) " +
                                 "VALUES(?,?,'teacher')"
                 )) {
                ps.setString(1, user);
                ps.setString(2, hash);
                ps.executeUpdate();
                LoginController.showAlert(
                        "Success","Registration complete"
                );
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
            LoginController.showAlert(
                    "Error","Username exists or DB error"
            );
        } catch (Exception e) {
            e.printStackTrace();
            LoginController.showAlert("Error","Hash error");
        }
    }
}
