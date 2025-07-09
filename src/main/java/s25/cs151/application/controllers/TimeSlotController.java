package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import s25.cs151.application.models.TimeSlot;

import java.io.*;
import java.util.Comparator;
import java.util.stream.IntStream;

public class TimeSlotController {
    @FXML
    private ComboBox<String> fromHourPicker, fromMinutePicker, toHourPicker, toMinutePicker;

    @FXML
    private TableView<TimeSlot> timeSlotsTable;
    @FXML
    private TableColumn<TimeSlot, String> fromTimeColumn;
    @FXML
    private TableColumn<TimeSlot, String> toTimeColumn;

    private final ObservableList<TimeSlot> timeSlotsList = FXCollections.observableArrayList();
    private final String FILE_NAME = "semester_time_slots.txt";

    @FXML
    public void initialize() {  // Method for professor to set time slots for students to choose from
        // Initialize hour and minute ComboBoxes
        fromHourPicker.setItems(FXCollections.observableArrayList(generateHourOptions()));
        fromMinutePicker.setItems(FXCollections.observableArrayList(generateMinuteOptions()));
        toHourPicker.setItems(FXCollections.observableArrayList(generateHourOptions()));
        toMinutePicker.setItems(FXCollections.observableArrayList(generateMinuteOptions()));

        // Set up table columns to display times
        fromTimeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFromTime()));
        toTimeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getToTime()));

        // Load time slots from file and display in the table
        loadTimeSlots();
        timeSlotsTable.setItems(timeSlotsList);
    }

    private ObservableList<String> generateHourOptions() {  // Method to create selectable hour times
        return FXCollections.observableArrayList(
                IntStream.range(0, 24)
                        .mapToObj(i -> String.format("%02d", i))
                        .toArray(String[]::new)
        );
    }

    private ObservableList<String> generateMinuteOptions() {    // Method to create selectable minute times
        return FXCollections.observableArrayList(
                IntStream.range(0, 60)
                        .filter(i -> i % 5 == 0)
                        .mapToObj(i -> String.format("%02d", i))
                        .toArray(String[]::new)
        );
    }

    @FXML
    public void handleAddTimeSlot() {   // Button to
        String fromHour = fromHourPicker.getValue();
        String fromMinute = fromMinutePicker.getValue();
        String toHour = toHourPicker.getValue();
        String toMinute = toMinutePicker.getValue();

        // Validate that all selections are made
        if (fromHour == null || fromMinute == null || toHour == null || toMinute == null) {
            showAlert("Incomplete Input", "Please select all time values.");
            return;
        }

        String fromTime = fromHour + ":" + fromMinute;
        String toTime = toHour + ":" + toMinute;
        if (fromTime.compareTo(toTime) >= 0) {
            showAlert("Invalid Time Slot", "The 'From' time must be before the 'To' time.");
            return;
        }

        // Create a new TimeSlot using the updated constructor order
        TimeSlot newSlot = new TimeSlot(
                Integer.parseInt(fromHour),
                Integer.parseInt(fromMinute),
                Integer.parseInt(toHour),
                Integer.parseInt(toMinute)
        );

        // Check for duplicate time slots
        if (timeSlotsList.stream().anyMatch(slot ->
                slot.getFromTime().equals(newSlot.getFromTime()) && slot.getToTime().equals(newSlot.getToTime()))) {
            showAlert("Duplicate Time Slot", "This time slot already exists.");
            return;
        }

        // Add the new time slot to the list and refresh the table
        timeSlotsList.add(newSlot);
        sortTimeSlots();
        timeSlotsTable.refresh();
    }

    // Helper method to sort time slots by their start time
    private void sortTimeSlots() {
        timeSlotsList.sort(Comparator.comparing(TimeSlot::getSortKey));
    }

    // Load time slots from the persistent file
    private void loadTimeSlots() {
        timeSlotsList.clear();
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] chunks = line.split(",");
                if (chunks.length == 2) {
                    // Split each time string into hour and minute parts
                    String[] fromChunk = chunks[0].split(":");
                    String[] toChunk = chunks[1].split(":");
                    // Create a new TimeSlot using the updated constructor order
                    TimeSlot slot = new TimeSlot(
                            Integer.parseInt(fromChunk[0]),
                            Integer.parseInt(fromChunk[1]),
                            Integer.parseInt(toChunk[0]),
                            Integer.parseInt(toChunk[1])
                    );
                    timeSlotsList.add(slot);
                }
            }
            sortTimeSlots();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Display an alert dialog to the user
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(title);
        alert.showAndWait();
    }

    // Go back to the MainView
    @FXML
    public void handleBack(javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/s25/cs151/application/controllers/Home.fxml"));
            Parent mainView = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(mainView, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save the current time slots to the file
    @FXML
    public void handleSaveTimeSlots(javafx.event.ActionEvent event) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            // Save each time slot as "HH:mm,HH:mm"
            for (TimeSlot slot : timeSlotsList) {
                writer.write(slot.getFromTime() + "," + slot.getToTime());
                writer.newLine();
            }
            showAlert("Success", "Time slots saved.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save time slots.");
        }
    }
}
