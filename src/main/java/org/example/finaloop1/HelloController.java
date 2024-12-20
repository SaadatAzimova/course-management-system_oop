package org.example.finaloop1;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.util.StringConverter;

import java.awt.event.ActionEvent;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class HelloController {

    @FXML private Tab courseTab;
    @FXML private Button goCourseTab;
    @FXML private Tab enrollmentTab;
    @FXML private Button goEnrollmentTab;
    @FXML private Tab instructorTab;
    @FXML private Button goInstructorTab;
    @FXML private Tab mainTab;
    @FXML private Button goMainTab;
    @FXML private Tab studentTab;
    @FXML private Button goStudentTab;

    @FXML private TextField studentEmail;
    @FXML private TextField studentName;
    @FXML private TextField studentPhone;
    @FXML private TableView<Student> studentTableView;
    @FXML private TableColumn<Student, Integer> studentIdColumn;
    @FXML private TableColumn<Student, String> studentNameColumn;
    @FXML private TableColumn<Student, String> studentEmailColumn;
    @FXML private TableColumn<Student, String> studentPhoneColumn;
    @FXML private Button studentAdd;
    @FXML private Button studentRemove;
    @FXML private Button studentUpdate;
    @FXML private Label studentActionMessage;
    @FXML private Button studentShow;
    @FXML private Button studentShowNoEnroll;

    @FXML private Label instructorActionMessage;
    @FXML private Button instructorAdd;
    @FXML private TextField instructorEmail;
    @FXML private TableColumn<Instructor, Integer> instructorIdColumn;
    @FXML private TableColumn<Instructor, String> instructorNameColumn;
    @FXML private TableColumn<Instructor, String> instructorEmailColumn;
    @FXML private TableColumn<Instructor, String> instructorPhoneColumn;
    @FXML private TextField instructorName;
    @FXML private TextField instructorPhone;
    @FXML private Button instructorRemove;
    @FXML private Button instructorUpdate;
    @FXML private TableView<Instructor> instructorTable;
    @FXML private TableColumn<Instructor, String> instructorNumColumn;
    @FXML private Button instructorNumOfCourses;
    @FXML private Button instructorNumOfStuds;
    @FXML private Button instructorShowWithHighestNumOfStuds;
    @FXML private Button instructorShowall;
    @FXML private Button instructorWithZerocourse;

    @FXML private ChoiceBox<Instructor> chooseInstructorForCourse;
    @FXML private Label courseActionMessage;
    @FXML private Button courseAdd;
    @FXML private TextField courseDescription;
    @FXML private TableColumn<Course, String> courseDescriptionColumn;
    @FXML private TableColumn<?, ?> courseIdColumn;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, Instructor> courseInstructorColumn;
    @FXML private Button courseRemove;
    @FXML private TextField courseTitle;
    @FXML private TableColumn<Course, String> courseTitleColumn;
    @FXML private Button courseUpdate;
    @FXML private Label courseAvgStudentMessage;
    @FXML private Button courseLowStudent;
    @FXML private Button courseShownumOfstud;
    @FXML private Button courseShowAvgStud;
    @FXML private Button CourseShowAll;
    @FXML private TableColumn<Course, String> courseStudentColumn;

    @FXML private TableView<Enrollment> enrollTable;
    @FXML private TableColumn<Enrollment, Integer> enrollIdColumn;
    @FXML private TableColumn<Enrollment, Student> enrollStudentColumn;
    @FXML private TableColumn<Enrollment, Course> enrollCourseColumn;
    @FXML private TableColumn<Enrollment, Integer> enrollYearColumn;
    @FXML private TableColumn<Enrollment, String> enrollSemesterColumn;
    @FXML private TableColumn<Enrollment, String> enrollGradeColumn;

    @FXML private Label enrollActionMessage;
    @FXML private Button enrollRemove;
    @FXML private Button enrollUpdate;
    @FXML private Button enrollAdd;
    @FXML private Button enrollReset;
    @FXML private ChoiceBox<Student> enrollStudent;
    @FXML private ChoiceBox<Course> enrollCourse;
    @FXML private ChoiceBox<Student> enrollStudentFilter;
    @FXML private ChoiceBox<Course> enrollCourseFilter;
    @FXML private ChoiceBox<String> enrollSemesterFilter;
    @FXML private ChoiceBox<Integer> enrollYearFilter;

    private ObservableList<Enrollment> enrollmentList = FXCollections.observableArrayList();
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    @FXML private void navigateToCourseTab() {
        courseTab.getTabPane().getSelectionModel().select(courseTab);
    }
    @FXML private void navigateToEnrollmentTab() {
        enrollmentTab.getTabPane().getSelectionModel().select(enrollmentTab);
    }
    @FXML private void navigateToInstructorTab() {
        instructorTab.getTabPane().getSelectionModel().select(instructorTab);
    }
    @FXML private void navigateToMainTab() {
        mainTab.getTabPane().getSelectionModel().select(mainTab);
    }
    @FXML private void navigateToStudentTab() {
        studentTab.getTabPane().getSelectionModel().select(studentTab);
    }
    // Student Section
    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private StudentDAO studentDAO = new StudentDAO() {
        @Override
        public void delete(Student entity) {
            // Implementation here
        }

        @Override
        public List<Course> findCoursesByInstructorId(int instructorId) {
            return List.of();
        }
    };
    @FXML public void initialize() {
        // Initialize Student Table Columns
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        studentNameColumn.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        studentEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        studentPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));

        // Initialize Instructor Table Columns
        instructorIdColumn.setCellValueFactory(new PropertyValueFactory<>("instructorId"));
        instructorNameColumn.setCellValueFactory(new PropertyValueFactory<>("instructorName"));
        instructorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("instructorEmail"));
        instructorPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("instructorPhone"));
        instructorNumColumn.setCellValueFactory(cellData -> cellData.getValue().instructorNumProperty());
        // Maps to the getTotalCourses() method

        // Load data into both tables
        loadStudents();
        loadInstructors();

        // Set both tables editable
        studentTableView.setEditable(true);
        studentNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        studentEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        studentPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        instructorTable.setEditable(true);
        instructorNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        instructorEmailColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        instructorPhoneColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        courseTitleColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        /*courseStudentColumn.setCellFactory(new PropertyValueFactory<>("courseNum"));*/
        courseInstructorColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getInstructor()));
        courseInstructorColumn.setCellFactory(column -> new TableCell<Course, Instructor>() {

            @Override
            protected void updateItem(Instructor instructor, boolean empty) {
                super.updateItem(instructor, empty);
                if (empty || instructor == null) {
                    setText(null);
                } else {
                    setText(instructor.getInstructorName() + " (ID: " + instructor.getInstructorId() + ")");
                }
            }
        });
        // Make columns editable
        courseTable.setEditable(true);
        courseTitleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        courseDescriptionColumn.setCellFactory(TextFieldTableCell.forTableColumn());


        // Load courses and instructor IDs
        loadCourses();
        loadInstructorIds();
    }
    private void loadStudents() {
        studentList.setAll(studentDAO.findAll());
        studentTableView.setItems(studentList);
        enrollIdColumn.setCellValueFactory(new PropertyValueFactory<>("enrollmentId"));
        enrollStudentColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getStudent()));
        enrollStudentColumn.setCellFactory(column -> new TableCell<Enrollment, Student>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                if (empty || student == null) {
                    setText(null);
                } else {
                    setText(student.getStudentName() + " (ID: " + student.getStudentId() + ")");
                }
            }
        });
        enrollCourseColumn.setCellValueFactory(cellData ->
                new SimpleObjectProperty<>(cellData.getValue().getCourse()));
        enrollCourseColumn.setCellFactory(column -> new TableCell<Enrollment, Course>() {
            @Override
            protected void updateItem(Course course, boolean empty) {
                super.updateItem(course, empty);
                if (empty || course == null) {
                    setText(null);
                } else {
                    setText(course.getCourseName() + " (ID: " + course.getCourseId() + ")");
                }
            }
        });
        enrollYearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        enrollSemesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        enrollGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // Make grade column editable
        enrollTable.setEditable(true);

