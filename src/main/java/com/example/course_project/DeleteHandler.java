package com.example.course_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteHandler implements EventHandler<ActionEvent> {
    private AdminView view;

    public DeleteHandler(AdminView view) {
        this.view = view;
    }

    @Override
    public void handle(ActionEvent event) {
        String table = view.getTableSelector().getValue();
        Object selected = view.getTable().getSelectionModel().getSelectedItem();

        if (table == null || selected == null) {
            LoginController.showAlert("Error", "Select table and row first");
            return;
        }

        try {
            // Рефлексия: ищем метод getId()
            Method m = selected.getClass().getMethod("getId");
            Object idObj = m.invoke(selected);
            int id = Integer.parseInt(idObj.toString());

            String sql = "DELETE FROM " + table + " WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                LoginController.showAlert("Success", "Deleted id=" + id);
                new AdminController(view).loadTable(table);
            }

        } catch (NoSuchMethodException ex) {
            LoginController.showAlert("Error", "No getId() on this row type");
        } catch (SQLException ex) {
            ex.printStackTrace();
            LoginController.showAlert("Error", "DB error: " + ex.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
            LoginController.showAlert("Error", "Reflection error");
        }
    }
}

