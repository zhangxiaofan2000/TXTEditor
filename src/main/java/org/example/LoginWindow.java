package org.example;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private Runnable loginSuccessCallback;

    public void setLoginSuccessCallback(Runnable callback) {
        this.loginSuccessCallback = callback;
    }
    public LoginWindow() {
        setTitle("登录");
        setSize(400, 150);
        setLocationRelativeTo(null);

        // 创建用户名和密码输入框
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);

        // 创建登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("请输入用户名和密码");
                    return;
                }
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/txt_editor", "root", "123456")) {
                    String sql = "SELECT * FROM user WHERE username = ? AND password = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(LoginWindow.this, "登录成功");
                        dispose();
                        // 登录成功后，调用回调函数
                        if (loginSuccessCallback != null) {
                            loginSuccessCallback.run();
                        }
                    } else {
                        messageLabel.setText("用户名或密码错误");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    messageLabel.setText("数据库连接失败");
                }
            }
        });

        // 创建消息标签
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.RED);

        // 将组件添加到窗口中
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(loginButton, BorderLayout.EAST);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);
        add(mainPanel);
    }

    public static void main(String[] args) {

    }
}
