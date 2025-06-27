package com.example.course_project;


import javafx.scene.control.TableView;


import java.sql.*;

public class AdminController {
    private AdminView view;

    public AdminController(AdminView view) {
        this.view = view;
        view.getTableSelector().setOnAction(
                new TableSelectHandler(view)
        );
        view.getAddButton().setOnAction(
                new AddHandler(view)
        );
        view.getDeleteButton().setOnAction(
                new DeleteHandler(view)
        );
        view.getUpdateButton().setOnAction(
                new UpdateHandler(view)
        );

    }



    public void loadTable(String tableName) {
        TableView<?> table = view.getTable();
        table.getItems().clear();
        table.getColumns().clear();
        // пример: SELECT * FROM tableName
        // и заполняете колонками + rows
        try (Connection conn = Database.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(
                     "SELECT * FROM " + tableName
             )) {
            // обёртка в объекты-модели по имени tableName...
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

