package org.example.finaloop1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class EnrollmentDAO implements DAOInterface<Enrollment> {
    private static final Logger logger = Logger.getLogger(EnrollmentDAO.class.getName());
    private Connection connection;

    public EnrollmentDAO() {
        try {
            this.connection = DBConnection.getConnection();
        } catch (SQLException e) {
            logger.severe("Failed to establish database connection: " + e.getMessage());
            e.printStackTrace();
        }
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



    public List<Enrollment> findFilteredEnrollments(Student student, Course course, String semester, Integer year) {
        List<Enrollment> filteredEnrollments = new ArrayList<>();

        // Start with a base query
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT e.enrollment_id, e.student_id, e.course_id, e.year, e.semester, e.grade, " +
                        "s.student_name, s.email AS student_email, s.phone AS student_phone, " +
                        "c.course_name, c.description AS course_description, " +
                        "i.instructor_id, i.instructor_name, i.instructor_email, i.instructor_phone " +
                        "FROM Enrollments e " +
                        "JOIN Students s ON e.student_id = s.student_id " +
                        "JOIN Courses c ON e.course_id = c.course_id " +
                        "JOIN Instructors i ON c.instructor_id = i.instructor_id " +
                        "WHERE 1=1"
        );

        // List to store parameters for prepared statement
        List<Object> params = new ArrayList<>();

        // Add filters based on non-null parameters
        if (student != null) {
            queryBuilder.append(" AND e.student_id = ?");
            params.add(student.getStudentId());
        }

        if (course != null) {
            queryBuilder.append(" AND e.course_id = ?");
            params.add(course.getCourseId());
        }

        if (semester != null && !semester.isEmpty()) {
            queryBuilder.append(" AND e.semester = ?");
            params.add(semester);
        }

        if (year != null) {
            queryBuilder.append(" AND e.year = ?");
            params.add(year);
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create Instructor
                    Instructor instructor = new Instructor(
                            rs.getInt("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("instructor_email"),
                            rs.getString("instructor_phone")
                    );

                    // Create Course
                    Course enrolledCourse = new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getString("course_description"),
                            instructor
                    );

                    // Create Student
                    Student enrolledStudent = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("student_email"),
                            rs.getString("student_phone")
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

                    filteredEnrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception - you might want to throw a custom DAO exception
        }

        return filteredEnrollments;
    }
    public List<Enrollment> findFilteredEnrollments(Student student, Course course, Optional<String> semester, Optional<Integer> year) {
        List<Enrollment> filteredEnrollments = new ArrayList<>();

        // Start with a base query
        StringBuilder queryBuilder = new StringBuilder(
                "SELECT e.enrollment_id, e.student_id, e.course_id, e.year, e.semester, e.grade, " +
                        "s.student_name, s.email AS student_email, s.phone AS student_phone, " +
                        "c.course_name, c.description AS course_description, " +
                        "i.instructor_id, i.instructor_name, i.email AS instructor_email, i.phone AS instructor_phone " +
                        "FROM Enrollments e " +
                        "JOIN Students s ON e.student_id = s.student_id " +
                        "JOIN Courses c ON e.course_id = c.course_id " +
                        "JOIN Instructors i ON c.instructor_id = i.instructor_id " +
                        "WHERE 1=1"
        );



        // List to store parameters for prepared statement
        List<Object> params = new ArrayList<>();

        // Add filters based on non-empty optional parameters
        if (student != null) {
            queryBuilder.append(" AND e.student_id = ?");
            params.add(student.getStudentId());
        }

        if (course != null) {
            queryBuilder.append(" AND e.course_id = ?");
            params.add(course.getCourseId());
        }

        if (semester.isPresent() && !semester.get().isEmpty()) {
            queryBuilder.append(" AND e.semester = ?");
            params.add(semester.get());
        }

        if (year.isPresent() && year.get() > 0) {
            queryBuilder.append(" AND e.year = ?");
            params.add(year.get());
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

            // Set parameters
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }

            // Execute query
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create Instructor
                    Instructor instructor = new Instructor(
                            rs.getInt("instructor_id"),
                            rs.getString("instructor_name"),
                            rs.getString("instructor_email"),
                            rs.getString("instructor_phone")
                    );

                    // Create Course
                    Course enrolledCourse = new Course(
                            rs.getInt("course_id"),
                            rs.getString("course_name"),
                            rs.getString("course_description"),
                            instructor
                    );

                    // Create Student
                    Student enrolledStudent = new Student(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("student_email"),
                            rs.getString("student_phone")
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

                    filteredEnrollments.add(enrollment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception - you might want to throw a custom DAO exception
        }

        return filteredEnrollments;
    }
    public List<Student> findAllDistinctStudents() {
        List<Student> distinctStudents = new ArrayList<>();
        String sql = "SELECT DISTINCT s.student_id, s.student_name, s.email, s.phone " +
                "FROM Enrollments e " +
                "JOIN Students s ON e.student_id = s.student_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
                distinctStudents.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctStudents;
    }

    // Method to get all distinct courses from enrollments
    public List<Course> findAllDistinctCourses() {
        List<Course> distinctCourses = new ArrayList<>();
        String sql = "SELECT DISTINCT c.course_id, c.course_name, c.description, " +
                "i.instructor_id, i.instructor_name " +
                "FROM Enrollments e " +
                "JOIN Courses c ON e.course_id = c.course_id " +
                "JOIN Instructors i ON c.instructor_id = i.instructor_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Instructor instructor = new Instructor(
                        rs.getInt("instructor_id"),
                        rs.getString("instructor_name"),
                        null, // Email not fetched in this query
                        null  // Phone not fetched in this query
                );

                Course course = new Course(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("description"),
                        instructor
                );
                distinctCourses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctCourses;
    }

    // Method to get all distinct semesters from enrollments
    public List<String> findAllDistinctSemesters() {
        List<String> distinctSemesters = new ArrayList<>();
        String sql = "SELECT DISTINCT semester FROM Enrollments ORDER BY semester";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                distinctSemesters.add(rs.getString("semester"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctSemesters;
    }

    // Method to get all distinct years from enrollments
    public List<Integer> findAllDistinctYears() {
        List<Integer> distinctYears = new ArrayList<>();
        String sql = "SELECT DISTINCT year FROM Enrollments ORDER BY year";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                distinctYears.add(rs.getInt("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return distinctYears;
    }
}