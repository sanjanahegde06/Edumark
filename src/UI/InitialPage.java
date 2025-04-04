package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InitialPage extends JFrame {
    private JButton loginButton, registerButton;

    public InitialPage() {
        setTitle("Welcome to EduMark");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome label
        JLabel welcomeLabel = new JLabel("Welcome to EduMark", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        // Guidance message
        JLabel messageLabel = new JLabel(
                "<html>If you already have an account, click <b>Login</b>.<br>" +
                "If not, click <b>Register</b> to create a new account.</html>",
                JLabel.CENTER);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        add(messageLabel, BorderLayout.CENTER);

        // Buttons Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        loginButton = new JButton("Login");
        registerButton = new JButton("Register");

        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));

        // Event Listeners
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new LoginForm();
                dispose(); // Close the current window
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterForm();
                dispose(); // Close the current window
            }
        });

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }
}
