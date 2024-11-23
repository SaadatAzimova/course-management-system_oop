package org.example.finaloop1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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
    @FXML private Button instructorUupdate;
    @FXML private TableView<Instructor> instructorTable;

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

    private void loadInstructors() {
        instructorList.clear();
        instructorList.addAll(instructorDAO.findAll());
        instructorTable.setItems(instructorList);
        instructorTable.refresh();
    }
    @FXML
    private void addStudent() {
        String name = studentName.getText().trim();
        String email = studentEmail.getText().trim();
        String phone = studentPhone.getText().trim();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled out!");
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
            studentActionMessage.setText("Student added successfully: " + name);
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

        // Get the new values from input fields
        String updatedName = studentName.getText().trim();
        String updatedEmail = studentEmail.getText().trim();
        String updatedPhone = studentPhone.getText().trim();

        // Only check fields that have been filled in
        // If a field is empty, keep the original value
        if (updatedName.isEmpty()) {
            updatedName = selectedStudent.getStudentName();
        }
        if (updatedEmail.isEmpty()) {
            updatedEmail = selectedStudent.getEmail();
        }
        if (updatedPhone.isEmpty()) {
            updatedPhone = selectedStudent.getPhone();
        }

        // Validate email if it's different from the original
        if (!updatedEmail.equals(selectedStudent.getEmail()) && !isValidEmail(updatedEmail)) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format!");
            return;
        }

        // Check if any values are actually different
        boolean hasChanges = !updatedName.equals(selectedStudent.getStudentName()) ||
                !updatedEmail.equals(selectedStudent.getEmail()) ||
                !updatedPhone.equals(selectedStudent.getPhone());

        if (!hasChanges) {
            studentActionMessage.setText("No changes made to update.");
            return;
        }

        // Update the student object with new values
        selectedStudent.setStudentName(updatedName);
        selectedStudent.setEmail(updatedEmail);
        selectedStudent.setPhone(updatedPhone);

        // Update the database
        boolean success = studentDAO.update(selectedStudent);
        if (success) {
            studentTableView.refresh();
            clearStudentFields();
            studentActionMessage.setText("Student updated successfully: " + selectedStudent.getStudentName());
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update student!");
        }
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
        studentActionMessage.setText("Student removed successfully: " + selectedStudent.getStudentName());
    }


    private void loadStudents() {
        studentList.clear();
        studentList.addAll(studentDAO.findAll());
        studentTableView.setItems(studentList);
    }

    private void clearStudentFields() {
        studentName.clear();
        studentEmail.clear();
        studentPhone.clear();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return email.matches(emailRegex);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
// Instructor Section

    private ObservableList<Instructor> instructorList = FXCollections.observableArrayList();
    private InstructorDAO instructorDAO = new InstructorDAO() {
        @Override
        public void delete(Instructor entity) {

        }
    };




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
        Instructor instructor = new Instructor(0, name, email, phone);
        int id = instructorDAO.insert(instructor);

        // Check if insertion was successful
        if (id > 0) {
            instructor.setInstructorId(id);
            instructorList.add(instructor);
            loadInstructors(); // Refresh table with updated database content
            clearInstructorFields(); // Clear the fields after successful addition
            showAlert(Alert.AlertType.INFORMATION, "Success", "Instructor added successfully!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to add instructor!");
        }
    }
    @FXML
    public void updateInstructor() {
        Instructor selectedInstructor = instructorTable.getSelectionModel().getSelectedItem();
        if (selectedInstructor == null) {
            instructorActionMessage.setText("No instructor selected to update.");
            return;
        }

        // Get the new values, if empty, keep the existing values
        String updatedName = instructorName.getText().trim();
        String updatedEmail = instructorEmail.getText().trim();
        String updatedPhone = instructorPhone.getText().trim();

        // Store original values
        String originalName = selectedInstructor.getInstructorName();
        String originalEmail = selectedInstructor.getInstructorEmail();
        String originalPhone = selectedInstructor.getInstructorPhone();

        // Only update fields that have been changed
        boolean hasChanges = false;

        if (!updatedName.isEmpty() && !updatedName.equals(originalName)) {
            selectedInstructor.setInstructorName(updatedName);
            hasChanges = true;
        }
        if (!updatedEmail.isEmpty() && !updatedEmail.equals(originalEmail)) {
            if (!isValidEmail(updatedEmail)) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Invalid email format!");
                return;
            }
            selectedInstructor.setInstructorEmail(updatedEmail);
            hasChanges = true;
        }
        if (!updatedPhone.isEmpty() && !updatedPhone.equals(originalPhone)) {
            selectedInstructor.setInstructorPhone(updatedPhone);
            hasChanges = true;
        }

        if (!hasChanges) {
            instructorActionMessage.setText("No changes made to update.");
            return;
        }

        // Update in database and handle any exceptions
        try {
            instructorDAO.update(selectedInstructor); // No need for success check as DAO method doesn't return anything
            instructorTable.refresh(); // Refresh the table to reflect updated data
            clearInstructorFields(); // Clear the input fields
            instructorActionMessage.setText("Updated instructor: " + selectedInstructor.getInstructorName());
        } catch (Exception e) {
            // Revert changes if database update failed
            selectedInstructor.setInstructorName(originalName);
            selectedInstructor.setInstructorEmail(originalEmail);
            selectedInstructor.setInstructorPhone(originalPhone);
            instructorTable.refresh();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update instructor!");
        }
    }


    private void clearInstructorFields() {
        instructorName.clear();
        instructorEmail.clear();
        instructorPhone.clear();
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





}
