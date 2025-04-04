// package UI;

// import Database.DatabaseConnection;
// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// public class LoginForm extends JFrame {

//     private JTextField usernameField;
//     private JPasswordField passwordField;
//     private JComboBox<String> roleComboBox;
//     private JButton loginButton, backButton;

//     public LoginForm() {
//         setTitle("Login - EduMark");
//         setSize(400, 300);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new GridLayout(5, 2));

//         // UI Components
//         add(new JLabel("Username:"));
//         usernameField = new JTextField();
//         add(usernameField);

//         add(new JLabel("Password:"));
//         passwordField = new JPasswordField();
//         add(passwordField);

//         add(new JLabel("Role:"));
//         roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});
//         add(roleComboBox);

//         loginButton = new JButton("Login");
//         backButton = new JButton("Back");

//         // Login Button Action
//         loginButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 loginUser();
//             }
//         });

//         // Back Button Action
//         backButton.addActionListener(new ActionListener() {
//             public void actionPerformed(ActionEvent e) {
//                 new InitialPage();
//                 dispose(); // Return to the initial page
//             }
//         });

//         add(loginButton);
//         add(backButton);
//         setVisible(true);
//     }

//     private void loginUser() {
//         String username = usernameField.getText();
//         String password = new String(passwordField.getPassword());
//         String selectedRole = (String) roleComboBox.getSelectedItem();

//         if (username.isEmpty() || password.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         try (Connection conn = DatabaseConnection.getConnection()) {
//             String query = "SELECT * FROM Users WHERE Username = ? AND Password = ? AND Role = ?";
//             PreparedStatement stmt = conn.prepareStatement(query);
//             stmt.setString(1, username);
//             stmt.setString(2, password);
//             stmt.setString(3, selectedRole);
//             ResultSet rs = stmt.executeQuery();

//             if (rs.next()) {
//                 JOptionPane.showMessageDialog(this, "Login Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

//                 // Redirect to appropriate dashboard
//                 if ("Student".equals(selectedRole)) {
//                     new StudentDashboard(username);
//                 } else {
//                     new TeacherDashboard(username);
//                 }
//                 dispose();
//             } else {
//                 JOptionPane.showMessageDialog(this, "Invalid username, password, or role.", "Error", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }
// }

// package UI;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.*;
// import java.sql.*;
// import Database.DatabaseConnection;

// public class LoginForm extends JFrame {

//     private JTextField usernameField;
//     private JPasswordField passwordField;
//     private JComboBox<String> roleComboBox;
//     private Connection connection;

//     public LoginForm() {
//         connection = DatabaseConnection.getConnection();

//         setTitle("Login Form");
//         setSize(400, 300);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         JPanel panel = new JPanel();
//         panel.setLayout(new GridLayout(5, 2, 10, 10));

//         JLabel usernameLabel = new JLabel("Username:");
//         usernameField = new JTextField();

//         JLabel passwordLabel = new JLabel("Password:");
//         passwordField = new JPasswordField();

//         JLabel roleLabel = new JLabel("Role:");
//         String[] roles = { "Student", "Teacher" };
//         roleComboBox = new JComboBox<>(roles);

//         JButton loginButton = new JButton("Login");

//         loginButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 String username = usernameField.getText();
//                 String password = new String(passwordField.getPassword());
//                 String role = (String) roleComboBox.getSelectedItem();

//                 if (username.isEmpty() || password.isEmpty()) {
//                     JOptionPane.showMessageDialog(null, "Please enter all fields.");
//                     return;
//                 }

//                 if (role.equals("Student")) {
//                     authenticateStudent(username, password);
//                 } else if (role.equals("Teacher")) {
//                     authenticateTeacher(username, password);
//                 }
//             }
//         });

//         panel.add(usernameLabel);
//         panel.add(usernameField);
//         panel.add(passwordLabel);
//         panel.add(passwordField);
//         panel.add(roleLabel);
//         panel.add(roleComboBox);
//         panel.add(new JLabel());
//         panel.add(loginButton);

//         add(panel);
//         setVisible(true);
//     }

//     private void authenticateStudent(String username, String password) {
//         try {
//             String query = "SELECT * FROM Users WHERE Username = ? AND Password = ? AND Role = 'Student'";
//             PreparedStatement pstmt = connection.prepareStatement(query);
//             pstmt.setString(1, username);
//             pstmt.setString(2, password);

//             ResultSet rs = pstmt.executeQuery();

//             if (rs.next()) {
//                 JOptionPane.showMessageDialog(this, "Student Login Successful!");
//                 new StudentDashboard(username);
//                 dispose();
//             } else {
//                 JOptionPane.showMessageDialog(this, "Invalid credentials for Student.");
//             }

//             rs.close();
//             pstmt.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void authenticateTeacher(String username, String password) {
//         try {
//             String query = "SELECT t.TeacherID FROM Users u " +
//                            "JOIN Teachers t ON u.UserID = t.UserID " +
//                            "WHERE u.Username = ? AND u.Password = ? AND u.Role = 'Teacher'";
//             PreparedStatement pstmt = connection.prepareStatement(query);
//             pstmt.setString(1, username);
//             pstmt.setString(2, password);

