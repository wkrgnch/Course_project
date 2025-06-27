package com.example.course_project;

import javafx.stage.Stage;


public class LoginController {
    private LoginView view;
    private Stage stage;

    public LoginController(LoginView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        view.getLoginButton().setOnAction(
                new LoginHandler(view, stage)
        );
        view.getRegisterButton().setOnAction(
                new RegisterHandler(view)
        );
    }

    public static void showAlert(String title, String msg) {
        javafx.scene.control.Alert a =
                new javafx.scene.control.Alert(
                        javafx.scene.control.Alert.AlertType.INFORMATION);
        a.setTitle(title);
        a.setContentText(msg);
        a.showAndWait();
    }

    public static String md5(String s) throws Exception {
        java.security.MessageDigest m =
                java.security.MessageDigest.getInstance("MD5");
        byte[] d = m.digest(
                s.getBytes(java.nio.charset.StandardCharsets.UTF_8)
        );
        StringBuilder sb = new StringBuilder();
        for (byte b : d) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
