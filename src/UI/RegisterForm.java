// package UI;

// import Database.DatabaseConnection;
// import javax.swing.*;
// import java.awt.*;
// import java.sql.*;
// import java.util.ArrayList;
// import java.util.HashMap;

// public class RegisterForm extends JFrame {
//     private JComboBox<String> roleComboBox, departmentComboBox, sectionComboBox;
//     private JTextField usernameField, usnField, teacherIdField;
//     private JPasswordField passwordField, confirmPasswordField;
//     private JButton registerButton, backButton;
//     private HashMap<String, ArrayList<String>> departmentSections = new HashMap<>();

//     public RegisterForm() {
//         setTitle("Register - EduMark");
//         setSize(500, 500);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new GridLayout(12, 2));

//         // UI Components
//         add(new JLabel("Role:"));
//         roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});
//         add(roleComboBox);

//         add(new JLabel("Username:"));
//         usernameField = new JTextField();
//         add(usernameField);

//         add(new JLabel("Password:"));
//         passwordField = new JPasswordField();
//         add(passwordField);

//         add(new JLabel("Confirm Password:"));
//         confirmPasswordField = new JPasswordField();
//         add(confirmPasswordField);

//         usnField = new JTextField();
//         teacherIdField = new JTextField();
//         departmentComboBox = new JComboBox<>();
//         sectionComboBox = new JComboBox<>();

//         add(new JLabel("Department:"));
//         add(departmentComboBox);
//         add(new JLabel("Section:"));
//         add(sectionComboBox);
//         add(new JLabel("USN (Students Only):"));
//         add(usnField);
//         add(new JLabel("Teacher ID (Teachers Only):"));
//         add(teacherIdField);

//         registerButton = new JButton("Register");
//         backButton = new JButton("Back");
//         add(registerButton);
//         add(backButton);

//         // Load Departments and Sections
//         loadDepartmentsAndSections();

//         // Event Listeners
//         roleComboBox.addActionListener(e -> toggleFields());
//         departmentComboBox.addActionListener(e -> updateSections());
//         registerButton.addActionListener(e -> registerUser());
//         backButton.addActionListener(e -> {
//             new InitialPage();
//             dispose();
//         });

//         toggleFields();
//         setVisible(true);
//     }

//     private void loadDepartmentsAndSections() {
//         try (Connection conn = DatabaseConnection.getConnection();
//              Statement stmt = conn.createStatement()) {

//             // Load Departments
//             ResultSet rs = stmt.executeQuery("SELECT DepartmentName FROM Departments");
//             while (rs.next()) {
//                 departmentComboBox.addItem(rs.getString("DepartmentName"));
//             }

//             // Load Sections
//             rs = stmt.executeQuery("SELECT d.DepartmentName, s.SectionName FROM Departments d JOIN Sections s ON d.DepartmentID = s.DepartmentID");
//             while (rs.next()) {
//                 String dept = rs.getString("DepartmentName");
//                 String section = rs.getString("SectionName");

//                 departmentSections.computeIfAbsent(dept, k -> new ArrayList<>()).add(section);
//             }
//             updateSections();
//         } catch (SQLException ex) {
//             JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private void updateSections() {
//         String selectedDept = (String) departmentComboBox.getSelectedItem();
//         sectionComboBox.removeAllItems();

//         if (departmentSections.containsKey(selectedDept)) {
//             for (String section : departmentSections.get(selectedDept)) {
//                 sectionComboBox.addItem(section);
//             }
//         }
//     }

//     private void toggleFields() {
//         boolean isStudent = roleComboBox.getSelectedItem().equals("Student");
//         usnField.setEnabled(isStudent);
//         teacherIdField.setEnabled(!isStudent);
//     }

//     private void registerUser() {
//         String username = usernameField.getText();
//         String password = new String(passwordField.getPassword());
//         String confirmPassword = new String(confirmPasswordField.getPassword());
//         String role = (String) roleComboBox.getSelectedItem();
//         String department = (String) departmentComboBox.getSelectedItem();
//         String section = (String) sectionComboBox.getSelectedItem();

//         if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//             JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         if (!password.equals(confirmPassword)) {
//             JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
//             return;
//         }

