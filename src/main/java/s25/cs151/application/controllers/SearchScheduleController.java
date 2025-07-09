package s25.cs151.application.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import s25.cs151.application.models.Appointment;
import s25.cs151.application.models.DetailedAppointment;
import s25.cs151.application.view.SceneManager;

import java.io.*;

public class SearchScheduleController {
    @FXML private TextField                    searchField;
    @FXML private TableView<Appointment>      scheduleTable;
    @FXML private TableColumn<Appointment,String> studentNameCol, dateCol, timeSlotCol, courseCol, reasonCol, commentCol;
    @FXML private TableColumn<Appointment,Void>    actionCol;

    private final String SCHEDULE_FILE = "office_hours_schedule.txt";
    private final ObservableList<Appointment> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1) configure columns
        studentNameCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getStudentName()));
        dateCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getScheduleDate()));
        timeSlotCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getTimeSlot()));
        courseCol.setCellValueFactory(d -> new javafx.beans.property.SimpleStringProperty(d.getValue().getCourse()));
        reasonCol.setCellValueFactory(cellData -> {
            Appointment app = cellData.getValue();
            if (app instanceof DetailedAppointment detailed) {
                return new javafx.beans.property.SimpleStringProperty(detailed.getReason());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        commentCol.setCellValueFactory(cellData -> {
            Appointment app = cellData.getValue();
            if (app instanceof DetailedAppointment detailed) {
                return new javafx.beans.property.SimpleStringProperty(detailed.getComment());
            } else {
                return new javafx.beans.property.SimpleStringProperty("");
            }
        });

        // 2) load from file
        loadData();

        // 3) wrap in FilteredList
        FilteredList<Appointment> filtered = new FilteredList<>(masterData, p -> true);
        searchField.textProperty().addListener((obs, old, nw) -> {
            String term = nw.toLowerCase().trim();
            filtered.setPredicate(appt ->
                    term.isEmpty() ||
                            appt.getStudentName().toLowerCase().contains(term)
            );
        });

        // 4) wrap in SortedList with custom comparator (date desc, then time desc)
        SortedList<Appointment> sorted = new SortedList<>(filtered);
        sorted.setComparator((a1, a2) -> {
            int cmpDate = a2.getScheduleDate().compareTo(a1.getScheduleDate());
            if (cmpDate != 0) return cmpDate;
            // compare start time substring "HH:mm"
            String t1 = a1.getTimeSlot().substring(0,5), t2 = a2.getTimeSlot().substring(0,5);
            return t2.compareTo(t1);
        });

        scheduleTable.setItems(sorted);

        // 5) add Delete button in each row
        addDeleteButtons();
    }

    private void loadData() {
        masterData.clear();
        File f = new File(SCHEDULE_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length >= 4) {
                    Appointment app = Appointment.fromCSVRow(parts);
                    if (app != null) masterData.add(app);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addDeleteButtons() {
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button btn = new Button("Delete");
            {
                btn.setOnAction(e -> {
                    Appointment appt = getTableView().getItems().get(getIndex());
                    masterData.remove(appt);
                    saveAll(masterData);
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

    private void saveAll(ObservableList<Appointment> list) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SCHEDULE_FILE))) {
            for (Appointment a : list) {
                bw.write(String.join(",", a.toCSVRow()));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        SceneManager.switchScene(event, "/s25/cs151/application/controllers/Home.fxml", 600, 400);
    }
}
