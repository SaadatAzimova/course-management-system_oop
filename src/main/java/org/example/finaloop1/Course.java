package org.example.finaloop1;

public class Course {
    private int courseId;           // Maps to course_id
    private String courseName;      // Maps to course_name
    private String description;     // Maps to description
    private int instructorId;       // Maps to instructor_id

    // Constructor
    public Course(int courseId, String courseName, String description, int instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.instructorId = instructorId;
    }
    public Course( String courseName, String description, int instructorId) {
        this.courseName = courseName;
        this.description = description;
        this.instructorId = instructorId;
    }

    public Course() {}

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    // toString Method
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                ", description='" + description + '\'' +
                ", instructorId=" + instructorId +
                '}';
    }
}
