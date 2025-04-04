// package UI;

// import Database.DatabaseConnection;
// import javax.swing.*;
// import java.awt.*;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// public class TeacherDashboard extends JFrame {

//     private JLabel nameLabel, departmentLabel, sectionLabel;
//     private JButton updateMarksButton, logoutButton;
//     private String username;

//     public TeacherDashboard(String username) {
//         this.username = username;

//         setTitle("Teacher Dashboard - EduMark");
//         setSize(500, 300);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new GridLayout(5, 2, 10, 10));

//         nameLabel = new JLabel();
//         departmentLabel = new JLabel();
//         sectionLabel = new JLabel();

//         add(new JLabel("Teacher Name:")); add(nameLabel);
//         add(new JLabel("Department:")); add(departmentLabel);
//         add(new JLabel("Section:")); add(sectionLabel);

//         updateMarksButton = new JButton("Update Marks");
//         logoutButton = new JButton("Logout");

//         add(updateMarksButton);
//         add(logoutButton);

//         updateMarksButton.addActionListener(e -> {
//             new UpdateMarksForm(username);
//             dispose();
//         });

//         logoutButton.addActionListener(e -> {
//             new InitialPage();
//             dispose();
//         });

//         loadTeacherData();
//         setVisible(true);
//     }

//     private void loadTeacherData() {
//         try (Connection conn = DatabaseConnection.getConnection()) {
//             String teacherQuery = """
//                 SELECT t.TeacherName, d.DepartmentName, sec.SectionName
//                 FROM Teachers t
//                 JOIN Departments d ON t.DepartmentID = d.DepartmentID
//                 JOIN Sections sec ON t.SectionID = sec.SectionID
//                 JOIN Users u ON u.UserID = t.UserID
//                 WHERE u.Username = ?
//             """;

//             PreparedStatement stmt = conn.prepareStatement(teacherQuery);
//             stmt.setString(1, username);
//             ResultSet rs = stmt.executeQuery();

//             if (rs.next()) {
//                 nameLabel.setText(rs.getString("TeacherName"));
//                 departmentLabel.setText(rs.getString("DepartmentName"));
//                 sectionLabel.setText(rs.getString("SectionName"));
//             } else {
//                 JOptionPane.showMessageDialog(this, "Teacher not found!", "Error", JOptionPane.ERROR_MESSAGE);
//             }
//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }
// }




//half proper
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
// import java.util.ArrayList;

// public class TeacherDashboard extends JFrame {
//     private int teacherID;
//     private String teacherUsername;
//     private Connection connection;

//     private JComboBox<String> departmentDropdown;
//     private JComboBox<String> sectionDropdown;
//     private JComboBox<String> usnDropdown;
//     private JComboBox<String> subjectDropdown;
//     private JButton updateMarksButton, backButton, logoutButton;

//     public TeacherDashboard(int teacherID) {
//         this.teacherID = teacherID;
//         connection = DatabaseConnection.getConnection();

//         setTitle("Teacher Dashboard");
//         setSize(700, 500);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         JPanel panel = new JPanel();
//         panel.setLayout(new GridLayout(6, 2, 10, 10));

//         JLabel welcomeLabel = new JLabel("Welcome, Teacher!", JLabel.CENTER);
//         welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
//         add(welcomeLabel, BorderLayout.NORTH);

//         panel.add(new JLabel("Select Department:"));
//         departmentDropdown = new JComboBox<>(getDepartments());
//         panel.add(departmentDropdown);

//         panel.add(new JLabel("Select Section:"));
//         sectionDropdown = new JComboBox<>();
//         panel.add(sectionDropdown);

//         panel.add(new JLabel("Select USN:"));
//         usnDropdown = new JComboBox<>();
//         panel.add(usnDropdown);

//         panel.add(new JLabel("Select Subject:"));
//         subjectDropdown = new JComboBox<>();
//         panel.add(subjectDropdown);

//         updateMarksButton = new JButton("Update Marks");
//         backButton = new JButton("Back");
//         logoutButton = new JButton("Logout");

//         panel.add(updateMarksButton);
//         panel.add(backButton);
//         panel.add(logoutButton);

//         add(panel);

//         setVisible(true);

//         // Listeners
//         departmentDropdown.addActionListener(e -> populateSections());
//         sectionDropdown.addActionListener(e -> populateUSNs());
//         usnDropdown.addActionListener(e -> populateSubjects());

//         updateMarksButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 int selectedSubjectIndex = subjectDropdown.getSelectedIndex();
//                 int selectedUsnIndex = usnDropdown.getSelectedIndex();

//                 if (selectedSubjectIndex < 0 || selectedUsnIndex < 0) {
//                     JOptionPane.showMessageDialog(null, "Please select all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
//                     return;
//                 }

//                 dispose();
//                 new UpdateMarksForm(teacherID).setVisible(true);
//             }
//         });

//         backButton.addActionListener(e -> {
//             dispose();
//             new LoginForm().setVisible(true);
//         });

//         logoutButton.addActionListener(e -> {
//             dispose();
//             new LoginForm().setVisible(true);
//         });
//     }

//     private String[] getDepartments() {
//         ArrayList<String> departments = new ArrayList<>();
//         try {
//             PreparedStatement stmt = connection.prepareStatement("SELECT DepartmentName FROM Departments");
//             ResultSet rs = stmt.executeQuery();
//             while (rs.next()) {
//                 departments.add(rs.getString("DepartmentName"));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//         return departments.toArray(new String[0]);
//     }

//     private void populateSections() {
//         sectionDropdown.removeAllItems();
//         String departmentName = (String) departmentDropdown.getSelectedItem();