//         try (Connection conn = DatabaseConnection.getConnection()) {
//             if ("Student".equals(role)) {
//                 if (usnField.getText().isEmpty()) {
//                     JOptionPane.showMessageDialog(this, "USN is required for students.", "Error", JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 // Call InsertStudent procedure
//                 CallableStatement stmt = conn.prepareCall("{CALL InsertStudent(?, ?, ?, ?, ?, ?)}");
//                 stmt.setString(1, username);
//                 stmt.setString(2, password);
//                 stmt.setString(3, usnField.getText());
//                 stmt.setString(4, username);
//                 stmt.setInt(5, getDepartmentId(department, conn));
//                 stmt.setInt(6, getSectionId(section, conn));
//                 stmt.execute();
//             } else {
//                 if (teacherIdField.getText().isEmpty()) {
//                     JOptionPane.showMessageDialog(this, "Teacher ID is required for teachers.", "Error", JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 // Call InsertTeacher procedure
//                 CallableStatement stmt = conn.prepareCall("{CALL InsertTeacher(?, ?, ?, ?, ?)}");
//                 stmt.setString(1, username);
//                 stmt.setString(2, password);
//                 stmt.setString(3, username);
//                 stmt.setInt(4, getDepartmentId(department, conn));
//                 stmt.setInt(5, getSectionId(section, conn));
//                 stmt.execute();
//             }

//             JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
//             new LoginForm();
//             dispose();

//         } catch (SQLException ex) {
//             JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }

//     private int getDepartmentId(String departmentName, Connection conn) throws SQLException {
//         PreparedStatement stmt = conn.prepareStatement("SELECT DepartmentID FROM Departments WHERE DepartmentName = ?");
//         stmt.setString(1, departmentName);
//         ResultSet rs = stmt.executeQuery();
//         return rs.next() ? rs.getInt("DepartmentID") : -1;
//     }

//     private int getSectionId(String sectionName, Connection conn) throws SQLException {
//         PreparedStatement stmt = conn.prepareStatement("SELECT SectionID FROM Sections WHERE SectionName = ?");
//         stmt.setString(1, sectionName);
//         ResultSet rs = stmt.executeQuery();
//         return rs.next() ? rs.getInt("SectionID") : -1;
//     }
// }


package UI;

