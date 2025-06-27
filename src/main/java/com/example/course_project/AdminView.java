package com.example.course_project;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class AdminView extends Application {
    private User adminUser;

    public AdminView(User user) {
        this.adminUser = user;
    }

    @Override
    public void start(Stage stage) {
        TabPane tabPane = new TabPane();

        Tab coursesTab = new Tab("Курсы", new CourseController());
        Tab teachersTab = new Tab("Преподаватели", new TeacherController());
        Tab groupsTab = new Tab("Группы", new GroupController());
        Tab lessonsTab = new Tab("Занятия", new LessonController());
        Tab assignmentsTab = new Tab("Назначения", new AssignmentController());
        Tab usersTab = new Tab("Пользователи", new UserController());

        tabPane.getTabs().addAll(coursesTab, teachersTab, groupsTab, lessonsTab, assignmentsTab, usersTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        BorderPane root = new BorderPane();
        root.setCenter(tabPane);

        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.setTitle("Панель администратора");
        stage.show();
    }
}

