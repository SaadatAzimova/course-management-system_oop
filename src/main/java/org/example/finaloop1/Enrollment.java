package org.example.finaloop1;

public class Enrollment {
    private int enrollmentId;
    private Student student;
    private Course course;
    private int year;
    private String semester;
    private String grade;

    // Full constructor
    public Enrollment(int enrollmentId, Student student, Course course, int year, String semester, String grade) {
        this.enrollmentId = enrollmentId;
        this.student = student;
        this.course = course;
        this.year = year;
        this.semester = semester;
        this.grade = grade;
    }

    // Constructor without enrollmentId (for new enrollments)
    public Enrollment(Student student, Course course) {
        this.student = student;
        this.course = course;
        this.year = java.time.Year.now().getValue(); // Get current year
        this.semester = determineSemester();
    }

    // Helper method to determine semester based on current month
    private String determineSemester() {
        int month = java.time.LocalDate.now().getMonthValue();
        if (month >= 1 && month <= 5) {
            return "Spring";
        } else if (month >= 6 && month <= 8) {
            return "Summer";
        } else {
            return "Fall";
        }
    }

    // Getters and Setters
    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    // Convenience methods for database operations
    public int getStudentId() {
        return student != null ? student.getStudentId() : 0;
    }

    public int getCourseId() {
        return course != null ? course.getCourseId() : 0;
    }

    @Override
    public String toString() {
        return "Enrollment{" +
                "enrollmentId=" + enrollmentId +
                ", student=" + (student != null ? student.getStudentName() : "N/A") +
                ", course=" + (course != null ? course.getCourseName() : "N/A") +
                ", year=" + year +
                ", semester='" + semester + '\'' +
                ", grade='" + grade + '\'' +
                '}';
    }
}