import Database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RegisterForm extends JFrame {
    private JComboBox<String> roleComboBox, departmentComboBox, sectionComboBox;
    private JTextField usernameField, usnField, teacherIdField, studentNameField, teacherNameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerButton, backButton;
    private HashMap<String, ArrayList<String>> departmentSections = new HashMap<>();
    private JPanel studentPanel, teacherPanel;

    public RegisterForm() {
        setTitle("Register - EduMark");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(mainPanel, BorderLayout.CENTER);

        // Role selection
        JPanel rolePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        rolePanel.add(new JLabel("Role:"));
        roleComboBox = new JComboBox<>(new String[]{"Student", "Teacher"});
        roleComboBox.addActionListener(e -> toggleFields());
        rolePanel.add(roleComboBox);
        mainPanel.add(rolePanel);

        // Username
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.add(new JLabel("Username:"));
        usernameField = new JTextField(20);
        usernamePanel.add(usernameField);
        mainPanel.add(usernamePanel);

        // Password
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(20);
        passwordPanel.add(passwordField);
        mainPanel.add(passwordPanel);

        // Confirm Password
        JPanel confirmPasswordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmPasswordPanel.add(new JLabel("Confirm Password:"));
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordPanel.add(confirmPasswordField);
        mainPanel.add(confirmPasswordPanel);

        // Student-specific fields
        studentPanel = new JPanel();
        studentPanel.setLayout(new BoxLayout(studentPanel, BoxLayout.Y_AXIS));
        studentPanel.setBorder(BorderFactory.createTitledBorder("Student Details"));
        studentNameField = new JTextField(20);
        usnField = new JTextField(20);
        studentPanel.add(createLabeledField("Student Name:", studentNameField));
        studentPanel.add(createLabeledField("USN (Students Only):", usnField));
        mainPanel.add(studentPanel);

        // Teacher-specific fields
        teacherPanel = new JPanel();
        teacherPanel.setLayout(new BoxLayout(teacherPanel, BoxLayout.Y_AXIS));
        teacherPanel.setBorder(BorderFactory.createTitledBorder("Teacher Details"));
        teacherNameField = new JTextField(20);
        teacherIdField = new JTextField(20);
        teacherPanel.add(createLabeledField("Teacher Name:", teacherNameField));
        teacherPanel.add(createLabeledField("Teacher ID:", teacherIdField));
        mainPanel.add(teacherPanel);

        // Department and Section
        JPanel departmentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        departmentPanel.add(new JLabel("Department:"));
        departmentComboBox = new JComboBox<>();
        departmentPanel.add(departmentComboBox);
        mainPanel.add(departmentPanel);

        JPanel sectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sectionPanel.add(new JLabel("Section:"));
        sectionComboBox = new JComboBox<>();
        sectionPanel.add(sectionComboBox);
        mainPanel.add(sectionPanel);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        registerButton = new JButton("Register");
        backButton = new JButton("Back");
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel);

        // Event Listeners
        registerButton.addActionListener(e -> registerUser());
        backButton.addActionListener(e -> {
            new InitialPage();
            dispose();
        });

        // Load Departments and Sections
        loadDepartmentsAndSections();

        // Initial visibility setup
        toggleFields();
        setVisible(true);
    }

    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(labelText));
        panel.add(textField);
        return panel;
    }

    private void loadDepartmentsAndSections() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Load Departments
            ResultSet rs = stmt.executeQuery("SELECT DepartmentName FROM Departments");
            while (rs.next()) {
                departmentComboBox.addItem(rs.getString("DepartmentName"));
            }

            // Load Sections
            rs = stmt.executeQuery("SELECT d.DepartmentName, s.SectionName FROM Departments d JOIN Sections s ON d.DepartmentID = s.DepartmentID");
            while (rs.next()) {
                String dept = rs.getString("DepartmentName");
                String section = rs.getString("SectionName");

                departmentSections.computeIfAbsent(dept, k -> new ArrayList<>()).add(section);
            }
            updateSections();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateSections() {
        String selectedDept = (String) departmentComboBox.getSelectedItem();
        sectionComboBox.removeAllItems();

        if (departmentSections.containsKey(selectedDept)) {
            for (String section : departmentSections.get(selectedDept)) {
                sectionComboBox.addItem(section);
            }
        }
    }

    private void toggleFields() {
        boolean isStudent = roleComboBox.getSelectedItem().equals("Student");

        studentPanel.setVisible(isStudent); // Show Student fields only for "Student" role
        teacherPanel.setVisible(!isStudent); // Show Teacher fields only for "Teacher" role

        revalidate();
        repaint();
    }

    private void registerUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();
        String department = (String) departmentComboBox.getSelectedItem();
        String section = (String) sectionComboBox.getSelectedItem();
        String studentName = studentNameField.getText();
        String teacherName = teacherNameField.getText();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            int sectionId = getSectionId(section, department, conn);
            if (sectionId == -1) {
                JOptionPane.showMessageDialog(this, "Invalid section for the selected department.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if ("Student".equals(role)) {
                if (usnField.getText().isEmpty() || studentName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "USN and Student Name are required for students.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call InsertStudent procedure
                CallableStatement stmt = conn.prepareCall("{CALL InsertStudent(?, ?, ?, ?, ?, ?)}");
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, usnField.getText());
                stmt.setString(4, studentName);
                stmt.setInt(5, getDepartmentId(department, conn));
                stmt.setInt(6, sectionId);
                stmt.execute();
            } else {
                if (teacherIdField.getText().isEmpty() || teacherName.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Teacher ID and Teacher Name are required for teachers.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Call InsertTeacher procedure
                CallableStatement stmt = conn.prepareCall("{CALL InsertTeacher(?, ?, ?, ?, ?)}");
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, teacherName);
                stmt.setInt(4, getDepartmentId(department, conn));
                stmt.setInt(5, sectionId);
                stmt.execute();
            }

            JOptionPane.showMessageDialog(this, "Registration Successful! You can now log in.", "Success", JOptionPane.INFORMATION_MESSAGE);
            new LoginForm();
            dispose();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private int getDepartmentId(String departmentName, Connection conn) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT DepartmentID FROM Departments WHERE DepartmentName = ?");
        stmt.setString(1, departmentName);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("DepartmentID") : -1;
    }

    private int getSectionId(String sectionName, String departmentName, Connection conn) throws SQLException {
        String query = "SELECT s.SectionID FROM Sections s " +
                       "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
                       "WHERE s.SectionName = ? AND d.DepartmentName = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, sectionName);
        stmt.setString(2, departmentName);
        ResultSet rs = stmt.executeQuery();
        return rs.next() ? rs.getInt("SectionID") : -1; // Returns -1 if no match found
    }
}