// Configure the grade column to be editable
        enrollGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        enrollGradeColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        enrollGradeColumn.setOnEditCommit(event -> {
            Enrollment enrollment = event.getRowValue();
            enrollment.setGrade(event.getNewValue());

            try {
                enrollmentDAO.update(enrollment);
                enrollActionMessage.setText("Enrollment updated successfully!");
            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update enrollment: " + e.getMessage());
                // Revert the change if update fails
                event.getTableView().refresh();
            }
        });

        // Load data
        loadEnrollments();

        // Setup ChoiceBoxes
        setupEnrollmentChoiceBoxes();
    }
    @FXML private void addStudent() {
        String name = studentName.getText().trim();
        String email = studentEmail.getText().trim();
        String phone = studentPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format!");
            return;
        }

        Student student = new Student(0, name, email, phone);
        int id = studentDAO.insert(student);

        if (id > 0) {
            student.setStudentId(id);
            studentList.add(student);
            clearStudentFields();

            studentActionMessage.setText("Student added successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add student!");
        }
    }
    @FXML private void updateStudent() {
        Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No student selected for update!");
            return;
        }
        // Update logic
        studentDAO.update(selectedStudent);
        studentTableView.refresh();
    }
    @FXML private void removeStudent() {
        Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No student selected!");
            return;
        }

        studentDAO.delete(selectedStudent.getStudentId());
        studentList.remove(selectedStudent);
        studentActionMessage.setText("Student removed successfully.");
    }
    private void clearStudentFields() {
        studentName.clear();
        studentEmail.clear();
        studentPhone.clear();
    }
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private boolean isValidEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
    @FXML private void studentShow() {
        // Assuming you have a DAO or service to fetch students
        List<Student> allStudents = studentDAO.findAll();  // Fetch all students from the database

        // Convert the list of students to an ObservableList for TableView
        ObservableList<Student> studentObservableList = FXCollections.observableArrayList(allStudents);

        // Set the ObservableList to the TableView
        studentTableView.setItems(studentObservableList);
    }
    @FXML private void handleShowUnenrolledStudents() {
        try {
            List<Student> unenrolledStudents = studentDAO.findUnenrolledStudents();
            if (!unenrolledStudents.isEmpty()) {
                // Clear existing items in the ObservableList and add new ones
                studentList.clear();
                studentList.addAll(unenrolledStudents);

                // Bind the ObservableList to the TableView
                studentTableView.setItems(studentList);

                studentActionMessage.setText("Unenrolled students displayed successfully!");
            } else {
                studentActionMessage.setText("No unenrolled students found.");
            }
        } catch (Exception e) {
            studentActionMessage.setText("Error displaying unenrolled students.");
            e.printStackTrace(); // Replace with proper logging
        }
    }
