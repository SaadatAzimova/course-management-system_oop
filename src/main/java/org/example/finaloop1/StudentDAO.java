package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class StudentDAO implements DAOInterface<Student> {
    private static final Logger logger = Logger.getLogger(StudentDAO.class.getName());

    @Override
    public int insert(Student student) {
        String query = "INSERT INTO Students (student_name, email, phone) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, student.getStudentName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPhone());
            pstmt.executeUpdate();

            try (ResultSet keys = pstmt.getGeneratedKeys()) {
                if (keys != null && keys.next()) {
                    return keys.getInt(1); // Return generated ID
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting student: " + student, e);
        }
        return -1;
    }

    @Override
    public Student read(int id) {
        String query = "SELECT * FROM Students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("email"),
                            rs.getString("phone")
                    );
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error reading student with ID: " + id, e);
        }
        return null;
    }

    @Override
    public boolean update(Student student) {
        String query = "UPDATE Students SET student_name = ?, email = ?, phone = ? WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, student.getStudentName());
            pstmt.setString(2, student.getEmail());
            pstmt.setString(3, student.getPhone());
            pstmt.setInt(4, student.getStudentId());

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating student: " + student, e);
            return false;
        }
        return false;
    }


    @Override
    public void delete(int student) {
        String query = "DELETE FROM Students WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, student);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting student: " + student, e);
        }
    }

    @Override
    public List<Student> findAll() {
        List<Student> students = new ArrayList<>();
        String query = "SELECT * FROM Students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                students.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all students", e);
        }
        return students;
    }
}
