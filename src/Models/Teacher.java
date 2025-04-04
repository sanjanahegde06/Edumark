package Models;

public class Teacher extends User {
    private String teacherName;
    private int departmentID;
    private int sectionID;

    public Teacher(int userID, String username, String password, String teacherName, int departmentID, int sectionID) {
        super(userID, username, password, "Teacher");
        this.teacherName = teacherName;
        this.departmentID = departmentID;
        this.sectionID = sectionID;
    }

    // Getters
    public String getTeacherName() {
        return teacherName;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public int getSectionID() {
        return sectionID;
    }

    // Setters
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public void setSectionID(int sectionID) {
        this.sectionID = sectionID;
    }

    @Override
    public String toString() {
        return "Teacher Name: " + teacherName + ", Department ID: " + departmentID + ", Section ID: " + sectionID;
    }
}
