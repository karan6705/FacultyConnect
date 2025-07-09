package s25.cs151.application.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import s25.cs151.application.models.DetailedAppointment;
import s25.cs151.application.models.Semester;
import s25.cs151.application.models.Appointment;
import s25.cs151.application.view.SceneManager;

import java.io.*;

public class ViewScheduleController {

    // Always read/write from here:
    private static final String OFFICE_HOURS_FILE = "office_hours.txt";
    private static final String APPT_SCHEDULE_FILE = "office_hours_schedule.txt";

    // --- OfficeHours table (first tab) ---
    @FXML private TableView<Semester> officeHoursTable;
    @FXML private TableColumn<Semester, String> yearCol;
    @FXML private TableColumn<Semester, String> semCol;
    @FXML private TableColumn<Semester, String> dayCol;

    // --- Appointments table (second tab) ---
    @FXML private TableView<Appointment> scheduleTable;
    @FXML private TableColumn<Appointment, String> studentNameCol;
    @FXML private TableColumn<Appointment, String> dateCol;
    @FXML private TableColumn<Appointment, String> timeSlotCol;
    @FXML private TableColumn<Appointment, String> courseCol;
    @FXML private TableColumn<Appointment, String> reasonCol;
    @FXML private TableColumn<Appointment, String> commentCol;

    @FXML
    public void initialize() {
        // Configure OfficeHours columns
        yearCol.setCellValueFactory(new PropertyValueFactory<>("year"));
        semCol.setCellValueFactory(new PropertyValueFactory<>("semester"));
        dayCol.setCellValueFactory(new PropertyValueFactory<>("days"));
        // Load and bind
        officeHoursTable.setItems(loadOfficeHours());

        // Configure Appointment columns
        studentNameCol.setCellValueFactory(new PropertyValueFactory<>("studentName"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("scheduleDate"));
        timeSlotCol.setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
        courseCol.setCellValueFactory(new PropertyValueFactory<>("course"));

        // Safely check for DetailedAppointment and retrieve reason/comment
        reasonCol.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment instanceof DetailedAppointment) {
                DetailedAppointment detailedAppointment = (DetailedAppointment) appointment;
                return new SimpleStringProperty(detailedAppointment.getReason());
            }
            return new SimpleStringProperty(""); // Return empty string if not DetailedAppointment
        });

        commentCol.setCellValueFactory(cellData -> {
            Appointment appointment = cellData.getValue();
            if (appointment instanceof DetailedAppointment) {
                DetailedAppointment detailedAppointment = (DetailedAppointment) appointment;
                return new SimpleStringProperty(detailedAppointment.getComment());
            }
            return new SimpleStringProperty(""); // Return empty string if not DetailedAppointment
        });

        // Load and bind
        scheduleTable.setItems(loadAppointments());
    }

    private ObservableList<Semester> loadOfficeHours() {
        ObservableList<Semester> list = FXCollections.observableArrayList();
        File file = new File(OFFICE_HOURS_FILE);
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length == 3) {
                    list.add(new Semester(parts[0], parts[1], parts[2]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private ObservableList<Appointment> loadAppointments() {
        ObservableList<Appointment> list = FXCollections.observableArrayList();
        File file = new File(APPT_SCHEDULE_FILE);
        if (!file.exists()) return list;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    Appointment app = Appointment.fromCSVRow(parts);
                    if (app != null) list.add(app);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/Home.fxml",
                600, 400);
    }
}
