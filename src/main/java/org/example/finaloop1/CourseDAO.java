package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseDAO implements DAOInterface<Course> {
    private final InstructorDAO instructorDAO;

    public CourseDAO() {
        this.instructorDAO = new InstructorDAO() {
            @Override
            public void delete(Instructor entity) {
                // Implement if needed
            }

            @Override
            public List<Course> findCoursesByInstructorId(int instructorId) {
                return List.of(); // Temporary stub
            }
        };
    }

    @Override
    public int insert(Course course) {
        String query = "INSERT INTO Courses (course_name, description, instructor_id) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            if (course.getInstructor() != null) {
                statement.setInt(3, course.getInstructor().getInstructorId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int courseId = generatedKeys.getInt(1);
                course.setCourseId(courseId);
                return courseId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Course read(int id) {
        String query = "SELECT * FROM Courses WHERE course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Instructor instructor = null;
                int instructorId = resultSet.getInt("instructor_id");
                if (!resultSet.wasNull()) {
                    instructor = instructorDAO.read(instructorId);
                }

                return new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        instructor
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(Course course) {
        String query = "UPDATE Courses SET course_name = ?, description = ?, instructor_id = ? WHERE course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            if (course.getInstructor() != null) {
                statement.setInt(3, course.getInstructor().getInstructorId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            statement.setInt(4, course.getCourseId());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void delete(Course course) {
        delete(course.getCourseId());
    }

    @Override
    public void delete(int courseId) {
        String query = "DELETE FROM Courses WHERE course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Course> findAll() {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM Courses";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Instructor instructor = null;
                int instructorId = resultSet.getInt("instructor_id");
                if (!resultSet.wasNull()) {
                    instructor = instructorDAO.read(instructorId);
                }

                courses.add(new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        instructor
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }

    @Override
    public List<Course> findCoursesByInstructorId(int instructorId) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM Courses WHERE instructor_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, instructorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Instructor instructor = instructorDAO.read(instructorId);

                courses.add(new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        instructor
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