// Instructor Section
    private ObservableList<Instructor> instructorList = FXCollections.observableArrayList();
    private InstructorDAO instructorDAO = new InstructorDAO() {
        @Override
        public void delete(Instructor entity) {

        }
        @Override
        public List<Course> findCoursesByInstructorId(int instructorId) {
            return List.of();
        }
    };
    @FXML private void showInstructorWithHighestNumOfStuds() {
        // Fetch top 3 instructors with the highest number of students
        List<Instructor> instructors = instructorDAO.findTop3InstructorsWithTotalStudents();

        if (!instructors.isEmpty()) {
            // Update the instructor's display number for students
            for (Instructor instructor : instructors) {
                instructor.setInstructorNumDisplay(String.valueOf(instructor.getTotalStudents())); // Set the number of students to display
            }

            // Set the table's items to the top 3 instructors
            instructorTable.getItems().setAll(instructors);

            // Set action message
            instructorActionMessage.setText("Displaying top 3 instructors with the highest number of unique students.");
        } else {
            instructorActionMessage.setText("No instructors found.");
        }
    }
    @FXML private void showAllInstructorsCourses() {
        List<Instructor> instructors = instructorDAO.findInstructorsWithTotalCourses();
        if (!instructors.isEmpty()) {
            for (Instructor instructor : instructors) {
                instructor.setInstructorNumDisplay(String.valueOf(instructor.getTotalCourses()));
            }
            instructorTable.getItems().setAll(instructors); // Show all instructors with their course counts
            instructorActionMessage.setText("Displaying all instructors with the number of courses they teach.");
        } else {
            instructorActionMessage.setText("No instructors found.");
        }
    }
    @FXML private void showInstructorsWithZeroCourses() {
        List<Instructor> instructors = instructorDAO.findInstructorsWithZeroCourses(); // Fetch instructors with 0 courses
        if (!instructors.isEmpty()) {
            instructorTable.getItems().setAll(instructors); // Populate the TableView with the instructors
            instructorActionMessage.setText("Displaying instructors with 0 courses.");
        } else {
            instructorActionMessage.setText("No instructors found with 0 courses.");
        }
    }
    @FXML private void showAllInstructorsStudent() {
        List<Instructor> instructors = instructorDAO.findInstructorsWithTotalStudents();
        if (!instructors.isEmpty()) {
            for (Instructor instructor : instructors) {
                instructor.setInstructorNumDisplay(String.valueOf(instructor.getTotalStudents()));
            }
            instructorTable.getItems().setAll(instructors); // Show all instructors with their student counts
            instructorActionMessage.setText("Displaying all instructors with the number of unique students.");
        } else {
            instructorActionMessage.setText("No instructors found.");
        }
    }
    @FXML private void showAllInstructors() {
        // Fetch all instructors (with or without student count, depending on the need)
        List<Instructor> instructors = instructorDAO.findInstructorsWithTotalStudents(); // Or use findAll if you don't need student counts

        // Update TableView with instructors
        instructorTable.getItems().setAll(instructors);

        // Set action message
        instructorActionMessage.setText("Displaying all instructors with student counts");
    }
