package com.example.course_project;


import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class TeacherView {
    private BorderPane root;
    private Label infoLabel;
    private TableView<?> table;
    private Button createCourseButton;

    public TeacherView(int teacherId) {
        root = new BorderPane();
        root.setPadding(new Insets(10));

        infoLabel = new Label("Преподаватель #" + teacherId);
        root.setTop(infoLabel);

        table = new TableView<>();
        root.setCenter(table);

        createCourseButton = new Button("Create New Course");
        createCourseButton.setStyle("-fx-background-color: #8BC34A;");
        HBox hb = new HBox(createCourseButton);
        root.setBottom(hb);
    }

    public BorderPane getRoot() { return root; }
    public Label getInfoLabel() { return infoLabel; }
    public TableView<?> getTable() { return table; }
    public Button getCreateCourseButton() { return createCourseButton; }
}
