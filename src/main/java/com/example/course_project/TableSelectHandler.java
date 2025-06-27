package com.example.course_project;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class TableSelectHandler implements EventHandler<ActionEvent> {
    private AdminView view;

    public TableSelectHandler(AdminView view) {
        this.view = view;
    }

    @Override
    public void handle(ActionEvent event) {
        String t = view.getTableSelector().getValue();
        new AdminController(view).loadTable(t);
    }
}

