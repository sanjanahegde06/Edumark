package Models;

public class Student extends User {
    private String usn;
    private String studentName;
    private int departmentID;
    private int sectionID;

    public Student(int userID, String username, String password, String usn, String studentName, int departmentID, int sectionID) {
        super(userID, username, password, "Student");
        this.usn = usn;
        this.studentName = studentName;
        this.departmentID = departmentID;
        this.sectionID = sectionID;
    }

    // Getters
    public String getUSN() {
        return usn;
    }

    public String getStudentName() {
        return studentName;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public int getSectionID() {
        return sectionID;
    }

    // Setters
    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    @Override
    public String toString() {
        return "Student Name: " + studentName + ", USN: " + usn + ", Department ID: " + departmentID + ", Section ID: " + sectionID;
    }
}
