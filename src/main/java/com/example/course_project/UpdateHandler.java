package com.example.course_project;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UpdateHandler implements EventHandler<ActionEvent> {
    private AdminView view;

    public UpdateHandler(AdminView view) {
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

        // Запрашиваем имя колонки для обновления
        TextInputDialog colDialog = new TextInputDialog();
        colDialog.setHeaderText("Update `" + table + "`");
        colDialog.setContentText("Enter column name to update:");
        colDialog.showAndWait();
        String column = colDialog.getResult();
        if (column == null || column.trim().isEmpty()) {
            return;  // пользователь отменил
        }

        // Запрашиваем новое значение
        TextInputDialog valDialog = new TextInputDialog();
        valDialog.setHeaderText("New value for " + column);
        valDialog.setContentText("Enter new value:");
        valDialog.showAndWait();
        String newValue = valDialog.getResult();
        if (newValue == null) {
            return;  // пользователь отменил
        }

        try {
            // получаем id выбранного объекта рефлексией
            Method getId = selected.getClass().getMethod("getId");
            int id = Integer.parseInt(getId.invoke(selected).toString());

            String sql = "UPDATE " + table + " SET " + column + " = ? WHERE id = ?";
            try (Connection conn = Database.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, newValue);
                ps.setInt(2, id);
                ps.executeUpdate();
                LoginController.showAlert("Success", "Updated id=" + id);
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
