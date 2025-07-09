package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.scene.control.*;
import s25.cs151.application.models.Semester;
import s25.cs151.application.view.SceneManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OfficeHoursController {

    // Always read/write the same file in the user's home directory
    private static final String OFFICE_HOURS = "office_hours.txt";

    @FXML private ComboBox<String> semesterComboBox;
    @FXML private TextField yearField;
    @FXML private CheckBox mondayCheckBox, tuesdayCheckBox,
            wednesdayCheckBox, thursdayCheckBox, fridayCheckBox;
    @FXML private TableView<Semester> officeHoursTable;
    @FXML private TableColumn<Semester, String> semesterColumn,
            yearColumn,
            daysColumn;
    @FXML private Button backButton;

    private final ObservableList<Semester> officeHoursList =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {  // Initializes semester variables for displaying Office Hours
        semesterComboBox.setItems(FXCollections.observableArrayList(
                "Spring", "Summer", "Fall", "Winter"
        ));

        semesterColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getSemester()
                )
        );
        yearColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getYear()
                )
        );
        daysColumn.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(
                        data.getValue().getDays()
                )
        );

        loadOfficeHours();
        officeHoursTable.setItems(officeHoursList);
    }

    @FXML
    public void handleSaveOfficeHours() {   // Method for saving new office hours, with checks to catch any misinputs
        String semester = semesterComboBox.getValue();
        String year     = yearField.getText().trim();

        if (!isYearValid(year)) {
            showAlert(Alert.AlertType.ERROR, "Year must be a 4-digit number!");
            return;
        }

        String days = getSelectedDays();
        if (semester == null || semester.isEmpty() || days.isEmpty()) {
            showAlert(Alert.AlertType.ERROR,
                    "Semester and Days selection are required!");
            return;
        }

        Semester newSemester = new Semester(semester, year, days);
        if (isDuplicate(newSemester)) {
            showAlert(Alert.AlertType.ERROR,
                    "Duplicate entry! This office hour already exists.");
            return;
        }

        officeHoursList.add(newSemester);
        saveOfficeHours();
        sortOfficeHours();
        officeHoursTable.refresh();
        resetForm();
    }

    private boolean isYearValid(String year) {
        return year.length() == 4 && year.matches("\\d{4}");
    }

    private String getSelectedDays() {  // Initializes available days for office hours
        StringBuilder daysBuilder = new StringBuilder();
        if (mondayCheckBox.isSelected())   daysBuilder.append("Mon ");
        if (tuesdayCheckBox.isSelected())  daysBuilder.append("Tue ");
        if (wednesdayCheckBox.isSelected())daysBuilder.append("Wed ");
        if (thursdayCheckBox.isSelected()) daysBuilder.append("Thu ");
        if (fridayCheckBox.isSelected())   daysBuilder.append("Fri ");
        return daysBuilder.toString().trim();
    }

    private boolean isDuplicate(Semester newSemester) {
        return officeHoursList.stream()
                .anyMatch(oh ->
                        oh.getSemester().equals(newSemester.getSemester()) &&
                                oh.getYear().equals(newSemester.getYear())
                );
    }

    private void showAlert(Alert.AlertType type, String message) {  // Method for throwing errors
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.showAndWait();
    }

    private void resetForm() {      // Method for clearing form for new input
        semesterComboBox.getSelectionModel().clearSelection();
        yearField.clear();
        mondayCheckBox.setSelected(false);
        tuesdayCheckBox.setSelected(false);
        wednesdayCheckBox.setSelected(false);
        thursdayCheckBox.setSelected(false);
        fridayCheckBox.setSelected(false);
    }

    private void sortOfficeHours() {    // Method for sorting office hours from earliest to latest year, and then by semester
        officeHoursList.sort((oh1, oh2) -> {
            // Descending by year, then semester
            try {
                int y1 = Integer.parseInt(oh1.getYear());
                int y2 = Integer.parseInt(oh2.getYear());
                if (y1 != y2) return Integer.compare(y2, y1);
            } catch (NumberFormatException e) {
                int cmp = oh2.getYear().compareTo(oh1.getYear());
                if (cmp != 0) return cmp;
            }
            return oh2.getSemester().compareTo(oh1.getSemester());
        });
    }

    private void loadOfficeHours() {    // Method to load office hours from office_hours.txt
        officeHoursList.clear();
        File file = new File(OFFICE_HOURS);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    officeHoursList.add(
                            new Semester(parts[0], parts[1], parts[2])
                    );
                }
            }
            sortOfficeHours();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveOfficeHours() {    // Method to save office hours data to office_hours.txt
        File file = new File(OFFICE_HOURS);
        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(file))) {
            for (Semester oh : officeHoursList) {
                writer.write(
                        oh.getSemester() + "," +
                                oh.getYear()     + "," +
                                oh.getDays()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBack(ActionEvent event) { // Method to navigate back to home page
        SceneManager.switchScene(
                event,
                "/s25/cs151/application/controllers/Home.fxml",
                600, 400
        );
    }
}
