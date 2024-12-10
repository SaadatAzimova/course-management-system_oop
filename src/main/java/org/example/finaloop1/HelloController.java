package org.example.finaloop1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Callback;
import javafx.scene.control.TableColumn;
import javafx.beans.value.ObservableValue;
import javafx.util.StringConverter;

import java.awt.event.ActionEvent;
import javafx.scene.control.Alert;
import java.time.LocalDate;
import java.util.List;

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
    @FXML private TableColumn<Instructor, Integer> instructorNumColumn;
    @FXML private Button instructorNumOfCourses;
    @FXML private Button instructorNumOfStuds;
    @FXML private Button instructorShowWithHighestNumOfStuds;
    @FXML private Button instructorShowall;

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
    @FXML private TableColumn<?, ?> courseStudentColumn;

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
    @FXML private ChoiceBox<Student> enrollStudent;
    @FXML private ChoiceBox<Course> enrollCourse;
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
    @FXML
    public void initialize() {
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

    @FXML
    private void addStudent() {
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

    @FXML
    private void updateStudent() {
        Student selectedStudent = studentTableView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No student selected for update!");
            return;
        }
        // Update logic
        studentDAO.update(selectedStudent);
        studentTableView.refresh();
    }
    @FXML
    private void removeStudent() {
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
    @FXML
    private void studentShow() {
        // Assuming you have a DAO or service to fetch students
        List<Student> allStudents = studentDAO.findAll();  // Fetch all students from the database

        // Convert the list of students to an ObservableList for TableView
        ObservableList<Student> studentObservableList = FXCollections.observableArrayList(allStudents);

        // Set the ObservableList to the TableView
        studentTableView.setItems(studentObservableList);
    }

    @FXML
    private void handleShowUnenrolledStudents() {
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
    @FXML private void updateInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "No instructor selected for update!");
            return;
        }
    }
    @FXML
    public void addInstructor() {
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

    @FXML
    private void addCourse() {
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


    private void clearCourseFields () {
            courseTitle.clear();
            courseDescription.clear();
            chooseInstructorForCourse.setValue(null);
        }
    private void setupEnrollmentChoiceBoxes() {
        // Populate student ChoiceBox
        List<Student> students = studentDAO.findAll();
        enrollStudent.getItems().addAll(students);
        enrollStudent.setConverter(new StringConverter<Student>() {
            @Override
            public String toString(Student student) {
                return student != null ?
                        student.getStudentName() + " (ID: " + student.getStudentId() + ")" : "";
            }

            @Override
            public Student fromString(String string) {
                return null;
            }
        });

        // Populate course ChoiceBox
        List<Course> courses = courseDAO.findAll();
        enrollCourse.getItems().addAll(courses);
        enrollCourse.setConverter(new StringConverter<Course>() {
            @Override
            public String toString(Course course) {
                return course != null ?
                        course.getCourseName() + " (ID: " + course.getCourseId() + ")" : "";
            }

            @Override
            public Course fromString(String string) {
                return null;
            }
        });

        // Semester and Year filter ChoiceBoxes
        enrollSemesterFilter.getItems().addAll("Spring", "Summer", "Fall");

        // Populate years (current year and past few years)
        int currentYear = LocalDate.now().getYear();
        for (int year = currentYear; year >= currentYear - 5; year--) {
            enrollYearFilter.getItems().add(year);
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
    @FXML
    private void addEnrollment() {
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

    @FXML
    private void removeEnrollment() {
        Enrollment selectedEnrollment = enrollTable.getSelectionModel().getSelectedItem();
        if (selectedEnrollment == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Error", "No enrollment selected!");
            return;
        }

        enrollmentDAO.delete(selectedEnrollment.getEnrollmentId());
        enrollmentList.remove(selectedEnrollment);
        enrollActionMessage.setText("Enrollment removed successfully!");
    }

    @FXML
    private void updateEnrollment(ActionEvent event) {
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
    @FXML
    private void filterEnrollments() {
        // Get filter values
        Student filteredStudent = enrollStudent.getValue();
        Course filteredCourse = enrollCourse.getValue();
        String filteredSemester = enrollSemesterFilter.getValue();
        Integer filteredYear = enrollYearFilter.getValue();

        // Retrieve filtered list from DAO
        List<Enrollment> filteredList = enrollmentDAO.findFilteredEnrollments(
                filteredStudent, filteredCourse, filteredSemester, filteredYear
        );

        // Update table view
        enrollmentList.setAll(filteredList);
    }

    private void loadEnrollments() {
        enrollmentList.clear();
        enrollmentList.addAll(enrollmentDAO.findAll());
        enrollTable.setItems(enrollmentList);
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
    }