@FXML private void updateInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No instructor selected for update!");
            return;
        }
    }
    @FXML public void addInstructor() {
        String name = instructorName.getText().trim();
        String email = instructorEmail.getText().trim();
        String phone = instructorPhone.getText().trim();
        // Check if any field is empty
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out!");
            return;
        }
        // Validate email format
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format!");
            return;
        }
        // Create a new Instructor object
        Instructor newInstructor = new Instructor(0, name, email, phone);
        int id = instructorDAO.insert(newInstructor);
        // Check if insertion was successful
        if (id > 0) {
            newInstructor.setInstructorId(id);
            instructorList.add(newInstructor);
            loadInstructors(); // Refresh table with updated database content
            clearInstructorFields(); // Clear the fields after successful addition
            instructorActionMessage.setText("Instructor added successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add instructor!");
        }
    }
    @FXML public void removeInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            instructorActionMessage.setText("No instructor selected to remove.");
            return;
        }

        instructorDAO.delete(selectedInstructor.getInstructorId());
        instructorList.remove(selectedInstructor);
        instructorActionMessage.setText(selectedInstructor.getInstructorName() + " deleted.");
    }
    private void clearInstructorFields() {
        instructorName.clear();
        instructorEmail.clear();
        instructorPhone.clear();
    }
    private void loadInstructors() {
        instructorList.clear();
        instructorList.addAll(instructorDAO.findAll());
        instructorTable.setItems(instructorList);
    }
private ObservableList<Course> courseList = FXCollections.observableArrayList();
    private CourseDAO courseDAO = new CourseDAO() {

    };
