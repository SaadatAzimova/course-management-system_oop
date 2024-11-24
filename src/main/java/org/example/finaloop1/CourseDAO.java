package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class CourseDAO implements DAOInterface<Course>  {

    @Override
    public int insert(Course course) {
        String query = "INSERT INTO course (course_name, description, instructor_id) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getInstructorId());
            statement.executeUpdate();

            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Return the generated course ID
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if insertion fails
    }

    @Override
    public Course read(int id) {
        String query = "SELECT * FROM course WHERE course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        resultSet.getInt("instructor_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no course is found
    }

    @Override
    public void update(Course course) {
        String query = "UPDATE course SET course_name = ?, description = ?, instructor_id = ? WHERE course_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, course.getCourseName());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getInstructorId());
            statement.setInt(4, course.getCourseId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Course course) {
        delete(course.getCourseId());
    }

    @Override
    public void delete(int courseId) {
        String query = "DELETE FROM course WHERE course_id = ?";
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
        String query = "SELECT * FROM course";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        resultSet.getInt("instructor_id")
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
        String query = "SELECT * FROM course WHERE instructor_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, instructorId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                courses.add(new Course(
                        resultSet.getInt("course_id"),
                        resultSet.getString("course_name"),
                        resultSet.getString("description"),
                        resultSet.getInt("instructor_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
}
