// package UI;

// import Database.DatabaseConnection;

// import javax.swing.*;
// import java.awt.*;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;

// public class UpdateMarksForm extends JFrame {

//     private JComboBox<String> studentComboBox;
//     private JTextField mse1Field, mse2Field, taskField, seeField;
//     private JButton updateButton, backButton;
//     private String username;

//     public UpdateMarksForm(String username) {
//         this.username = username;
//         setTitle("Update Marks - EduMark");
//         setSize(400, 400);
//         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//         setLayout(new GridLayout(7, 2, 10, 10));

//         // Labels and Input Fields
//         add(new JLabel("Select Student:"));
//         studentComboBox = new JComboBox<>();
//         add(studentComboBox);

//         add(new JLabel("MSE1 (Out of 20):"));
//         mse1Field = new JTextField();
//         add(mse1Field);

//         add(new JLabel("MSE2 (Out of 20):"));
//         mse2Field = new JTextField();
//         add(mse2Field);

//         add(new JLabel("Task (Out of 10):"));
//         taskField = new JTextField();
//         add(taskField);

//         add(new JLabel("SEE (Out of 100):"));
//         seeField = new JTextField();
//         add(seeField);

//         updateButton = new JButton("Update Marks");
//         backButton = new JButton("Back to Dashboard");

//         add(updateButton);
//         add(backButton);

//         // Load students from the teacher's assigned section
//         loadStudents();

//         // Button Listeners
//         updateButton.addActionListener(e -> updateMarks());
//         backButton.addActionListener(e -> {
//             new TeacherDashboard(username);
//             dispose();
//         });

//         setVisible(true);
//     }

//     // Fetch students in the teacher's assigned section
//     private void loadStudents() {
//         try (Connection conn = DatabaseConnection.getConnection()) {
//             String query = """
//                 SELECT s.USN, s.StudentName
//                 FROM Students s
//                 JOIN Teachers t ON s.SectionID = t.SectionID
//                 JOIN Users u ON u.UserID = t.UserID
//                 WHERE u.Username = ?
//             """;
//             PreparedStatement stmt = conn.prepareStatement(query);
//             stmt.setString(1, username);
//             ResultSet rs = stmt.executeQuery();

//             while (rs.next()) {
//                 String studentData = rs.getString("USN") + " - " + rs.getString("StudentName");
//                 studentComboBox.addItem(studentData);
//             }

//             if (studentComboBox.getItemCount() == 0) {
//                 JOptionPane.showMessageDialog(this, "No students found for your section.");
//             }
//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//         }
//     }

//     // Update student marks using stored procedure
//     private void updateMarks() {
//         try (Connection conn = DatabaseConnection.getConnection()) {
//             String selectedStudent = (String) studentComboBox.getSelectedItem();
//             if (selectedStudent == null) {
//                 JOptionPane.showMessageDialog(this, "No student selected.");
//                 return;
//             }

//             String usn = selectedStudent.split(" - ")[0];
//             int mse1 = Integer.parseInt(mse1Field.getText());
//             int mse2 = Integer.parseInt(mse2Field.getText());
//             int task = Integer.parseInt(taskField.getText());
//             int see = Integer.parseInt(seeField.getText());

//             if (mse1 < 0 || mse1 > 20 || mse2 < 0 || mse2 > 20 || task < 0 || task > 10 || see < 0 || see > 100) {
//                 JOptionPane.showMessageDialog(this, "Invalid marks. Please enter within the valid range.");
//                 return;
//             }

//             // Call stored procedure to insert/update marks
//             String query = "{CALL InsertMarks(?, ?, ?, ?, ?)}";
//             PreparedStatement stmt = conn.prepareCall(query);
//             stmt.setString(1, usn);
//             stmt.setInt(2, mse1);
//             stmt.setInt(3, mse2);
//             stmt.setInt(4, task);
//             stmt.setInt(5, see);

//             stmt.execute();
//             JOptionPane.showMessageDialog(this, "Marks updated successfully!");

//         } catch (NumberFormatException ex) {
//             JOptionPane.showMessageDialog(this, "Please enter valid numeric values.");
//         } catch (Exception ex) {
//             JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
//         }
//     }
// }

// package UI;

// import Database.DatabaseConnection;

// import javax.swing.*;
// import java.awt.*;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.SQLException;

// public class UpdateMarksForm extends JFrame {
//     private int teacherID;
//     private int subjectID;
//     private String usn;
//     private Connection connection;

//     private JTextField mse1Field, mse2Field, taskField, seeField;

//     public UpdateMarksForm(int teacherID, int subjectID, String usn) {
//         this.teacherID = teacherID;
//         this.subjectID = subjectID;
//         this.usn = usn;
//         connection = DatabaseConnection.getConnection();

