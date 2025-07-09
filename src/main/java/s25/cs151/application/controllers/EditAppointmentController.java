package s25.cs151.application.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import s25.cs151.application.models.Appointment;
import s25.cs151.application.models.DetailedAppointment;
import s25.cs151.application.storage.FileLoaderUtil;
import s25.cs151.application.view.SceneManager;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class EditAppointmentController {

    @FXML private TextField studentNameField, reasonField, commentField;
    @FXML private DatePicker scheduleDatePicker;
    @FXML private ComboBox<String> timeSlotComboBox, courseComboBox;
    @FXML private Button saveButton, cancelButton;

    private final String TIMESLOTS_FILE = "semester_time_slots.txt";
    private final String COURSES_FILE   = "courses.txt";
    private final String SCHEDULE_FILE  = "office_hours_schedule.txt";

    private Appointment originalAppointment;
    private String      originalLine;

    /** FXML to return to (set by caller) */
    private String returnFXML = "/s25/cs151/application/controllers/SearchSchedule.fxml";

    /**
     * Called by the launcher to set both the appointment to edit
     * and the FXML path to return to.
     */
    public void setContext(Appointment appt, String returnFXML) {
        this.returnFXML = returnFXML;
        setAppointment(appt);
    }

    /** Initialize ComboBoxes. */
    @FXML
    public void initialize() {
        timeSlotComboBox.setItems(
                javafx.collections.FXCollections.observableArrayList(
                        FileLoaderUtil.loadTimeSlotsFromFile(TIMESLOTS_FILE)
                )
        );
        courseComboBox.setItems(
                javafx.collections.FXCollections.observableArrayList(
                        FileLoaderUtil.loadCoursesFromFile(COURSES_FILE)
                )
        );
    }

    public void setAppointment(Appointment appt) {
        this.originalAppointment = appt;
        this.originalLine = String.join(",", appt.toCSVRow());

        studentNameField.setText(appt.getStudentName());
        scheduleDatePicker.setValue(java.time.LocalDate.parse(appt.getScheduleDate()));
        timeSlotComboBox.getSelectionModel().select(appt.getTimeSlot());
        courseComboBox.getSelectionModel().select(appt.getCourse());

        // Check if the appointment is a DetailedAppointment and set reason and comment
        if (appt instanceof DetailedAppointment) {
            DetailedAppointment detailedAppt = (DetailedAppointment) appt;
            reasonField.setText(detailedAppt.getReason());
            commentField.setText(detailedAppt.getComment());
        } else {
            // If it's a regular Appointment, just clear the reason and comment fields
            reasonField.clear();
            commentField.clear();
        }
    }

    @FXML
    public void handleSave(ActionEvent event) {
        String studentName = studentNameField.getText().trim();
        java.time.LocalDate date = scheduleDatePicker.getValue();
        String timeSlot = timeSlotComboBox.getValue();
        String course   = courseComboBox.getValue();
        String reason   = reasonField.getText().trim();
        String comment  = commentField.getText().trim();

        if (studentName.isEmpty() || date == null || timeSlot == null || course == null) {
            showAlert("Input Error",
                    "Student name, date, time slot, and course are required.");
            return;
        }

        // Create the updated appointment using the appropriate class
        Appointment updated;
        if (reason.isEmpty() && comment.isEmpty()) {
            // If reason and comment are empty, create a regular Appointment
            updated = new Appointment(studentName, date.toString(), timeSlot, course);
        } else {
            // If reason and comment are provided, create a DetailedAppointment
            updated = new DetailedAppointment(studentName, date.toString(), timeSlot, course, reason, comment);
        }

        // Convert the appointment to CSV format
        String newLine = String.join(",", updated.toCSVRow());

        try {
            List<String> lines = Files.readAllLines(Path.of(SCHEDULE_FILE));
            boolean found = false;

            for (int i = 0; i < lines.size(); i++) {
                Appointment existing = Appointment.fromCSVRow(lines.get(i).split(",", -1));
                if (existing != null &&
                        existing.getStudentName().equals(originalAppointment.getStudentName()) &&
                        existing.getScheduleDate().equals(originalAppointment.getScheduleDate()) &&
                        existing.getTimeSlot().equals(originalAppointment.getTimeSlot())) {

                    lines.set(i, newLine);
                    found = true;
                    break;
                }
            }

            if (found) {
                Files.write(Paths.get(SCHEDULE_FILE), lines, StandardOpenOption.TRUNCATE_EXISTING);
                showAlert("Success", "Appointment updated.");
            } else {
                showAlert("Error", "Appointment not found for update.");
            }

            // Return to the page that launched us
            SceneManager.switchScene(event, returnFXML, 600, 400);

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to update appointment.");
        }
    }

    /** Cancel and return. */
    @FXML
    public void handleCancel(ActionEvent event) {
        SceneManager.switchScene(event, returnFXML, 600, 400);
    }

    private void showAlert(String title, String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setTitle(title);
        a.showAndWait();
    }
}