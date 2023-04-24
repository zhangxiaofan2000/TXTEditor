package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Objects;

public class RegisterWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JLabel messageLabel;

    public RegisterWindow() {
        setTitle("注册");
        setSize(400, 200);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/logo.jpg")));
        setIconImage(icon.getImage());

        // 创建用户名和密码输入框
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        inputPanel.add(new JLabel("用户名:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);
        inputPanel.add(new JLabel("密码:"));
        passwordField = new JPasswordField();
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("确认密码:"));
        confirmPasswordField = new JPasswordField();
        inputPanel.add(confirmPasswordField);

        // 创建注册按钮
        JButton registerButton = new JButton("注册");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());
                String confirmPassword = new String(confirmPasswordField.getPassword());
                if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    messageLabel.setText("请输入用户名和密码");
                    return;
                }
                if (!password.equals(confirmPassword)) {
                    messageLabel.setText("两次输入的密码不一致");
                    return;
                }
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/txt_editor", "root", "123456")) {
                    String sql = "SELECT * FROM user WHERE username = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        messageLabel.setText("用户名已存在");
                        return;
                    }
                    sql = "INSERT INTO user (username, password) VALUES (?, ?)";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, username);
                    pstmt.setString(2, password);
                    pstmt.executeUpdate();
                    JOptionPane.showMessageDialog(RegisterWindow.this, "注册成功");
                    dispose();
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
        mainPanel.add(registerButton, BorderLayout.EAST);
        mainPanel.add(messageLabel, BorderLayout.SOUTH);
        add(mainPanel);
    }
}