//         setTitle("Update Marks");
//         setSize(500, 400);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         JPanel panel = new JPanel(new GridLayout(8, 2, 10, 10));

//         panel.add(new JLabel("MSE1:"));
//         mse1Field = new JTextField();
//         panel.add(mse1Field);

//         panel.add(new JLabel("MSE2:"));
//         mse2Field = new JTextField();
//         panel.add(mse2Field);

//         panel.add(new JLabel("Task:"));
//         taskField = new JTextField();
//         panel.add(taskField);

//         panel.add(new JLabel("SEE:"));
//         seeField = new JTextField();
//         panel.add(seeField);

//         JButton updateButton = new JButton("Update Marks");
//         panel.add(updateButton);

//         add(panel);
//         setVisible(true);

//         updateButton.addActionListener(e -> updateMarks());
//     }

//     private void updateMarks() {
//         try {
//             int mse1 = Integer.parseInt(mse1Field.getText());
//             int mse2 = Integer.parseInt(mse2Field.getText());
//             int task = Integer.parseInt(taskField.getText());
//             int see = Integer.parseInt(seeField.getText());

//             PreparedStatement pstmt = connection.prepareStatement("EXEC InsertMarks ?, ?, ?, ?, ?, ?");
//             pstmt.setString(1, usn);
//             pstmt.setInt(2, subjectID);
//             pstmt.setInt(3, mse1);
//             pstmt.setInt(4, mse2);
//             pstmt.setInt(5, task);
//             pstmt.setInt(6, see);

//             pstmt.executeUpdate();
//             JOptionPane.showMessageDialog(this, "Marks Updated Successfully!");
//         } catch (SQLException | NumberFormatException ex) {
//             ex.printStackTrace();
//         }
//     }
// }



//half proper from here...........................................
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

// public class UpdateMarksForm extends JFrame {
//     private JComboBox<String> subjectComboBox;
//     private JTextField usnField, mse1Field, mse2Field, taskField, seeField;
//     private int teacherID;
//     private Connection connection;

//     public UpdateMarksForm(int teacherID) {
//         this.teacherID = teacherID;
//         connection = DatabaseConnection.getConnection();

//         setTitle("Update Marks");
//         setSize(500, 400);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         JPanel panel = new JPanel();
//         panel.setLayout(new GridLayout(8, 2, 10, 10));

//         JLabel subjectLabel = new JLabel("Select Subject:");
//         subjectComboBox = new JComboBox<>();
//         loadSubjects();

//         JLabel usnLabel = new JLabel("Enter USN:");
//         usnField = new JTextField();

//         JLabel mse1Label = new JLabel("MSE1:");
//         mse1Field = new JTextField();

//         JLabel mse2Label = new JLabel("MSE2:");
//         mse2Field = new JTextField();

//         JLabel taskLabel = new JLabel("Task:");
//         taskField = new JTextField();

//         JLabel seeLabel = new JLabel("SEE:");
//         seeField = new JTextField();

//         JButton updateButton = new JButton("Update Marks");
//         updateButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 updateMarks();
//             }
//         });

//         JButton backButton = new JButton("Back");
//         backButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 new TeacherDashboard(teacherID).setVisible(true); // Redirect to Teacher Dashboard
//                 dispose();
//             }
//         });

//         panel.add(subjectLabel);
//         panel.add(subjectComboBox);
//         panel.add(usnLabel);
//         panel.add(usnField);
//         panel.add(mse1Label);
//         panel.add(mse1Field);
//         panel.add(mse2Label);
//         panel.add(mse2Field);
//         panel.add(taskLabel);
//         panel.add(taskField);
//         panel.add(seeLabel);
//         panel.add(seeField);
//         panel.add(updateButton);
//         panel.add(backButton);

//         add(panel);

//         setVisible(true);
//     }

//     private void loadSubjects() {
//         try {
//             String query = "SELECT ts.SubjectID, s.SubjectName " +
//                            "FROM TeacherSubject ts " +
//                            "JOIN Subjects s ON ts.SubjectID = s.SubjectID " +
//                            "WHERE ts.TeacherID = ?";
//             PreparedStatement pstmt = connection.prepareStatement(query);
//             pstmt.setInt(1, teacherID);
//             ResultSet rs = pstmt.executeQuery();

//             while (rs.next()) {
//                 String subjectName = rs.getString("SubjectName");
//                 subjectComboBox.addItem(subjectName);
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         }
//     }

//     private void updateMarks() {
//         try {
//             String usn = usnField.getText();
//             String selectedSubject = (String) subjectComboBox.getSelectedItem();
//             int mse1 = Integer.parseInt(mse1Field.getText());
//             int mse2 = Integer.parseInt(mse2Field.getText());
//             int task = Integer.parseInt(taskField.getText());
//             int see = Integer.parseInt(seeField.getText());