//             ResultSet rs = pstmt.executeQuery();

//             if (rs.next()) {
//                 int teacherID = rs.getInt("TeacherID");  // Retrieve the teacherID from the result set
//                 JOptionPane.showMessageDialog(this, "Teacher Login Successful!");
//                 new TeacherDashboard(teacherID);  // Passing teacherID as int
//                 dispose();
//             } else {
//                 JOptionPane.showMessageDialog(this, "Invalid credentials for Teacher.");
//             }

//             rs.close();
//             pstmt.close();
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     public static void main(String[] args) {
//         new LoginForm();
//     }
// }


// package UI;

// import Database.DatabaseConnection;

// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;

// public class LoginForm extends JFrame {
//     private JTextField usernameField;
//     private JPasswordField passwordField;
//     private JComboBox<String> roleComboBox;
//     private Connection connection;

//     public LoginForm() {
//         connection = DatabaseConnection.getConnection();

//         setTitle("Login Form");
//         setSize(400, 300);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         JPanel panel = new JPanel();
//         panel.setLayout(new GridLayout(5, 2, 10, 10));

//         JLabel usernameLabel = new JLabel("Username:");
//         usernameField = new JTextField();

//         JLabel passwordLabel = new JLabel("Password:");
//         passwordField = new JPasswordField();

//         JLabel roleLabel = new JLabel("Role:");
//         roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});

//         JButton loginButton = new JButton("Login");
//         JButton backButton = new JButton("Back");  // NEW: Back Button

//         loginButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 handleLogin();
//             }
//         });

//         // Action Listener for Back Button
//         backButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 dispose();  // Close the current LoginForm
//                 new InitialPage();  // Open the InitialPage
//             }
//         });

//         panel.add(usernameLabel);
//         panel.add(usernameField);
//         panel.add(passwordLabel);
//         panel.add(passwordField);
//         panel.add(roleLabel);
//         panel.add(roleComboBox);
//         panel.add(loginButton);
//         panel.add(backButton);  // Adding the Back Button to the panel

//         add(panel);
//         setVisible(true);
//     }

//     private void handleLogin() {
//         String username = usernameField.getText();
//         String password = new String(passwordField.getPassword());
//         String role = (String) roleComboBox.getSelectedItem();

//         try {
//             if (role.equals("Student")) {
//                 String query = "SELECT * FROM Users u JOIN Students s ON u.UserID = s.UserID WHERE u.Username = ? AND u.Password = ? AND u.Role = 'Student'";
//                 PreparedStatement pstmt = connection.prepareStatement(query);
//                 pstmt.setString(1, username);
//                 pstmt.setString(2, password);
//                 ResultSet rs = pstmt.executeQuery();

//                 if (rs.next()) {
//                     int studentID = rs.getInt("StudentID");
//                     dispose();
//                     new StudentDashboard(username).setVisible(true);
//                 } else {
//                     JOptionPane.showMessageDialog(this, "Invalid Student Credentials");
//                 }

//             } else if (role.equals("Teacher")) {
//                 String query = "SELECT * FROM Users u JOIN Teachers t ON u.UserID = t.UserID WHERE u.Username = ? AND u.Password = ? AND u.Role = 'Teacher'";
//                 PreparedStatement pstmt = connection.prepareStatement(query);
//                 pstmt.setString(1, username);
//                 pstmt.setString(2, password);
//                 ResultSet rs = pstmt.executeQuery();

//                 if (rs.next()) {
//                     int teacherID = rs.getInt("TeacherID");
//                     dispose();
//                     new TeacherDashboard(teacherID).setVisible(true);
//                 } else {
//                     JOptionPane.showMessageDialog(this, "Invalid Teacher Credentials");
//                 }
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }

package UI;

import Database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private Connection connection;

    public LoginForm() {
        connection = DatabaseConnection.getConnection();

        setTitle("Login Form");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        JLabel roleLabel = new JLabel("Role:");
        roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});

        JButton loginButton = new JButton("Login");
        JButton backButton = new JButton("Back");  // NEW: Back Button

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        // Action Listener for Back Button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close the current LoginForm
                new InitialPage();  // Open the InitialPage
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(roleLabel);
        panel.add(roleComboBox);
        panel.add(loginButton);
        panel.add(backButton);  // Adding the Back Button to the panel

        add(panel);
        setVisible(true);
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        try {
            if (role.equals("Student")) {
                String query = "SELECT * FROM Users u JOIN Students s ON u.UserID = s.UserID WHERE u.Username = ? AND u.Password = ? AND u.Role = 'Student'";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int studentID = rs.getInt("StudentID");
                    dispose();
                    new StudentDashboard(username).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Student Credentials");
                }

            } else if (role.equals("Teacher")) {
                String query = "SELECT * FROM Users u JOIN Teachers t ON u.UserID = t.UserID WHERE u.Username = ? AND u.Password = ? AND u.Role = 'Teacher'";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, username);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    int teacherID = rs.getInt("TeacherID");
                    dispose();
                    new TeacherDashboard(teacherID).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Teacher Credentials");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
