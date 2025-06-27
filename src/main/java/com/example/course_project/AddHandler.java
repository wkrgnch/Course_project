package com.example.course_project;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextInputDialog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class AddHandler implements EventHandler<ActionEvent> {
    private AdminView view;

    public AddHandler(AdminView view) {
        this.view = view;
    }

    @Override
    public void handle(ActionEvent event) {
        String table = view.getTableSelector().getValue();
        if (table == null) {
            LoginController.showAlert("Error", "Please select a table first");
            return;
        }

        // Запрашиваем имена колонок
        TextInputDialog colDialog = new TextInputDialog();
        colDialog.setHeaderText("New row for `" + table + "`");
        colDialog.setContentText("Enter column names (comma-separated):");
        colDialog.showAndWait();  // показываем диалог
        String colsInput = colDialog.getResult();  // вернёт null, если закрыли без ввода
        if (colsInput == null || colsInput.trim().isEmpty()) {
            return;  // отмена
        }

        // Запрашиваем значения
        TextInputDialog valDialog = new TextInputDialog();
        valDialog.setHeaderText("Values for `" + table + "`");
        valDialog.setContentText("Enter values (comma-separated, in same order):");
        valDialog.showAndWait();
        String valsInput = valDialog.getResult();
        if (valsInput == null || valsInput.trim().isEmpty()) {
            return;
        }

        String[] cols = colsInput.split("\\s*,\\s*");
        String[] vals = valsInput.split("\\s*,\\s*");
        if (cols.length != vals.length) {
            LoginController.showAlert("Error", "Columns count must match values count");
            return;
        }

        // Строим SQL-запрос
        StringBuilder sbCols = new StringBuilder();
        StringBuilder sbPlace = new StringBuilder();
        for (int i = 0; i < cols.length; i++) {
            if (i > 0) {
                sbCols.append(", ");
                sbPlace.append(", ");
            }
            sbCols.append(cols[i]);
            sbPlace.append("?");
        }
        String sql = "INSERT INTO " + table +
                " (" + sbCols + ") VALUES (" + sbPlace + ")";

        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < vals.length; i++) {
                ps.setString(i + 1, vals[i]);
            }
            ps.executeUpdate();
            LoginController.showAlert("Success", "Row added to " + table);
            new AdminController(view).loadTable(table);

        } catch (SQLException e) {
            e.printStackTrace();
            LoginController.showAlert("Error", "DB error: " + e.getMessage());
        }
    }
}


