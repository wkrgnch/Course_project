package com.example.course_project;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class AdminView {
    private BorderPane root;
    private ComboBox<String> tableSelector;
    private TableView<?> table;
    private Button addButton, deleteButton, updateButton;

    public AdminView() {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        tableSelector = new ComboBox<>();
        tableSelector.getItems().addAll("teachers","courses","groups","lessons","assignments","users");
        root.setTop(tableSelector);

        table = new TableView<>();
        root.setCenter(table);

        addButton = new Button("Add");
        addButton.setStyle("-fx-background-color: #8BC34A;");
        deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: #F44336;");
        updateButton = new Button("Update");
        HBox hb = new HBox(10, addButton, deleteButton, updateButton);
        root.setBottom(hb);
    }

    public BorderPane getRoot() { return root; }
    public ComboBox<String> getTableSelector() { return tableSelector; }
    public TableView<?> getTable() { return table; }
    public Button getAddButton() { return addButton; }
    public Button getDeleteButton() { return deleteButton; }
    public Button getUpdateButton() { return updateButton; }
}
