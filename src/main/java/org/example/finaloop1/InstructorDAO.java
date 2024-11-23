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

}
