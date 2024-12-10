package org.example.finaloop1;

import java.util.List;

public class Course {
    private int courseId;
    private String courseName;
    private String description;
    private Instructor instructor;  // Changed from instructorId to Instructor object

    // Constructor with Instructor
    public Course(int courseId, String courseName, String description, int instructorId) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        // You might want to add a method in InstructorDAO to fetch by ID
        this.instructor = new InstructorDAO() {
            @Override
            public void delete(Instructor entity) {

            }

            @Override
            public List<Course> findCoursesByInstructorId(int instructorId) {
                return List.of();
            }


        }.read(instructorId);
    }

    // Constructor that takes Instructor object
    public Course(int courseId, String courseName, String description, Instructor instructor) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
        this.instructor = instructor;
    }

    // Getters and setters
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

    public Instructor getInstructor() {
        return instructor;
    }

    public void setInstructor(Instructor instructor) {
        this.instructor = instructor;
    }

    // Convenience method to get instructor ID for database operations
    public int getInstructorId() {
        return instructor != null ? instructor.getInstructorId() : 0;
    }
}