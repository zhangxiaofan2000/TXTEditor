package org.example;

public class Main {
    public static void main(String[] args) {

        TextEditor editor = new TextEditor();

        LoginWindow loginWindow = new LoginWindow();
        loginWindow.setLoginSuccessCallback(() -> {
            editor.setVisible(true);
        });
        loginWindow.setVisible(true);
    }
}