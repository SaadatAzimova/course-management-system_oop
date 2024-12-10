package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class InstructorDAO implements DAOInterface<Instructor> {
    private static final Logger logger = Logger.getLogger(InstructorDAO.class.getName());

    @Override
    public int insert(Instructor instructor) {
        String query = "INSERT INTO Instructors (instructor_name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, instructor.getInstructorName());
            pstmt.setString(2, instructor.getInstructorEmail());
            pstmt.setString(3, instructor.getInstructorPhone());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys != null && keys.next()) {
                    return keys.getInt(1); // Return generated ID
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting instructor: " + instructor, e);
        }
        return -1;
    }

    @Override
    public Instructor read(int id) {
        String query = "SELECT * FROM Instructors WHERE instructor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Instructor(
                            rs.getInt("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reading instructor with ID: " + id, e);
        }
        return null;
    }

    @Override
    public boolean update(Instructor instructor) {
        String query = "UPDATE Instructors SET instructor_name = ?, email = ?, phone = ? WHERE instructor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, instructor.getInstructorName());
            pstmt.setString(2, instructor.getInstructorEmail());
            pstmt.setString(3, instructor.getInstructorPhone());
            pstmt.setInt(4, instructor.getInstructorId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating instructor: " + instructor, e);
        }
        return false;
    }

    @Override
    public void delete(int instructorId) {
        String query = "DELETE FROM Instructors WHERE instructor_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, instructorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting instructor: " + instructorId, e);
        }
    }

    @Override
    public List<Instructor> findAll() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT * FROM Instructors";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                instructors.add(new Instructor(
                        rs.getInt("instructor_id"),
                        rs.getString("instructor_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all instructors", e);
        }
        return instructors;
    }

    public List<Instructor> findInstructorsWithTotalStudents() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT i.instructor_id, i.instructor_name, i.email, i.phone, COUNT(DISTINCT e.student_id) AS total_students " +
                "FROM Instructors i " +
                "JOIN Courses c ON i.instructor_id = c.instructor_id " +
                "LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                "GROUP BY i.instructor_id, i.instructor_name " +
                "ORDER BY total_students DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int instructorId = rs.getInt("instructor_id");
                String instructorName = rs.getString("instructor_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int totalStudents = rs.getInt("total_students");

                // Use the constructor that includes totalStudents
                Instructor instructor = new Instructor(instructorId, instructorName, email, phone, totalStudents);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching instructors with student counts", e);
        }
        return instructors;
    }
    public List<Instructor> findTop3InstructorsWithTotalStudents() {
        List<Instructor> instructors = new ArrayList<>();
        // Modified query to get the top 3 instructors based on the number of distinct students
        String query = "SELECT i.instructor_id, i.instructor_name, i.email, i.phone, " +
                "COUNT(DISTINCT e.student_id) AS total_students " +
                "FROM Instructors i " +
                "JOIN Courses c ON i.instructor_id = c.instructor_id " +
                "LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                "GROUP BY i.instructor_id, i.instructor_name " +
                "ORDER BY total_students DESC " +
                "LIMIT 3"; // Limits the result to the top 3 instructors

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int instructorId = rs.getInt("instructor_id");
                String instructorName = rs.getString("instructor_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int totalStudents = rs.getInt("total_students");

                // Use the constructor that includes totalStudents
                Instructor instructor = new Instructor(instructorId, instructorName, email, phone, totalStudents);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching top 3 instructors with student counts", e);
        }
        return instructors;
    }
    public List<Instructor> findInstructorsWithTotalCourses() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT i.instructor_id, i.instructor_name, i.email, i.phone, " +
                "COUNT(c.course_id) AS total_courses " +
                "FROM Instructors i " +
                "JOIN Courses c ON i.instructor_id = c.instructor_id " +
                "GROUP BY i.instructor_id, i.instructor_name " +
                "ORDER BY total_courses DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int instructorId = rs.getInt("instructor_id");
                String instructorName = rs.getString("instructor_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int totalCourses = rs.getInt("total_courses");

                // Create Instructor object and set the total number of courses
                Instructor instructor = new Instructor(instructorId, instructorName, email, phone, 0, totalCourses);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching instructors with course counts", e);
        }
        return instructors;
    }
    public List<Instructor> findInstructorsWithZeroCourses() {
        List<Instructor> instructors = new ArrayList<>();
        String query = "SELECT i.instructor_id, i.instructor_name, i.email, i.phone, " +
                "COUNT(c.course_id) AS total_courses " +
                "FROM Instructors i " +
                "LEFT JOIN Courses c ON i.instructor_id = c.instructor_id " +
                "GROUP BY i.instructor_id, i.instructor_name " +
                "HAVING COUNT(c.course_id) = 0"; // Filter for instructors with no courses

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int instructorId = rs.getInt("instructor_id");
                String instructorName = rs.getString("instructor_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                int totalCourses = rs.getInt("total_courses");

                // Create Instructor object with 0 courses
                Instructor instructor = new Instructor(instructorId, instructorName, email, phone, 0, totalCourses);
                instructors.add(instructor);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching instructors with zero courses", e);
        }
        return instructors;
    }





}