@FXML private void addCourse() {
        // Retrieve input values
        String title = courseTitle.getText().trim();
        String description = courseDescription.getText().trim();
        Instructor selectedInstructor = (Instructor) chooseInstructorForCourse.getValue();

        // Validate input fields
        if (title.isEmpty() || description.isEmpty() || selectedInstructor == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
            return;
        }

        // Create new course with selected instructor
        Course newCourse = new Course(0, title, description, selectedInstructor);

        // Insert course into database
        int courseId = courseDAO.insert(newCourse);
        if (courseId > 0) {
            // Set the generated ID and add to the list
            newCourse.setCourseId(courseId);
            courseList.add(newCourse);

            // Clear input fields and show success message
            courseActionMessage.setText("Course added successfully!");
            clearCourseFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add course!");
        }
    }
    @FXML private void removeCourse() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No course selected!");
            return;
        }

        courseDAO.delete(selectedCourse);
        courseList.remove(selectedCourse);
        courseActionMessage.setText("Course removed successfully!");
    }
    @FXML private void updateCourse() {
        Course selectedCourse = (Course) courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No course selected!");
            return;
        }

        String newTitle = selectedCourse.getCourseName();
        String newDescription = selectedCourse.getDescription();

        if (newTitle.isEmpty() || newDescription.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
            return;
        }

        courseDAO.update(selectedCourse);
        courseActionMessage.setText("Course updated successfully!");
    }

    private void loadCourses() {
        courseList.clear();
        courseList.addAll(courseDAO.findAll());
        courseTable.setItems(courseList);
    }

    private void loadInstructorIds() {
        // Clear current items
        chooseInstructorForCourse.getItems().clear();

        // Retrieve instructors from the database
        List<Instructor> instructors = instructorDAO.findAll();

        // Populate the ChoiceBox with Instructor objects
        if (instructors != null) {
            chooseInstructorForCourse.getItems().addAll(instructors);
        }

        // Set a custom StringConverter for displaying instructor names with IDs
        chooseInstructorForCourse.setConverter(new javafx.util.StringConverter<Instructor>() {
            @Override
            public String toString(Instructor instructor) {
                // Display format: "Name (ID)"
                return instructor != null ? instructor.getInstructorName() + " (" + instructor.getInstructorId() + ")" : "";
            }

            @Override
            public Instructor fromString(String string) {
                // Reverse conversion is not needed in this context
                return null;
            }
        });
    }
    @FXML private void handleCourseShowNumOfStud() {
        // Fetch all courses and student counts
        List<Course> courses = courseDAO.findAll();
        Map<String, Integer> studentCounts = courseDAO.getStudentCountByCourse();

        // Update the number of students for each course
        for (Course course : courses) {
            int numStudents = studentCounts.getOrDefault(course.getCourseName(), 0);
            course.setNumberOfStudents(numStudents); // Set the number of students
        }

        // Refresh the table with updated courses
        courseTable.getItems().setAll(courses);
        courseActionMessage.setText("Student numbers displayed for each course.");
        // Set up the student column to display the number of students
        courseStudentColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(
                        cellData.getValue().getNumberOfStudents()
                ))
        );
    }
    @FXML private void handleCourseShowAll() {
        try {
            // Retrieve all courses from the DAO
            List<Course> courses = courseDAO.findAll();

            if (courses.isEmpty()) {
                courseActionMessage.setText("No courses found.");
            } else {
                // Populate the TableView
                ObservableList<Course> courseList = FXCollections.observableArrayList(courses);
                courseTable.setItems(courseList);

                // Set the courseStudentColumn to " "
                courseStudentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(" "));

                courseActionMessage.setText("All courses loaded successfully.");
            }
        } catch (Exception e) {
            courseActionMessage.setText("Error loading courses: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearCourseFields () {
        courseTitle.clear();
        courseDescription.clear();
        chooseInstructorForCourse.setValue(null);
    }
    private void setupEnrollmentChoiceBoxes() {
        // Populate student ChoiceBox for enrollment
        List<Student> students = studentDAO.findAll();
        enrollStudent.getItems().clear();
        enrollStudent.getItems().addAll(students);
        enrollStudent.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student != null ? student.getStudentName() + " (ID: " + student.getStudentId() + ")" : "";
            }

            @Override
            public Student fromString(String string) {
                return null; // Not used
            }
        });

        // Populate course ChoiceBox for enrollment
        List<Course> courses = courseDAO.findAll();
        enrollCourse.getItems().clear();
        enrollCourse.getItems().addAll(courses);
        enrollCourse.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getCourseName() + " (ID: " + course.getCourseId() + ")" : "";
            }

            @Override
            public Course fromString(String string) {
                return null; // Not used
            }
        });

        // Populate student filter ChoiceBox
        enrollStudentFilter.getItems().clear();
        enrollStudentFilter.getItems().addAll(students);
        enrollStudentFilter.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student != null ? student.getStudentName() : "";
            }

            @Override
            public Student fromString(String string) {
                return null; // Not used
            }
        });

        // Populate course filter ChoiceBox
        enrollCourseFilter.getItems().clear();
        enrollCourseFilter.getItems().addAll(courses);
        enrollCourseFilter.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ? course.getCourseName() : "";
            }

            @Override
            public Course fromString(String string) {
                return null; // Not used
            }
        });

        // Populate semester filter ChoiceBox based on existing enrollments
        List<String> semesters = enrollmentDAO.findDistinctSemesters();
        enrollSemesterFilter.getItems().clear();
        enrollSemesterFilter.getItems().addAll(semesters);

        // Populate year filter ChoiceBox based on existing enrollments
        List<Integer> years = enrollmentDAO.findDistinctYears();
        enrollYearFilter.getItems().clear();
        enrollYearFilter.getItems().addAll(years);
    }

    @FXML private void handleCourseLowStudentAction() {
        try {
            int maxStudents = 5; // For example, courses with fewer than 5 students

            List<Course> lowStudentCourses = courseDAO.findCoursesWithFewStudents(maxStudents);

            if (lowStudentCourses.isEmpty()) {
                courseActionMessage.setText("No courses found with fewer than " + maxStudents + " students.");
            } else {
                // Clear existing table data
                courseTable.getItems().clear();

                // Populate the table with courses having few students
                courseTable.getItems().addAll(lowStudentCourses);

                // Set up the student column to display the number of students
                courseStudentColumn.setCellValueFactory(cellData ->
                        new SimpleStringProperty(String.valueOf(
                                cellData.getValue().getNumberOfStudents()
                        ))
                );

                courseActionMessage.setText("Found " + lowStudentCourses.size() + " courses with fewer than " + maxStudents + " students.");
            }
        } catch (Exception e) {
            courseActionMessage.setText("Error finding courses with few students.");
            e.printStackTrace();
        }
    }
    @FXML private void showAverageStudents() {
        int avgStudents = courseDAO.getAverageStudentsPerCourse();

        // Update the label with the result
        if (avgStudents >= 0) {
            courseActionMessage.setText(String.format("Average students per course: %d", avgStudents));
        } else {
            courseActionMessage.setText("Could not retrieve average students.");
        }
    }
    @FXML
    private void updateEnrollment() {
        Enrollment selectedEnrollment = enrollTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No enrollment selected!");
            return;
        }

        try {
            enrollmentDAO.update(selectedEnrollment);
            enrollActionMessage.setText("Enrollment updated successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update enrollment: " + e.getMessage());
        }
        enrollTable.refresh();

    }
    @FXML private void addEnrollment() {
        Student selectedStudent = enrollStudent.getValue();
        Course selectedCourse = enrollCourse.getValue();

        if (selectedStudent == null || selectedCourse == null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please select both a student and a course!");
            return;
        }

        // Create new enrollment with current year and semester
        int currentYear = LocalDate.now().getYear();
        String currentSemester = determineSemester();

        Enrollment newEnrollment = new Enrollment(0, selectedStudent, selectedCourse,
                currentYear, currentSemester, null);

        int enrollmentId = enrollmentDAO.insert(newEnrollment);
        if (enrollmentId > 0) {
            newEnrollment.setEnrollmentId(enrollmentId);
            enrollmentList.add(newEnrollment);
            enrollActionMessage.setText("Enrollment added successfully!");
            clearEnrollmentFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add enrollment!");
        }
    }
    @FXML private void removeEnrollment() {
        Enrollment selectedEnrollment = enrollTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No enrollment selected!");
            return;
        }

        enrollmentDAO.delete(selectedEnrollment.getEnrollmentId());
        enrollmentList.remove(selectedEnrollment);
        enrollActionMessage.setText("Enrollment removed successfully!");
    }
    @FXML private void updateEnrollment(ActionEvent event) {
        Enrollment selectedEnrollment = enrollTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No enrollment selected!");
            return;
        }
        try {
            enrollmentDAO.update(selectedEnrollment);
            enrollActionMessage.setText("Enrollment updated successfully!");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Update Error", "Failed to update enrollment!");
        }
    }
