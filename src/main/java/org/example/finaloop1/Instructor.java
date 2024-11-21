package org.example.finaloop1;

public class Instructor {
    private int instructorId;
    private String instructorName;
    private String instructorEmail;
    private String InstructorPhone;

    public Instructor(int instructorId, String instructorName, String instructorEmail, String instructirPhone) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        InstructorPhone = instructirPhone;
    }

    public Instructor(String instructorName, String instructorEmail, String instructirPhone) {
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        InstructorPhone = instructirPhone;
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
        return InstructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        InstructorPhone = instructorPhone;
    }
}
