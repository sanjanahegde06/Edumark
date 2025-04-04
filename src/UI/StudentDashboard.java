// package UI;

// import Database.DatabaseConnection;
// import javax.swing.*;
// import java.awt.*;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// public class StudentDashboard extends JFrame {

//     private JLabel nameLabel, usnLabel, departmentLabel, sectionLabel;
//     private JLabel mse1Label, mse2Label, taskLabel, seeLabel, cieLabel, see50Label, totalLabel, gradeLabel;
//     private JButton logoutButton;
//     private String username;

//     public StudentDashboard(String username) {
//         this.username = username;

//         setTitle("Student Dashboard - EduMark");
//         setSize(500, 500);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new GridLayout(12, 2, 10, 10));

//         nameLabel = new JLabel();
//         usnLabel = new JLabel();
//         departmentLabel = new JLabel();
//         sectionLabel = new JLabel();

//         mse1Label = new JLabel();
//         mse2Label = new JLabel();
//         taskLabel = new JLabel();
//         seeLabel = new JLabel();
//         cieLabel = new JLabel();
//         see50Label = new JLabel();
//         totalLabel = new JLabel();
//         gradeLabel = new JLabel();

//         add(new JLabel("Name:")); add(nameLabel);
//         add(new JLabel("USN:")); add(usnLabel);
//         add(new JLabel("Department:")); add(departmentLabel);
//         add(new JLabel("Section:")); add(sectionLabel);

//         add(new JLabel("MSE1:")); add(mse1Label);
//         add(new JLabel("MSE2:")); add(mse2Label);
//         add(new JLabel("Task:")); add(taskLabel);
//         add(new JLabel("SEE (Out of 100):")); add(seeLabel);
//         add(new JLabel("CIE (Out of 50):")); add(cieLabel);
//         add(new JLabel("SEE (Reduced to 50):")); add(see50Label);
//         add(new JLabel("Total (Out of 100):")); add(totalLabel);
//         add(new JLabel("Grade:")); add(gradeLabel);

//         logoutButton = new JButton("Logout");
//         add(logoutButton);

//         logoutButton.addActionListener(e -> {
//             new InitialPage();
//             dispose();
//         });

//         loadStudentData();
//         setVisible(true);
//     }

//     private void loadStudentData() {
//         try (Connection conn = DatabaseConnection.getConnection()) {
//             // Fetch student and department details
//             String studentQuery = """
//                 SELECT s.StudentName, s.USN, d.DepartmentName, sec.SectionName
//                 FROM Students s
//                 JOIN Departments d ON s.DepartmentID = d.DepartmentID
//                 JOIN Sections sec ON s.SectionID = sec.SectionID
//                 JOIN Users u ON u.UserID = s.UserID
//                 WHERE u.Username = ?
//             """;

//             PreparedStatement studentStmt = conn.prepareStatement(studentQuery);
//             studentStmt.setString(1, username);
//             ResultSet studentRs = studentStmt.executeQuery();

//             if (studentRs.next()) {
//                 nameLabel.setText(studentRs.getString("StudentName"));
//                 usnLabel.setText(studentRs.getString("USN"));
//                 departmentLabel.setText(studentRs.getString("DepartmentName"));
//                 sectionLabel.setText(studentRs.getString("SectionName"));
//             }

//             // Fetch marks details
//             String marksQuery = """
//                 SELECT MSE1, MSE2, Task, SEE, CIE, SEE_50, Total, Grade
//                 FROM Marks m
//                 JOIN Students s ON m.StudentID = s.StudentID
//                 JOIN Users u ON u.UserID = s.UserID
//                 WHERE u.Username = ?
//             """;

//             PreparedStatement marksStmt = conn.prepareStatement(marksQuery);
//             marksStmt.setString(1, username);
//             ResultSet marksRs = marksStmt.executeQuery();

//             if (marksRs.next()) {
//                 mse1Label.setText(String.valueOf(marksRs.getInt("MSE1")));
//                 mse2Label.setText(String.valueOf(marksRs.getInt("MSE2")));
//                 taskLabel.setText(String.valueOf(marksRs.getInt("Task")));
//                 seeLabel.setText(String.valueOf(marksRs.getInt("SEE")));
//                 cieLabel.setText(String.valueOf(marksRs.getInt("CIE")));
//                 see50Label.setText(String.valueOf(marksRs.getInt("SEE_50")));
//                 totalLabel.setText(String.valueOf(marksRs.getInt("Total")));
//                 gradeLabel.setText(marksRs.getString("Grade"));
//             } else {
//                 JOptionPane.showMessageDialog(this, "No marks found.", "Info", JOptionPane.INFORMATION_MESSAGE);
//             }
//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
//         }
//     }
// }
package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import Database.DatabaseConnection;

public class StudentDashboard extends JFrame {

    private String studentUsername;
    private JLabel welcomeLabel;
    private JTextArea marksTextArea;
    private JButton logoutButton;

    public StudentDashboard(String studentUsername) {
        this.studentUsername = studentUsername;

        setTitle("Student Dashboard");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout());
        welcomeLabel = new JLabel("Welcome, " + studentUsername);
        topPanel.add(welcomeLabel);

        marksTextArea = new JTextArea(20, 50);
        marksTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(marksTextArea);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        logoutButton = new JButton("Logout");

        buttonPanel.add(logoutButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadMarks();

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                InitialPage initialPage = new InitialPage();
                initialPage.setVisible(true);
            }
        });
    }

    private void loadMarks() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT s.SubjectName, m.MSE1, m.MSE2, m.Task, m.SEE, m.CIE, m.SEE_50, m.Total, m.Grade " +
                           "FROM Marks m " +
                           "JOIN Subjects s ON m.SubjectID = s.SubjectID " +
                           "JOIN Students st ON m.USN = st.USN " +
                           "JOIN Users u ON st.UserID = u.UserID " +
                           "WHERE u.Username = ?";
            
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setString(1, studentUsername);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder marksList = new StringBuilder();
            marksList.append("Subject\tMSE1\tMSE2\tTask\tSEE\tCIE\tSEE_50\tTotal\tGrade\n");
            marksList.append("---------------------------------------------------------------------\n");

            while (rs.next()) {
                String subjectName = rs.getString("SubjectName");
                int mse1 = rs.getInt("MSE1");
                int mse2 = rs.getInt("MSE2");
                int task = rs.getInt("Task");
                int see = rs.getInt("SEE");
                int cie = rs.getInt("CIE");
                int see_50 = rs.getInt("SEE_50");
                int total = rs.getInt("Total");
                String grade = rs.getString("Grade");

                marksList.append(String.format("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%s\n",
                        subjectName, mse1, mse2, task, see, cie, see_50, total, grade));
            }

            if (marksList.length() == 0) {
                marksTextArea.setText("No marks found for this student.");
            } else {
                marksTextArea.setText(marksList.toString());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentDashboard studentDashboard = new StudentDashboard("student1"); // Test with valid username
            studentDashboard.setVisible(true);
        });
    }
}
