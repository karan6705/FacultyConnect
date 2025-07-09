package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import s25.cs151.application.models.Appointment;
import s25.cs151.application.models.DetailedAppointment;
import s25.cs151.application.view.SceneManager;

import java.io.*;

public class ManageAppointmentsController {
    @FXML private TextField searchField;
    @FXML private TableView<Appointment> scheduleTable;
    @FXML private TableColumn<Appointment, String> studentNameCol,
            dateCol,
            timeSlotCol,
            courseCol,
            reasonCol,
            commentCol;

    private final String SCHEDULE_FILE = "office_hours_schedule.txt";
    private final ObservableList<Appointment> masterData =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {  // Loads student office hour data
        // Bind columns to Appointment properties
        studentNameCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getStudentName()));
        dateCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getScheduleDate()));
        timeSlotCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getTimeSlot()));
        courseCol.setCellValueFactory(d ->
                new javafx.beans.property.SimpleStringProperty(
                        d.getValue().getCourse()));

        // Display reason/comment only if DetailedAppointment
        reasonCol.setCellValueFactory(d -> {
            Appointment appt = d.getValue();
            if (appt instanceof DetailedAppointment) {
                return new javafx.beans.property.SimpleStringProperty(
                        ((DetailedAppointment) appt).getReason());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        commentCol.setCellValueFactory(d -> {
            Appointment appt = d.getValue();
            if (appt instanceof DetailedAppointment) {
                return new javafx.beans.property.SimpleStringProperty(
                        ((DetailedAppointment) appt).getComment());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // Load data from file
        loadData();

        // Wrap in FilteredList for search
        FilteredList<Appointment> filtered =
                new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((obs, oldV, newV) -> {
            String term = newV.toLowerCase().trim();
            filtered.setPredicate(appt ->
                    term.isEmpty() ||
                            appt.getStudentName().toLowerCase().contains(term)
            );
        });

        // Wrap in SortedList for date/time descending order
        SortedList<Appointment> sorted = new SortedList<>(filtered,
                (a1, a2) -> {
                    int cmp = a2.getScheduleDate().compareTo(a1.getScheduleDate());
                    if (cmp != 0) return cmp;
                    String t1 = a1.getTimeSlot().substring(0, 5),
                            t2 = a2.getTimeSlot().substring(0, 5);
                    return t2.compareTo(t1);
                }
        );
        scheduleTable.setItems(sorted);
    }

    private void loadData() {   // Loads data from text file for viewing
        masterData.clear();
        File file = new File(SCHEDULE_FILE);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                Appointment appt = Appointment.fromCSVRow(parts);
                if (appt != null) {
                    masterData.add(appt);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Opens EditAppointment, passing this page as return‐FXML. */
    @FXML
    private void handleEditSelected(ActionEvent event) {
        Appointment appt = scheduleTable.getSelectionModel().getSelectedItem();
        if (appt == null) {
            new Alert(Alert.AlertType.INFORMATION,
                    "Please select a row to edit.", ButtonType.OK).showAndWait();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/s25/cs151/application/controllers/EditAppointment.fxml"));
            Parent root = loader.load();

            EditAppointmentController ctrl = loader.getController();
            ctrl.setContext(appt,
                    "/s25/cs151/application/controllers/ManageAppointments.fxml");

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Back to Home */
    @FXML
    private void handleBack(ActionEvent event) {
        SceneManager.switchScene(event,
                "/s25/cs151/application/controllers/Home.fxml", 600, 400);
    }
}