private void loadEnrollments() {
        enrollmentList.clear();
        enrollmentList.addAll(enrollmentDAO.findAll());
        enrollTable.setItems(enrollmentList);
        populateFilterChoiceBoxes();
    }

    private void clearEnrollmentFields() {
        enrollStudent.setValue(null);
        enrollCourse.setValue(null);
    }

    private String determineSemester() {
        int month = LocalDate.now().getMonthValue();
        if (month >= 1 && month <= 5) return "Spring";
        if (month >= 6 && month <= 8) return "Summer";
        return "Fall";
    }
    private void populateFilterChoiceBoxes() {
        // Populate Student Filter with Distinct Students and "All" option
        Set<Student> uniqueStudents = enrollmentList.stream()
                .map(Enrollment::getStudent)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        enrollStudentFilter.getItems().clear();
        enrollStudentFilter.getItems().add(null); // "All" option
        enrollStudentFilter.getItems().addAll(uniqueStudents);
        // Populate Course Filter with Distinct Courses and "All" option
        Set<Course> uniqueCourses = enrollmentList.stream()
                .map(Enrollment::getCourse)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        enrollCourseFilter.getItems().clear();
        enrollCourseFilter.getItems().add(null); // "All" option
        enrollCourseFilter.getItems().addAll(uniqueCourses);
        // Populate Semester Filter with Distinct Semesters and "All" option
        Set<String> uniqueSemesters = enrollmentList.stream()
                .map(Enrollment::getSemester)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        enrollSemesterFilter.getItems().clear();
        enrollSemesterFilter.getItems().add(null); // "All" option
        enrollSemesterFilter.getItems().addAll(uniqueSemesters);
        // Populate Year Filter with Distinct Years and "All" option
        Set<Integer> uniqueYears = enrollmentList.stream()
                .map(Enrollment::getYear)
                .collect(Collectors.toCollection(LinkedHashSet::new));
        enrollYearFilter.getItems().clear();
        enrollYearFilter.getItems().add(null); // "All" option
        enrollYearFilter.getItems().addAll(uniqueYears);
        // Add listeners to trigger filtering on selection
        enrollStudentFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterEnrollments());
        enrollCourseFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterEnrollments());
        enrollSemesterFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterEnrollments());
        enrollYearFilter.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> filterEnrollments());
    }
    @FXML
    private void filterEnrollments() {
        // Get filter values
        // Get filter values (null represents "All" option)
        Student filteredStudent = enrollStudentFilter.getValue();
        Course filteredCourse = enrollCourseFilter.getValue();
        String filteredSemester = enrollSemesterFilter.getValue();
        Integer filteredYear = enrollYearFilter.getValue();

        // Retrieve filtered list from DAO
        List<Enrollment> filteredList = enrollmentDAO.findFilteredEnrollments(
                filteredStudent, filteredCourse, filteredSemester, filteredYear
        );

        // Update table view
        enrollmentList.setAll(filteredList);
    }
    @FXML
    private void resetEnrollmentFilter() {
        // Clear all filter choice boxes
        enrollStudentFilter.setValue(null);
        enrollCourseFilter.setValue(null);
        enrollSemesterFilter.setValue(null);
        enrollYearFilter.setValue(null);
        // Reload all enrollments
        loadEnrollments();
    }


}

