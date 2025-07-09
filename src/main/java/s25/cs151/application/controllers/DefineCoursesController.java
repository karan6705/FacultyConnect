package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import s25.cs151.application.models.Course;
import s25.cs151.application.storage.Database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DefineCoursesController {

    @FXML
    private TextField courseCodeField, courseNameField, sectionField;
    @FXML
    private Button addCourseButton, backButton;
    @FXML
    private TableView<Course> coursesTable;
    @FXML
    private TableColumn<Course, String> codeColumn, nameColumn, sectionColumn;

    // Change the file name to use a .txt file instead of .csv
    private final String COURSES_FILE = "courses.txt";
    private final Database coursesDb = new Database(COURSES_FILE, ",");

    private final ObservableList<Course> coursesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {  // Method that initializes Course Code, Student Name, and Section Number data to view in Your Courses view
        codeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCode()));
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getName()));
        sectionColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getSection()));

        loadCourses();
    }

    @FXML
    private void addCourse() {  // Method to add a new course to the course list
        String code = courseCodeField.getText().trim();
        String name = courseNameField.getText().trim();
        String section = sectionField.getText().trim();

        if (code.isEmpty() || name.isEmpty() || section.isEmpty()) {    // Throws error message if any input box is left empty.
            showAlert("Input Error", "All fields must be filled.");
            return;
        }

        // Prevent duplicate entries based on the combination: course code, course name, section.
        for (Course c : coursesList) {
            if (c.getCode().equalsIgnoreCase(code) &&
                    c.getName().equalsIgnoreCase(name) &&
                    c.getSection().equalsIgnoreCase(section)) {
                showAlert("Duplicate Entry", "This course already exists.");
                return;
            }
        }

        Course newCourse = new Course(code, name, section);
        coursesList.add(newCourse);
        saveCourses();
        sortCourses();
        coursesTable.refresh();

        // Clear input fields.
        courseCodeField.clear();
        courseNameField.clear();
        sectionField.clear();
    }

    private void loadCourses() {    // Method to load courses data from courses.txt file
        coursesList.clear();
        List<String[]> rows = coursesDb.readData();
        for (String[] row : rows) {
            if (row.length == 3) {
                Course course = Course.fromCSVRow(row);
                if (course != null) {
                    coursesList.add(course);
                }
            }
        }
        sortCourses();
        coursesTable.setItems(coursesList);
    }

    private void saveCourses() {    // Method to add courses to courses.txt file for storage
        List<String[]> data = new ArrayList<>();
        for (Course c : coursesList) {
            data.add(c.toCSVRow());
        }
        coursesDb.writeData(data);
    }

    private void sortCourses() {
        // Sort courses descending by course code.
        coursesList.sort(Comparator.comparing(Course::getCode, Comparator.reverseOrder()));
    }

    private void showAlert(String title, String message) {  // Method to throw error messages when needed
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Back button action: navigate back to the MainView (home page)
    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/controllers/Home.fxml"));
            Parent mainView = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(mainView, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
