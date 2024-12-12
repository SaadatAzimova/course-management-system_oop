package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class CourseDAO implements DAOInterface<Course> {
    // We'll need an InstructorDAO to fetch Instructor objects
    private InstructorDAO instructorDAO;

    public CourseDAO() {
        this.instructorDAO = new InstructorDAO() {
            @Override
            public void delete(Instructor entity) {

            }

            @Override
            public List<Course> findCoursesByInstructorId(int instructorId) {
                return List.of();
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
            // Use getInstructorId() method to get the instructor ID
            statement.setInt(3, course.getInstructor().getInstructorId());
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
                // Fetch the associated instructor
                Instructor instructor = instructorDAO.read(resultSet.getInt("instructor_id"));

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
            // Use getInstructorId() method to get the instructor ID
            statement.setInt(3, course.getInstructor().getInstructorId());
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
                // Fetch the associated instructor
                Instructor instructor = instructorDAO.read(resultSet.getInt("instructor_id"));

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
    public Map<String, Integer> getStudentCountByCourse() {
        Map<String, Integer> courseStudentCounts = new HashMap<>();
        String query = "SELECT c.course_id, c.course_name, c.description, c.instructor_id, COUNT(e.student_id) AS total_students " +
                "FROM Courses c LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                "GROUP BY c.course_id, c.course_name ORDER BY total_students DESC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                courseStudentCounts.put(resultSet.getString("course_name"), resultSet.getInt("total_students"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courseStudentCounts;
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
                // Fetch the associated instructor
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
    public List<Course> findCoursesWithFewStudents(int maxStudents) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT c.course_id, c.course_name, c.description, c.instructor_id, " +
                "COALESCE(COUNT(DISTINCT e.student_id), 0) AS total_students " +
                "FROM Courses c " +
                "LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                "GROUP BY c.course_id, c.course_name, c.description, c.instructor_id " +
                "HAVING COALESCE(COUNT(DISTINCT e.student_id), 0) < ? " +
                "ORDER BY total_students ASC";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, maxStudents);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int courseId = resultSet.getInt("course_id");
                String courseName = resultSet.getString("course_name");
                String description = resultSet.getString("description");
                int instructorId = resultSet.getInt("instructor_id");
                int totalStudents = resultSet.getInt("total_students");


                // Fetch the full instructor details
                Instructor instructor = instructorDAO.read(instructorId);

                // If instructor is not found, create a minimal instructor object
                if (instructor == null) {
                    instructor = new Instructor(instructorId, "Unknown", "", "", 0);
                }

                // Creating a course object with the number of students
                Course course = new Course(courseId, courseName, description, instructor, totalStudents);

                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return courses;
    }
    public int getAverageStudentsPerCourse() {
        String query = "SELECT ROUND(AVG(student_count), 0) AS avg_students_per_course " +
                "FROM (" +
                "    SELECT c.course_id, COUNT(e.student_id) AS student_count " +
                "    FROM Courses c " +
                "    LEFT JOIN Enrollments e ON c.course_id = e.course_id " +
                "    GROUP BY c.course_id" +
                ") AS student_counts";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                // Return the rounded integer value
                return resultSet.getInt("avg_students_per_course");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if an error occurs or no result is found
    }


}