package org.example.finaloop1;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Instructor {
    private int instructorId;
    private String instructorName;
    private String instructorEmail;
    private String instructorPhone;
    private int totalStudents;
    private int totalCourses;
    private StringProperty instructorNum = new SimpleStringProperty("");

    public Instructor(String instructorName, String instructorEmail, String instructorPhone) {
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
    }
    public Instructor(int instructorId, String instructorName, String instructorEmail, String instructorPhone) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
    }
    public Instructor(int instructorId, String instructorName, String instructorEmail, String instructorPhone, int totalStudents) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.totalStudents = totalStudents;
    }

    public Instructor(int instructorId, String instructorName, String instructorEmail,
                      String instructorPhone, int totalStudents, int totalCourses) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.instructorPhone = instructorPhone;
        this.totalStudents = totalStudents;
        this.totalCourses = totalCourses; // Initialize the new field
    }

    // Getter for the StringProperty to bind to the TableView column
    public StringProperty instructorNumProperty() {
        return instructorNum;
    }

    // Method to update the number displayed
    public void setInstructorNumDisplay(String value) {
        this.instructorNum.set(value);
    }
    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }
    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    public int getTotalCourses() {
        return totalCourses;
    }

    public void setTotalCourses(int totalCourses) {
        this.totalCourses = totalCourses;
    }
}
