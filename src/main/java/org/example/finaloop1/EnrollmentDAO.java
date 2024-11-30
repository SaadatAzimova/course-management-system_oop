package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO implements DAOInterface<Enrollment> {
    private Connection connection;

    public EnrollmentDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public int insert(Enrollment enrollment) {
        String sql = "INSERT INTO Enrollments (student_id, course_id, year, semester, grade) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setInt(3, enrollment.getYear());
            pstmt.setString(4, enrollment.getSemester());
            pstmt.setString(5, enrollment.getGrade());

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating enrollment failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enrollment.setEnrollmentId(generatedKeys.getInt(1));
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Creating enrollment failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public Enrollment read(int enrollmentId) {
        String sql = "SELECT e.enrollment_id, e.student_id, e.course_id, e.year, e.semester, e.grade, " +
                "s.student_name, c.course_name, c.description, " +
                "i.instructor_id, i.instructor_name " +
                "FROM Enrollments e " +
                "JOIN Students s ON e.student_id = s.student_id " +
                "JOIN Courses c ON e.course_id = c.course_id " +
                "JOIN Instructors i ON c.instructor_id = i.instructor_id " +
                "WHERE e.enrollment_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Create Student
                    Student student = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name")
                    );

                    // Create Instructor
                    Instructor instructor = new Instructor(
                            rs.getInt("instructor_id"),
                            rs.getString("instructor_name"),
                            null, // Email not fetched in this query
                            null  // Phone not fetched in this query
                    );

                    // Create Course
                    Course course = new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getString("description"),
                            instructor
                    );

                    // Create Enrollment
                    return new Enrollment(
                            rs.getInt("enrollment_id"),
                            student,
                            course,
                            rs.getInt("year"),
                            rs.getString("semester"),
                            rs.getString("grade")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Enrollment enrollment) {
        String sql = "UPDATE Enrollments SET student_id = ?, course_id = ?, year = ?, semester = ?, grade = ? WHERE enrollment_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollment.getStudentId());
            pstmt.setInt(2, enrollment.getCourseId());
            pstmt.setInt(3, enrollment.getYear());
            pstmt.setString(4, enrollment.getSemester());
            pstmt.setString(5, enrollment.getGrade());
            pstmt.setInt(6, enrollment.getEnrollmentId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void delete(Enrollment enrollment) {
        delete(enrollment.getEnrollmentId());
    }

    @Override
    public void delete(int enrollmentId) {
        String sql = "DELETE FROM Enrollments WHERE enrollment_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, enrollmentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Enrollment> findAll() {
        return findEnrollments(null, null, 0, null);
    }

    // Method to filter enrollments with multiple parameters
    public List<Enrollment> findEnrollments(Student student, Course course, int year, String semester) {
        List<Enrollment> enrollments = new ArrayList<>();

        StringBuilder sqlBuilder = new StringBuilder(
                "SELECT e.enrollment_id, e.student_id, e.course_id, e.year, e.semester, e.grade, " +
                        "s.student_name, c.course_name, c.description, " +
                        "i.instructor_id, i.instructor_name " +
                        "FROM Enrollments e " +
                        "JOIN Students s ON e.student_id = s.student_id " +
                        "JOIN Courses c ON e.course_id = c.course_id " +
                        "JOIN Instructors i ON c.instructor_id = i.instructor_id " +
                        "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        // Add filters based on provided parameters
        if (student != null) {
            sqlBuilder.append("AND e.student_id = ? ");
            params.add(student.getStudentId());
        }

        if (course != null) {
            sqlBuilder.append("AND e.course_id = ? ");
            params.add(course.getCourseId());
        }

        if (year > 0) {
            sqlBuilder.append("AND e.year = ? ");
            params.add(year);
        }

        if (semester != null && !semester.isEmpty()) {
            sqlBuilder.append("AND e.semester = ? ");
            params.add(semester);
        }

        try (PreparedStatement pstmt = connection.prepareStatement(sqlBuilder.toString())) {
            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create Student
                    Student enrolledStudent = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name")
                    );

                    // Create Instructor
                    Instructor instructor = new Instructor(
                            rs.getInt("instructor_id"),
                            rs.getString("instructor_name"),
                            null, // Email not fetched in this query
                            null  // Phone not fetched in this query
                    );

                    // Create Course
                    Course enrolledCourse = new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getString("description"),
                            instructor
                    );

                    // Create Enrollment
                    Enrollment enrollment = new Enrollment(
                            rs.getInt("enrollment_id"),
                            enrolledStudent,
                            enrolledCourse,
                            rs.getInt("year"),
                            rs.getString("semester"),
                            rs.getString("grade")
                    );

                    enrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return enrollments;
    }

    @Override
    public List<Course> findCoursesByInstructorId(int instructorId) {
        // This method is part of the interface but not directly related to Enrollment
        // You might want to implement this in a CourseDAO
        return new ArrayList<>();
    }
}