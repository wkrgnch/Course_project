package com.example.course_project;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Callback;
import java.sql.*;

public class GroupController extends VBox {
    private TableView<Group> table;
    private TextField groupNumberField, specialityField, directionField, studentCountField;
    private Label statusLabel;

    public GroupController() {
        setSpacing(10);
        setPadding(new Insets(10));

        Label label = new Label("Список групп");
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Group, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new IdCellFactory());
        TableColumn<Group, Integer> numberCol = new TableColumn<>("Номер группы");
        numberCol.setCellValueFactory(new NumberCellFactory());
        TableColumn<Group, String> specialityCol = new TableColumn<>("Специальность");
        specialityCol.setCellValueFactory(new SpecialityCellFactory());
        TableColumn<Group, String> directionCol = new TableColumn<>("Направление");
        directionCol.setCellValueFactory(new DirectionCellFactory());
        TableColumn<Group, Integer> countCol = new TableColumn<>("Кол-во студентов");
        countCol.setCellValueFactory(new CountCellFactory());

        table.getColumns().addAll(idCol, numberCol, specialityCol, directionCol, countCol);

        groupNumberField = new TextField();
        groupNumberField.setPromptText("Номер группы");
        specialityField = new TextField();
        specialityField.setPromptText("Специальность");
        directionField = new TextField();
        directionField.setPromptText("Направление");
        studentCountField = new TextField();
        studentCountField.setPromptText("Кол-во студентов");

        Button addButton = new Button("Добавить");
        addButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        addButton.setOnAction(new AddGroupHandler());

        Button deleteButton = new Button("Удалить");
        deleteButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        deleteButton.setOnAction(new DeleteGroupHandler());

        statusLabel = new Label();

        HBox controls = new HBox(5, groupNumberField, specialityField, directionField, studentCountField, addButton, deleteButton);
        controls.setAlignment(Pos.CENTER_LEFT);

        getChildren().addAll(label, table, controls, statusLabel);
        loadGroups();
    }

    private void loadGroups() {
        table.getItems().clear();
        try {
            Connection conn = Database.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM groups");
            while (rs.next()) {
                table.getItems().add(new Group(
                        rs.getInt("id"),
                        rs.getInt("group_number"),
                        rs.getString("speciality"),
                        rs.getString("direction"),
                        rs.getInt("student_count")
                ));
            }
        } catch (SQLException e) {
            statusLabel.setText("Ошибка загрузки групп!");
        }
    }

    private void addGroup() {
        try {
            Connection conn = Database.getConnection();
            String sql = "INSERT INTO groups (group_number, speciality, direction, student_count) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(groupNumberField.getText()));
            stmt.setString(2, specialityField.getText());
            stmt.setString(3, directionField.getText());
            stmt.setInt(4, Integer.parseInt(studentCountField.getText()));
            stmt.executeUpdate();
            statusLabel.setText("Группа добавлена.");
            loadGroups();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка добавления группы.");
        }
    }

    private void deleteGroup() {
        Group selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Выберите группу!");
            return;
        }
        try {
            Connection conn = Database.getConnection();
            String sql = "DELETE FROM groups WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, selected.getId());
            stmt.executeUpdate();
            statusLabel.setText("Группа удалена.");
            loadGroups();
        } catch (SQLException e) {
            statusLabel.setText("Ошибка удаления группы.");
        }
    }


    private class IdCellFactory implements Callback<TableColumn.CellDataFeatures<Group, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Group, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getId()).asObject();
        }
    }

    private class NumberCellFactory implements Callback<TableColumn.CellDataFeatures<Group, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Group, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getGroupNumber()).asObject();
        }
    }

    private class SpecialityCellFactory implements Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> param) {
            return new SimpleStringProperty(param.getValue().getSpeciality());
        }
    }

    private class DirectionCellFactory implements Callback<TableColumn.CellDataFeatures<Group, String>, ObservableValue<String>> {
        public ObservableValue<String> call(TableColumn.CellDataFeatures<Group, String> param) {
            return new SimpleStringProperty(param.getValue().getDirection());
        }
    }

    private class CountCellFactory implements Callback<TableColumn.CellDataFeatures<Group, Integer>, ObservableValue<Integer>> {
        public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Group, Integer> param) {
            return new SimpleIntegerProperty(param.getValue().getStudentCount()).asObject();
        }
    }

    private class AddGroupHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            addGroup();
        }
    }

    private class DeleteGroupHandler implements EventHandler<ActionEvent> {
        public void handle(ActionEvent event) {
            deleteGroup();
        }
    }
}

