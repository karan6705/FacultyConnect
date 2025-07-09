// File: s25/cs151/application/controllers/OfficeHoursScheduleController.java
package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import s25.cs151.application.models.Appointment;
import s25.cs151.application.models.DetailedAppointment;

import java.io.*;
import java.time.LocalDate;

public class AddAppointmentController {

    @FXML
    private TextField studentNameField, reasonField, commentField;
    @FXML
    private DatePicker scheduleDatePicker;
    @FXML
    private ComboBox<String> timeSlotComboBox, courseComboBox;
    @FXML
    private Button backButton;

    // File names for persistence
    public final String TIMESLOTS_FILE = "semester_time_slots.txt";
    public final String COURSES_FILE = "courses.txt";
    public final String SCHEDULE_FILE = "office_hours_schedule.txt";

    @FXML
    public void initialize() {
        // Set default schedule date to today's date.
        scheduleDatePicker.setValue(LocalDate.now());

        // Populate time slot ComboBox.
        timeSlotComboBox.setItems(loadTimeSlots());
        if (!timeSlotComboBox.getItems().isEmpty()) {
            timeSlotComboBox.getSelectionModel().select(0);
        }

        // Populate course ComboBox.
        courseComboBox.setItems(loadCourses());
        if (!courseComboBox.getItems().isEmpty()) {
            courseComboBox.getSelectionModel().select(0);
        }
    }

    private ObservableList<String> loadTimeSlots() {
        ObservableList<String> items = FXCollections.observableArrayList();
        File file = new File(TIMESLOTS_FILE);
        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Each line is of the format: "fromTime,toTime" e.g., "16:30,16:45"
            while ((line = reader.readLine()) != null) {
                String[] chunks = line.split(",");
                if (chunks.length == 2) {
                    String timeSlot = chunks[0] + " - " + chunks[1];
                    items.add(timeSlot);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    private ObservableList<String> loadCourses() {
        ObservableList<String> items = FXCollections.observableArrayList();
        File file = new File(COURSES_FILE);
        if (!file.exists()) return items;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            // Expecting each line of the format: courseCode,courseName,section
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    // Display only code and section (e.g., CS151-04)
                    String displayValue = parts[0].trim() + "-" + parts[2].trim();
                    items.add(displayValue);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return items;
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String studentName = studentNameField.getText().trim();
        LocalDate scheduleDate = scheduleDatePicker.getValue();
        String timeSlot = timeSlotComboBox.getValue();
        String course = courseComboBox.getValue();
        String reason = reasonField.getText().trim();
        String comment = commentField.getText().trim();

        // Validate required fields.
        if (studentName.isEmpty() || scheduleDate == null || timeSlot == null || course == null) {
            showAlert("Input Error", "Student name, schedule date, time slot, and course are required.");
            return;
        }

        // Create a new OfficeHoursSchedule object.
        Appointment appointment;

        if (reason.isEmpty() && comment.isEmpty()) {
            appointment = new Appointment(
                    studentName,
                    scheduleDate.toString(),
                    timeSlot,
                    course
            );
        } else {
            appointment = new DetailedAppointment(
                    studentName,
                    scheduleDate.toString(),
                    timeSlot,
                    course,
                    reason,
                    comment
            );
        }

        // Save the new schedule entry.
        saveSchedule(appointment);

        // Optionally, clear the form or navigate elsewhere.
        showAlert("Success", "Office hours schedule saved successfully.");
        // Navigate back to MainView or clear fields. For example:
        handleBack(event);
    }

    private void saveSchedule(Appointment appointment) {
        // Append the new appointment to the schedule file.
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULE_FILE.toString(), true))) {
            String line = String.join(",", appointment.toCSVRow());
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save office hours schedule.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    @FXML
    public void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/controllers/Home.fxml"));
            Parent mainView = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
