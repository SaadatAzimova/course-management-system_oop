package org.example.finaloop1;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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

    @FXML private Label courseActionMessage;
    @FXML private Button courseAdd;
    @FXML private TextField courseDescription;
    @FXML private TableColumn<?, ?> courseDescriptionColumn;
    @FXML private TableColumn<?, ?> courseIdColumn;
    @FXML private TableView<?> courseInitialize;
    @FXML private TableColumn<?, ?> courseInstructorColumn;
    @FXML private Button courseRemove;
    @FXML private TextField courseTitle;
    @FXML private TableColumn<?, ?> courseTitleColumn;
    @FXML private Button courseUpdate;


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
    }

    private void loadStudents() {
        studentList.setAll(studentDAO.findAll());
        studentTableView.setItems(studentList);
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


// Instructor Section

    private ObservableList<Instructor> instructorList = FXCollections.observableArrayList();
    private InstructorDAO instructorDAO = new InstructorDAO() {
        @Override
        public void delete(Instructor entity) {

        }
    };
    @FXML
    private void updateInstructor() {
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

}
