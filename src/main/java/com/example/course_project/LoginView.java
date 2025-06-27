package com.example.course_project;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

public class LoginView {
    private GridPane root;
    private TextField usernameField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button registerButton;

    public LoginView() {
        root = new GridPane();
        root.setPadding(new Insets(20));
        root.setHgap(10);
        root.setVgap(10);

        // Поле для логина
        root.add(new Label("Username:"), 0, 0);
        usernameField = new TextField();
        root.add(usernameField, 1, 0);

        // Поле для пароля
        root.add(new Label("Password:"), 0, 1);
        passwordField = new PasswordField();
        root.add(passwordField, 1, 1);

        // Кнопка "Login"
        loginButton = new Button("Login");
        root.add(loginButton, 0, 2);

        // Кнопка "Register"
        registerButton = new Button("Register");
        root.add(registerButton, 1, 2);
    }

    public GridPane getRoot() {
        return root;
    }

    public TextField getUsernameField() {
        return usernameField;
    }

    public PasswordField getPasswordField() {
        return passwordField;
    }

    public Button getLoginButton() {
        return loginButton;
    }

    public Button getRegisterButton() {
        return registerButton;
    }
}