//             String query = "EXEC InsertMarks ?, ?, ?, ?, ?, ?";
//             PreparedStatement pstmt = connection.prepareStatement(query);
//             pstmt.setString(1, usn);
//             pstmt.setString(2, selectedSubject);
//             pstmt.setInt(3, mse1);
//             pstmt.setInt(4, mse2);
//             pstmt.setInt(5, task);
//             pstmt.setInt(6, see);

//             int rowsAffected = pstmt.executeUpdate();
//             if (rowsAffected > 0) {
//                 JOptionPane.showMessageDialog(this, "Marks updated successfully!");
//             } else {
//                 JOptionPane.showMessageDialog(this, "Failed to update marks. Please try again.");
//             }

//         } catch (SQLException e) {
//             e.printStackTrace();
//         } catch (NumberFormatException e) {
//             JOptionPane.showMessageDialog(this, "Please enter valid numbers for marks.");
//         }
//     }
// }
//till here................................

package UI;

import Database.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UpdateMarksForm extends JFrame {
    private JTextField mse1Field, mse2Field, taskField, seeField;
    private int teacherID;
    private String selectedSubject, selectedUSN;
    private Connection connection;

    public UpdateMarksForm(int teacherID, String selectedSubject, String selectedUSN) {
        this.teacherID = teacherID;
        this.selectedSubject = selectedSubject;
        this.selectedUSN = selectedUSN;
        this.connection = DatabaseConnection.getConnection();

        setTitle("Update Marks for Subject: " + selectedSubject + " | USN: " + selectedUSN);
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Panel setup
        JPanel panel = new JPanel(new BorderLayout(10, 10));

        // Header section
        JLabel titleLabel = new JLabel("Update Marks", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Form section
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Marks fields
        formPanel.add(new JLabel("MSE1 Marks (0-20):"));
        mse1Field = new JTextField();
        formPanel.add(mse1Field);

        formPanel.add(new JLabel("MSE2 Marks (0-20):"));
        mse2Field = new JTextField();
        formPanel.add(mse2Field);

        formPanel.add(new JLabel("Task Marks (0-10):"));
        taskField = new JTextField();
        formPanel.add(taskField);

        formPanel.add(new JLabel("SEE Marks (0-100):"));
        seeField = new JTextField();
        formPanel.add(seeField);

        panel.add(formPanel, BorderLayout.CENTER);

        // Button section
        JButton updateButton = new JButton("Update Marks");
        JButton backButton = new JButton("Back");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.add(updateButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);

        // Button functionality
        updateButton.addActionListener(e -> updateMarks());
        backButton.addActionListener(e -> {
            new TeacherDashboard(teacherID).setVisible(true);
            dispose();
        });

        setVisible(true);
    }

    private int fetchSubjectID(String subjectName) throws SQLException {
        String query = "SELECT SubjectID FROM Subjects WHERE SubjectName = ?";
        PreparedStatement pstmt = connection.prepareStatement(query);
        pstmt.setString(1, subjectName);

        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
            return rs.getInt("SubjectID");
        } else {
            throw new SQLException("Invalid Subject Name: " + subjectName);
        }
    }

    private void updateMarks() {
        try {
            // Retrieve marks
            int mse1 = Integer.parseInt(mse1Field.getText());
            int mse2 = Integer.parseInt(mse2Field.getText());
            int task = Integer.parseInt(taskField.getText());
            int see = Integer.parseInt(seeField.getText());

            // Validate marks
            if (mse1 < 0 || mse1 > 20 || mse2 < 0 || mse2 > 20 || task < 0 || task > 10 || see < 0 || see > 100) {
                JOptionPane.showMessageDialog(this, "Marks must be within valid ranges!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Fetch SubjectID from SubjectName
            int subjectID = fetchSubjectID(selectedSubject);

            // Debugging output for parameters
            System.out.println("USN: " + selectedUSN);
            System.out.println("SubjectID: " + subjectID);
            System.out.println("MSE1: " + mse1 + ", MSE2: " + mse2 + ", Task: " + task + ", SEE: " + see);

            String query = "EXEC InsertMarks ?, ?, ?, ?, ?, ?";
            PreparedStatement pstmt = connection.prepareStatement(query);

            // Set parameters correctly
            pstmt.setString(1, selectedUSN);   // USN is nvarchar, pass as String
            pstmt.setInt(2, subjectID);        // SubjectID is int
            pstmt.setInt(3, mse1);
            pstmt.setInt(4, mse2);
            pstmt.setInt(5, task);
            pstmt.setInt(6, see);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Marks updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update marks. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numeric values for marks.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}