//         try {
//             PreparedStatement stmt = connection.prepareStatement(
//                     "SELECT s.SectionName FROM Sections s " +
//                     "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
//                     "WHERE d.DepartmentName = ?");

//             stmt.setString(1, departmentName);
//             ResultSet rs = stmt.executeQuery();

//             while (rs.next()) {
//                 sectionDropdown.addItem(rs.getString("SectionName"));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void populateUSNs() {
//         usnDropdown.removeAllItems();
//         String departmentName = (String) departmentDropdown.getSelectedItem();
//         String sectionName = (String) sectionDropdown.getSelectedItem();

//         try {
//             PreparedStatement stmt = connection.prepareStatement(
//                     "SELECT s.USN FROM Students s " +
//                     "JOIN Sections sec ON s.SectionID = sec.SectionID " +
//                     "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
//                     "WHERE d.DepartmentName = ? AND sec.SectionName = ?");

//             stmt.setString(1, departmentName);
//             stmt.setString(2, sectionName);

//             ResultSet rs = stmt.executeQuery();

//             while (rs.next()) {
//                 usnDropdown.addItem(rs.getString("USN"));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void populateSubjects() {
//         subjectDropdown.removeAllItems();
//         String departmentName = (String) departmentDropdown.getSelectedItem();

//         try {
//             PreparedStatement stmt = connection.prepareStatement(
//                     "SELECT s.SubjectName FROM Subjects s " +
//                     "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
//                     "WHERE d.DepartmentName = ?");

//             stmt.setString(1, departmentName);
//             ResultSet rs = stmt.executeQuery();

//             while (rs.next()) {
//                 subjectDropdown.addItem(rs.getString("SubjectName"));
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }
// }
// til here ...........................




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
import java.util.ArrayList;

public class TeacherDashboard extends JFrame {
    private int teacherID;
    private Connection connection;

    private JComboBox<String> departmentDropdown;
    private JComboBox<String> sectionDropdown;
    private JComboBox<String> usnDropdown;
    private JComboBox<String> subjectDropdown;
    private JButton updateMarksButton, backButton, logoutButton;

    public TeacherDashboard(int teacherID) {
        this.teacherID = teacherID;
        connection = DatabaseConnection.getConnection();

        setTitle("Teacher Dashboard");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 10, 10));

        JLabel welcomeLabel = new JLabel("Welcome, Teacher!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        panel.add(new JLabel("Select Department:"));
        departmentDropdown = new JComboBox<>(getDepartments());
        panel.add(departmentDropdown);

        panel.add(new JLabel("Select Section:"));
        sectionDropdown = new JComboBox<>();
        panel.add(sectionDropdown);

        panel.add(new JLabel("Select USN:"));
        usnDropdown = new JComboBox<>();
        panel.add(usnDropdown);

        panel.add(new JLabel("Select Subject:"));
        subjectDropdown = new JComboBox<>();
        panel.add(subjectDropdown);

        updateMarksButton = new JButton("Update Marks");
        backButton = new JButton("Back");
        logoutButton = new JButton("Logout");

        panel.add(updateMarksButton);
        panel.add(backButton);
        panel.add(logoutButton);

        add(panel);

        setVisible(true);

        // Listeners
        departmentDropdown.addActionListener(e -> populateSections());
        sectionDropdown.addActionListener(e -> populateUSNs());
        usnDropdown.addActionListener(e -> populateSubjects());

        updateMarksButton.addActionListener(e -> {
            int selectedSubjectIndex = subjectDropdown.getSelectedIndex();
            int selectedUsnIndex = usnDropdown.getSelectedIndex();

            if (selectedSubjectIndex < 0 || selectedUsnIndex < 0) {
                JOptionPane.showMessageDialog(null, "Please select all required fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedSubject = (String) subjectDropdown.getSelectedItem();
            String selectedUSN = (String) usnDropdown.getSelectedItem();

            dispose();
            new UpdateMarksForm(teacherID, selectedSubject, selectedUSN).setVisible(true);
        });

        backButton.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });

        logoutButton.addActionListener(e -> {
            dispose();
            new LoginForm().setVisible(true);
        });
    }

    private String[] getDepartments() {
        ArrayList<String> departments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT DepartmentName FROM Departments")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                departments.add(rs.getString("DepartmentName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return departments.toArray(new String[0]);
    }

    private void populateSections() {
        sectionDropdown.removeAllItems();
        String departmentName = (String) departmentDropdown.getSelectedItem();

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.SectionName FROM Sections s " +
                        "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
                        "WHERE d.DepartmentName = ?")) {

            stmt.setString(1, departmentName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                sectionDropdown.addItem(rs.getString("SectionName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void populateUSNs() {
        usnDropdown.removeAllItems();
        String departmentName = (String) departmentDropdown.getSelectedItem();
        String sectionName = (String) sectionDropdown.getSelectedItem();

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.USN FROM Students s " +
                        "JOIN Sections sec ON s.SectionID = sec.SectionID " +
                        "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
                        "WHERE d.DepartmentName = ? AND sec.SectionName = ?")) {

            stmt.setString(1, departmentName);
            stmt.setString(2, sectionName);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                usnDropdown.addItem(rs.getString("USN"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void populateSubjects() {
        subjectDropdown.removeAllItems();
        String departmentName = (String) departmentDropdown.getSelectedItem();

        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT s.SubjectName FROM Subjects s " +
                        "JOIN Departments d ON s.DepartmentID = d.DepartmentID " +
                        "WHERE d.DepartmentName = ?")) {

            stmt.setString(1, departmentName);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                subjectDropdown.addItem(rs.getString("SubjectName"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}